package org.example.strategy.allocation;

import org.example.models.ParkingSlot;
import org.example.models.vehicle.Vehicle;

import java.util.Comparator;
import java.util.List;

public class NearestSlotStrategy implements SlotAllocationStrategy {
    @Override
    public ParkingSlot findSlot(List<ParkingSlot> availableSlots, Vehicle vehicle) {
        return availableSlots.stream()
                .filter(slot -> slot.canFit(vehicle))
                .min(Comparator.comparing(ParkingSlot::getFloor)
                        .thenComparing(ParkingSlot::getSlotId))
                .orElse(null);
    }
}
