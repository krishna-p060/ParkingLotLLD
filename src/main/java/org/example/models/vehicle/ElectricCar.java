package org.example.models.vehicle;

import org.example.enums.VehicleType;

public class ElectricCar extends Vehicle {
    private boolean usingCharging;

    public ElectricCar(String licensePlate) {
        super(licensePlate, VehicleType.ELECTRIC_CAR);
        this.usingCharging = false;
    }

    public boolean isUsingCharging() {
        return usingCharging;
    }

    public void setUsingCharging(boolean usingCharging) {
        this.usingCharging = usingCharging;
    }
}
