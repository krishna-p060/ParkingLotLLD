package org.example.payment;

public class CashPayment implements PaymentMethod {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing cash payment of $" + String.format("%.2f", amount));
        return true;
    }

    @Override
    public String getPaymentType() {
        return "Cash";
    }
}
