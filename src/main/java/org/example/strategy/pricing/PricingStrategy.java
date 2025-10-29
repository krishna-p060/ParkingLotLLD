package org.example.strategy.pricing;

import org.example.models.ParkingSlot;
import org.example.models.Ticket;
import org.example.models.vehicle.Vehicle;

public interface PricingStrategy {
    double calculatePrice(Ticket ticket, ParkingSlot slot, Vehicle vehicle);
}
