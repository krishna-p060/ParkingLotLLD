package org.example.payment;

public class CardPayment implements PaymentMethod {
    private final String cardNumber;

    public CardPayment() {
        this.cardNumber = "**** **** **** 1234";
    }

    public CardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing card payment of $" + String.format("%.2f", amount) +
                " using card " + cardNumber);
        return true;
    }

    @Override
    public String getPaymentType() {
        return "Card";
    }
}
