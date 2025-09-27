package StrategyPattern;

import java.util.Scanner;

// Strategy interface
interface PaymentStrategy {
    void pay(double amount);
    String getPaymentMethod();
}

// Concrete Strategies
class CreditCardPayment implements PaymentStrategy {
    private String cardNumber;
    private String cardHolderName;
    
    public CreditCardPayment(String cardNumber, String cardHolderName) {
        this.cardNumber = cardNumber;
        this.cardHolderName = cardHolderName;
    }
    
    @Override
    public void pay(double amount) {
        System.out.println("Processing credit card payment...");
        System.out.println("Card Holder: " + cardHolderName);
        System.out.println("Card Number: " + maskCardNumber(cardNumber));
        System.out.println("Amount: $" + String.format("%.2f", amount));
        System.out.println("Payment successful via Credit Card!");
    }
    
    private String maskCardNumber(String cardNumber) {
        if (cardNumber.length() > 4) {
            return "****-****-****-" + cardNumber.substring(cardNumber.length() - 4);
        }
        return cardNumber;
    }
    
    @Override
    public String getPaymentMethod() {
        return "Credit Card";
    }
}

class PayPalPayment implements PaymentStrategy {
    private String email;
    
    public PayPalPayment(String email) {
        this.email = email;
    }
    
    @Override
    public void pay(double amount) {
        System.out.println("Processing PayPal payment...");
        System.out.println("PayPal Email: " + email);
        System.out.println("Amount: $" + String.format("%.2f", amount));
        System.out.println("Redirecting to PayPal...");
        System.out.println("Payment successful via PayPal!");
    }
    
    @Override
    public String getPaymentMethod() {
        return "PayPal";
    }
}

class BankTransferPayment implements PaymentStrategy {
    private String accountNumber;
    private String bankName;
    
    public BankTransferPayment(String accountNumber, String bankName) {
        this.accountNumber = accountNumber;
        this.bankName = bankName;
    }
    
    @Override
    public void pay(double amount) {
        System.out.println("Processing bank transfer...");
        System.out.println("Bank: " + bankName);
        System.out.println("Account: " + maskAccountNumber(accountNumber));
        System.out.println("Amount: $" + String.format("%.2f", amount));
        System.out.println("Transfer initiated successfully!");
    }
    
    private String maskAccountNumber(String accountNumber) {
        if (accountNumber.length() > 4) {
            return "****" + accountNumber.substring(accountNumber.length() - 4);
        }
        return accountNumber;
    }
    
    @Override
    public String getPaymentMethod() {
        return "Bank Transfer";
    }
}

class CryptocurrencyPayment implements PaymentStrategy {
    private String walletAddress;
    private String cryptoType;
    
    public CryptocurrencyPayment(String walletAddress, String cryptoType) {
        this.walletAddress = walletAddress;
        this.cryptoType = cryptoType;
    }
    
    @Override
    public void pay(double amount) {
        System.out.println("Processing cryptocurrency payment...");
        System.out.println("Crypto Type: " + cryptoType);
        System.out.println("Wallet Address: " + maskWalletAddress(walletAddress));
        System.out.println("Amount: $" + String.format("%.2f", amount));
        System.out.println("Transaction confirmed on blockchain!");
    }
    
    private String maskWalletAddress(String address) {
        if (address.length() > 8) {
            return address.substring(0, 4) + "..." + address.substring(address.length() - 4);
        }
        return address;
    }
    
    @Override
    public String getPaymentMethod() {
        return cryptoType + " Cryptocurrency";
    }
}

// Context class
class PaymentProcessor {
    private PaymentStrategy paymentStrategy;
    
    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }
    
    public void processPayment(double amount) {
        if (paymentStrategy == null) {
            System.out.println("No payment method selected!");
            return;
        }
        
        System.out.println("=== Payment Processing ===");
        System.out.println("Selected Payment Method: " + paymentStrategy.getPaymentMethod());
        paymentStrategy.pay(amount);
        System.out.println("========================\n");
    }
    
    public PaymentStrategy getCurrentStrategy() {
        return paymentStrategy;
    }
}

// Payment Strategy Factory
class PaymentStrategyFactory {
    
    public static PaymentStrategy createPaymentStrategy(String type, Scanner scanner) {
        switch (type.toLowerCase()) {
            case "credit":
            case "creditcard":
                System.out.print("Enter card number: ");
                String cardNumber = scanner.nextLine();
                System.out.print("Enter cardholder name: ");
                String cardHolderName = scanner.nextLine();
                return new CreditCardPayment(cardNumber, cardHolderName);
                
            case "paypal":
                System.out.print("Enter PayPal email: ");
                String email = scanner.nextLine();
                return new PayPalPayment(email);
                
            case "bank":
            case "banktransfer":
                System.out.print("Enter account number: ");
                String accountNumber = scanner.nextLine();
                System.out.print("Enter bank name: ");
                String bankName = scanner.nextLine();
                return new BankTransferPayment(accountNumber, bankName);
                
            case "crypto":
            case "cryptocurrency":
                System.out.print("Enter wallet address: ");
                String walletAddress = scanner.nextLine();
                System.out.print("Enter crypto type (BTC, ETH, etc.): ");
                String cryptoType = scanner.nextLine();
                return new CryptocurrencyPayment(walletAddress, cryptoType);
                
            default:
                throw new IllegalArgumentException("Unsupported payment method: " + type);
        }
    }
}

// Test class
class PaymentStrategyTest {
    public static void main(String[] args) {
        PaymentProcessor processor = new PaymentProcessor();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Payment Strategy Demo ===\n");
        
        // Demo with different payment methods
        double amount = 100.50;
        
        try {
            // Credit Card Payment
            PaymentStrategy creditCard = PaymentStrategyFactory.createPaymentStrategy("credit", scanner);
            processor.setPaymentStrategy(creditCard);
            processor.processPayment(amount);
            
            // PayPal Payment
            PaymentStrategy paypal = PaymentStrategyFactory.createPaymentStrategy("paypal", scanner);
            processor.setPaymentStrategy(paypal);
            processor.processPayment(amount);
            
            // Bank Transfer Payment
            PaymentStrategy bankTransfer = PaymentStrategyFactory.createPaymentStrategy("bank", scanner);
            processor.setPaymentStrategy(bankTransfer);
            processor.processPayment(amount);
            
            // Cryptocurrency Payment
            PaymentStrategy crypto = PaymentStrategyFactory.createPaymentStrategy("crypto", scanner);
            processor.setPaymentStrategy(crypto);
            processor.processPayment(amount);
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
