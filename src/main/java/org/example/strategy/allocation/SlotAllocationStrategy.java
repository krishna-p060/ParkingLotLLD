package org.example.strategy.allocation;

import org.example.models.ParkingSlot;
import org.example.models.vehicle.Vehicle;

import java.util.List;

public interface SlotAllocationStrategy {
    ParkingSlot findSlot(List<ParkingSlot> availableSlots, Vehicle vehicle);
}