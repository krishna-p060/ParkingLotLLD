package org.example.payment;

public interface PaymentMethod {
    boolean processPayment(double amount);
    String getPaymentType();
}
