package org.example.strategy.pricing;

import org.example.enums.SlotType;
import org.example.models.ParkingSlot;
import org.example.models.Ticket;
import org.example.models.vehicle.ElectricCar;
import org.example.models.vehicle.Vehicle;

import java.util.HashMap;
import java.util.Map;

public class HourlyPricingStrategy implements PricingStrategy {
    private final Map<SlotType, Double> hourlyRates;
    private final double chargingRate;

    public HourlyPricingStrategy() {
        hourlyRates = new HashMap<>();
        hourlyRates.put(SlotType.MOTORCYCLE, 10.0);
        hourlyRates.put(SlotType.CAR, 20.0);
        hourlyRates.put(SlotType.TRUCK, 50.0);
        chargingRate = 5.0;
    }

    @Override
    public double calculatePrice(Ticket ticket, ParkingSlot slot, Vehicle vehicle) {
        long hours = java.time.Duration.between(ticket.getEntryTime(), ticket.getExitTime()).toHours();
        if (hours == 0) hours = 1;

        double basePrice = hourlyRates.get(slot.getType()) * hours;

        double chargingCost = 0;
        if (vehicle.isElectric()) {
            boolean usingCharging = false;
            if (vehicle instanceof ElectricCar) {
                usingCharging = ((ElectricCar) vehicle).isUsingCharging();
            }

            if (usingCharging) {
                chargingCost = chargingRate * hours;
            }
        }

        return basePrice + chargingCost;
    }
}
