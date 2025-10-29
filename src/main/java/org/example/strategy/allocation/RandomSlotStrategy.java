package org.example.strategy.allocation;

import org.example.models.ParkingSlot;
import org.example.models.vehicle.Vehicle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomSlotStrategy implements SlotAllocationStrategy {
    private final Random random = new Random();

    @Override
    public ParkingSlot findSlot(List<ParkingSlot> availableSlots, Vehicle vehicle) {
        List<ParkingSlot> suitableSlots = availableSlots.stream()
                .filter(slot -> slot.canFit(vehicle))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        if (suitableSlots.isEmpty()) {
            return null;
        }

        return suitableSlots.get(random.nextInt(suitableSlots.size()));
    }
}
