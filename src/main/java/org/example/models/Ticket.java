package org.example.models;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public class Ticket {
    private static final AtomicLong ticketCounter = new AtomicLong(1);

    private final String ticketId;
    private final String vehicleLicense;
    private final String slotId;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double amount;
    private boolean isPaid;

    public Ticket(String vehicleLicense, String slotId) {
        this.ticketId = "TICKET-" + String.format("%06d", ticketCounter.getAndIncrement());
        this.vehicleLicense = vehicleLicense;
        this.slotId = slotId;
        this.entryTime = LocalDateTime.now();
        this.isPaid = false;
    }

    public String getTicketId() { return ticketId; }
    public String getVehicleLicense() { return vehicleLicense; }
    public String getSlotId() { return slotId; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public LocalDateTime getExitTime() { return exitTime; }
    public void setExitTime(LocalDateTime exitTime) { this.exitTime = exitTime; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public boolean isPaid() { return isPaid; }
    public void setPaid(boolean paid) { isPaid = paid; }

    @Override
    public String toString() {
        return "Ticket{" +
                "id='" + ticketId + '\'' +
                ", vehicle='" + vehicleLicense + '\'' +
                ", slot='" + slotId + '\'' +
                ", entryTime=" + entryTime +
                ", amount=" + amount +
                ", paid=" + isPaid +
                '}';
    }
}
