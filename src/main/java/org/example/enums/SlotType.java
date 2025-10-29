package org.example.enums;

public enum SlotType {
    MOTORCYCLE(1), CAR(2), TRUCK(3);

    private final int size;

    SlotType(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public boolean canFit(VehicleType vehicleType) {
        SlotType requiredSlot = getRequiredSlotType(vehicleType);
        return this.size >= requiredSlot.size;
    }

    public static SlotType getRequiredSlotType(VehicleType vehicleType) {
        switch (vehicleType) {
            case MOTORCYCLE:
            case ELECTRIC_MOTORCYCLE:
                return MOTORCYCLE;
            case CAR:
            case ELECTRIC_CAR:
                return CAR;
            case TRUCK:
                return TRUCK;
            default:
                throw new IllegalArgumentException("Unknown vehicle type: " + vehicleType);
        }
    }
}