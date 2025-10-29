package org.example.payment;

public class DigitalPayment implements PaymentMethod {
    private final String walletType;

    public DigitalPayment() {
        this.walletType = "DefaultWallet";
    }

    public DigitalPayment(String walletType) {
        this.walletType = walletType;
    }

    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing digital payment of $" + String.format("%.2f", amount) +
                " via " + walletType);
        return true;
    }

    @Override
    public String getPaymentType() {
        return "Digital";
    }
}
