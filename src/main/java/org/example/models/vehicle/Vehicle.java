package org.example.models.vehicle;

import org.example.enums.VehicleType;

public abstract class Vehicle {
    private final String licensePlate;
    private final VehicleType type;

    public Vehicle(String licensePlate, VehicleType type) {
        this.licensePlate = licensePlate;
        this.type = type;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public VehicleType getType() {
        return type;
    }

    public boolean isElectric() {
        return type == VehicleType.ELECTRIC_CAR || type == VehicleType.ELECTRIC_MOTORCYCLE;
    }

    @Override
    public String toString() {
        return type + " (" + licensePlate + ")";
    }
}
