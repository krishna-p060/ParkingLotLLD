package org.example;

import org.example.enums.SlotType;
import org.example.models.ParkingLot;
import org.example.models.ParkingSlot;
import org.example.models.Ticket;
import org.example.models.vehicle.*;
import org.example.payment.CardPayment;
import org.example.payment.CashPayment;
import org.example.payment.DigitalPayment;
import org.example.strategy.allocation.NearestSlotStrategy;
import org.example.strategy.allocation.RandomSlotStrategy;
import org.example.strategy.pricing.FlatPricingStrategy;
import org.example.strategy.pricing.HourlyPricingStrategy;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ParkingLot parkingLot = new ParkingLot("Premium Parking", new NearestSlotStrategy(), new HourlyPricingStrategy());

        setupParkingLot(parkingLot);

        try {
            parkingLot.displayAvailability();

            demoBasicParking(parkingLot);
            demoElectricVehicles(parkingLot);
            demoStrategyChanges(parkingLot);
            demoFullParkingLot(parkingLot);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void setupParkingLot(ParkingLot parkingLot) {
        parkingLot.addSlot(new ParkingSlot("F1-M001", SlotType.MOTORCYCLE, 1, false));
        parkingLot.addSlot(new ParkingSlot("F1-M002", SlotType.MOTORCYCLE, 1, true));
        parkingLot.addSlot(new ParkingSlot("F1-C001", SlotType.CAR, 1, false));
        parkingLot.addSlot(new ParkingSlot("F1-C002", SlotType.CAR, 1, true));
        parkingLot.addSlot(new ParkingSlot("F1-C003", SlotType.CAR, 1, true));
        parkingLot.addSlot(new ParkingSlot("F1-T001", SlotType.TRUCK, 1, false));

        parkingLot.addSlot(new ParkingSlot("F2-M001", SlotType.MOTORCYCLE, 2, false));
        parkingLot.addSlot(new ParkingSlot("F2-M002", SlotType.MOTORCYCLE, 2, true));
        parkingLot.addSlot(new ParkingSlot("F2-C001", SlotType.CAR, 2, false));
        parkingLot.addSlot(new ParkingSlot("F2-C002", SlotType.CAR, 2, true));
        parkingLot.addSlot(new ParkingSlot("F2-T001", SlotType.TRUCK, 2, false));

        parkingLot.addSlot(new ParkingSlot("F3-E001", SlotType.CAR, 3, true));
        parkingLot.addSlot(new ParkingSlot("F3-E002", SlotType.CAR, 3, true));
        parkingLot.addSlot(new ParkingSlot("F3-EM01", SlotType.MOTORCYCLE, 3, true));
    }

    private static void demoBasicParking(ParkingLot parkingLot) {
        System.out.println("\n === Basic Parking Operations ===");

        Vehicle car1 = new Car("ABC123");
        Vehicle motorcycle1 = new Motorcycle("XYZ789");
        Vehicle truck1 = new Truck("TRUCK01");

        Ticket ticket1 = parkingLot.parkVehicle(car1);
        Ticket ticket2 = parkingLot.parkVehicle(motorcycle1);
        Ticket ticket3 = parkingLot.parkVehicle(truck1);

        parkingLot.displayAvailability();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n === Processing Exits ===");
        parkingLot.exitVehicle(ticket1.getTicketId(), new CardPayment());
        parkingLot.exitVehicle(ticket2.getTicketId(), new CashPayment());
        parkingLot.exitVehicle(ticket3.getTicketId(), new DigitalPayment("PayPal"));

        parkingLot.displayAvailability();
    }

    private static void demoElectricVehicles(ParkingLot parkingLot) {
        System.out.println("\n=== Electric Vehicle Parking ===");

        Vehicle electricCar1 = new ElectricCar("EV001");
        Vehicle electricCar2 = new ElectricCar("EV002");

        Ticket evTicket1 = parkingLot.parkVehicle(electricCar1);
        Ticket evTicket2 = parkingLot.parkVehicle(electricCar2);

        parkingLot.displayAvailability();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n=== Electric Vehicle Exits (with charging costs) ===");
        parkingLot.exitVehicle(evTicket1.getTicketId(), new CardPayment("**** **** **** 5678"));
        parkingLot.exitVehicle(evTicket2.getTicketId(), new DigitalPayment("Tesla Wallet"));
    }

    private static void demoStrategyChanges(ParkingLot parkingLot) {
        System.out.println("\n=== Strategy Pattern - Changing Strategies ===");

        System.out.println("Switching to Random Slot Allocation Strategy");
        parkingLot.setAllocationStrategy(new RandomSlotStrategy());

        System.out.println("Switching to Flat Pricing Strategy");
        parkingLot.setPricingStrategy(new FlatPricingStrategy());

        Vehicle car2 = new Car("NEW001");
        Vehicle motorcycle2 = new Motorcycle("NEW002");

        Ticket newTicket1 = parkingLot.parkVehicle(car2);
        Ticket newTicket2 = parkingLot.parkVehicle(motorcycle2);

        parkingLot.displayAvailability();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n=== Exits with Flat Pricing ===");
        parkingLot.exitVehicle(newTicket1.getTicketId(), new CardPayment());
        parkingLot.exitVehicle(newTicket2.getTicketId(), new DigitalPayment("Apple Pay"));
    }

    private static void demoFullParkingLot(ParkingLot parkingLot) {
        System.out.println("\n=== Full Parking Lot Scenario ===");

        List<Vehicle> moreVehicles = Arrays.asList(
                new Car("FULL001"), new Car("FULL002"), new Car("FULL003"),
                new Car("FULL004"), new Car("FULL005"), new Car("FULL006"),
                new Motorcycle("FBIKE01"), new Motorcycle("FBIKE02"), new Motorcycle("FBIKE03"),
                new Truck("FTRUCK1"), new Truck("FTRUCK2"),
                new ElectricCar("FEV001"), new ElectricCar("FEV002")
        );

        for (Vehicle vehicle : moreVehicles) {
            try {
                Ticket ticket = parkingLot.parkVehicle(vehicle);
                System.out.println("Successfully parked: " + vehicle.getLicensePlate());
            } catch (RuntimeException e) {
                System.out.println("Failed to park " + vehicle.getLicensePlate() + ": " + e.getMessage());
            }
        }

        System.out.println("\n=== Final Parking Lot Status ===");
        parkingLot.displayAvailability();

        System.out.println("\n=== Active Tickets ===");
        parkingLot.getActiveTickets().forEach((ticketId, ticket) -> {
            System.out.println("• " + ticketId + " -> " + ticket.getVehicleLicense() +
                    " (Slot: " + ticket.getSlotId() + ")");
        });
    }
}