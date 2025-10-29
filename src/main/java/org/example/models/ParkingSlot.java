package org.example.models;

import org.example.enums.SlotType;
import org.example.models.vehicle.ElectricCar;
import org.example.models.vehicle.Vehicle;

public class ParkingSlot {
    private final String slotId;
    private final SlotType type;
    private final int floor;
    private final boolean hasChargingStation;
    private Vehicle parkedVehicle;
    private boolean isOccupied;

    public ParkingSlot(String slotId, SlotType type, int floor, boolean hasChargingStation) {
        this.slotId = slotId;
        this.type = type;
        this.floor = floor;
        this.hasChargingStation = hasChargingStation;
        this.isOccupied = false;
    }

    public boolean canFit(Vehicle vehicle) {
        return !isOccupied && type.canFit(vehicle.getType()) &&
                (!vehicle.isElectric() || hasChargingStation);
    }

    public void parkVehicle(Vehicle vehicle) {
        if (!canFit(vehicle)) {
            throw new IllegalStateException("Cannot park vehicle in this slot");
        }
        this.parkedVehicle = vehicle;
        this.isOccupied = true;

        if (vehicle.isElectric() && hasChargingStation) {
            if (vehicle instanceof ElectricCar) {
                ((ElectricCar) vehicle).setUsingCharging(true);
            }
        }
    }

    public Vehicle removeVehicle() {
        Vehicle vehicle = parkedVehicle;
        parkedVehicle = null;
        isOccupied = false;

        if (vehicle != null && vehicle.isElectric()) {
            if (vehicle instanceof ElectricCar) {
                ((ElectricCar) vehicle).setUsingCharging(false);
            }
        }

        return vehicle;
    }

    public String getSlotId() { return slotId; }
    public SlotType getType() { return type; }
    public int getFloor() { return floor; }
    public boolean hasChargingStation() { return hasChargingStation; }
    public boolean isOccupied() { return isOccupied; }
    public Vehicle getParkedVehicle() { return parkedVehicle; }

    @Override
    public String toString() {
        return slotId + " (Floor " + floor + ", " + type +
                (hasChargingStation ? ", Charging Available" : "") + ")";
    }
}