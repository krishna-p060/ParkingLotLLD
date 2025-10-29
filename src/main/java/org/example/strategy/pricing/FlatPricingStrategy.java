package org.example.strategy.pricing;

import org.example.enums.SlotType;
import org.example.models.ParkingSlot;
import org.example.models.Ticket;
import org.example.models.vehicle.ElectricCar;
import org.example.models.vehicle.Vehicle;

import java.util.HashMap;
import java.util.Map;

public class FlatPricingStrategy implements PricingStrategy {
    private final Map<SlotType, Double> flatRates;
    private final double chargingRate;

    public FlatPricingStrategy() {
        flatRates = new HashMap<>();
        flatRates.put(SlotType.MOTORCYCLE, 50.0);
        flatRates.put(SlotType.CAR, 100.0);
        flatRates.put(SlotType.TRUCK, 200.0);
        chargingRate = 25.0;
    }

    @Override
    public double calculatePrice(Ticket ticket, ParkingSlot slot, Vehicle vehicle) {
        double basePrice = flatRates.get(slot.getType());

        double chargingCost = 0;
        if (vehicle.isElectric()) {
            boolean usingCharging = false;
            if (vehicle instanceof ElectricCar) {
                usingCharging = ((ElectricCar) vehicle).isUsingCharging();
            }

            if (usingCharging) {
                chargingCost = chargingRate;
            }
        }

        return basePrice + chargingCost;
    }
}
