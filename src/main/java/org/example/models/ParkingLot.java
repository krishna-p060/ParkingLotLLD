package org.example.models;

import org.example.enums.SlotType;
import org.example.models.vehicle.Vehicle;
import org.example.payment.PaymentMethod;
import org.example.strategy.allocation.NearestSlotStrategy;
import org.example.strategy.allocation.SlotAllocationStrategy;
import org.example.strategy.pricing.HourlyPricingStrategy;
import org.example.strategy.pricing.PricingStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParkingLot {
    private final String name;
    private final List<ParkingSlot> slots;
    private final Map<String, Ticket> activeTickets;
    private SlotAllocationStrategy allocationStrategy;
    private PricingStrategy pricingStrategy;

    public ParkingLot(String name, SlotAllocationStrategy allocationStrategy, PricingStrategy pricingStrategy) {
        this.name = name;
        this.slots = new ArrayList<>();
        this.activeTickets = new HashMap<>();
        this.allocationStrategy = allocationStrategy;
        this.pricingStrategy = pricingStrategy;
    }

    public void addSlot(ParkingSlot slot) {
        slots.add(slot);
    }

    public void setAllocationStrategy(SlotAllocationStrategy strategy) {
        this.allocationStrategy = strategy;
    }

    public void setPricingStrategy(PricingStrategy strategy) {
        this.pricingStrategy = strategy;
    }

    public Ticket parkVehicle(Vehicle vehicle) {
        List<ParkingSlot> availableSlots = slots.stream()
                .filter(slot -> !slot.isOccupied())
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        ParkingSlot allocatedSlot = allocationStrategy.findSlot(availableSlots, vehicle);

        if (allocatedSlot == null) {
            throw new RuntimeException("No available slot for vehicle: " + vehicle.getLicensePlate());
        }

        allocatedSlot.parkVehicle(vehicle);
        Ticket ticket = new Ticket(vehicle.getLicensePlate(), allocatedSlot.getSlotId());
        activeTickets.put(ticket.getTicketId(), ticket);

        System.out.println("Vehicle " + vehicle.getLicensePlate() + " parked in slot " +
                allocatedSlot.getSlotId() + " on floor " + allocatedSlot.getFloor());
        System.out.println("Ticket ID: " + ticket.getTicketId());

        return ticket;
    }

    public double exitVehicle(String ticketId, PaymentMethod paymentMethod) {
        Ticket ticket = activeTickets.get(ticketId);
        if (ticket == null) {
            throw new IllegalArgumentException("Invalid ticket ID: " + ticketId);
        }

        ParkingSlot slot = findSlotById(ticket.getSlotId());
        if (slot == null) {
            throw new IllegalStateException("Slot not found: " + ticket.getSlotId());
        }

        Vehicle vehicle = slot.removeVehicle();
        ticket.setExitTime(LocalDateTime.now());

        double amount = pricingStrategy.calculatePrice(ticket, slot, vehicle);
        ticket.setAmount(amount);

        boolean paymentSuccess = paymentMethod.processPayment(amount);
        if (paymentSuccess) {
            ticket.setPaid(true);
            activeTickets.remove(ticketId);

            System.out.println("Vehicle " + vehicle.getLicensePlate() + " exited successfully");
            System.out.println("Total amount: $" + String.format("%.2f", amount) +
                    " (Payment: " + paymentMethod.getPaymentType() + ")");

            return amount;
        } else {
            slot.parkVehicle(vehicle);
            throw new RuntimeException("Payment failed for ticket: " + ticketId);
        }
    }

    private ParkingSlot findSlotById(String slotId) {
        return slots.stream()
                .filter(slot -> slot.getSlotId().equals(slotId))
                .findFirst()
                .orElse(null);
    }

    public void displayAvailability() {
        Map<SlotType, Long> availability = new HashMap<>();
        Map<SlotType, Long> occupied = new HashMap<>();

        for (SlotType type : SlotType.values()) {
            long availableCount = slots.stream()
                    .filter(slot -> slot.getType() == type && !slot.isOccupied())
                    .count();
            long occupiedCount = slots.stream()
                    .filter(slot -> slot.getType() == type && slot.isOccupied())
                    .count();

            availability.put(type, availableCount);
            occupied.put(type, occupiedCount);
        }

        System.out.println("\n === " + name + " Availability ===");
        for (SlotType type : SlotType.values()) {
            long avail = availability.get(type);
            long occ = occupied.get(type);
            long total = avail + occ;
            System.out.printf("%-12s: %d/%d available (%.1f%% occupancy)\n",
                    type, avail, total, total > 0 ? (occ * 100.0 / total) : 0);
        }

        long chargingAvailable = slots.stream()
                .filter(slot -> slot.hasChargingStation() && !slot.isOccupied())
                .count();
        long chargingTotal = slots.stream()
                .filter(ParkingSlot::hasChargingStation)
                .count();

        System.out.printf("Charging   : %d/%d available\n", chargingAvailable, chargingTotal);
        System.out.println("Active Tickets: " + activeTickets.size());
    }

    public String getName() { return name; }
    public List<ParkingSlot> getSlots() { return new ArrayList<>(slots); }
    public Map<String, Ticket> getActiveTickets() { return new HashMap<>(activeTickets); }
}
