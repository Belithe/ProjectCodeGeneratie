package nl.inholland.Models;

public class Account {
    private int balance;
    private int dayLimit;
    private String IBAN;
    private int minimumLimit;
    private int userId;

    public Account(int balance, int dayLimit, String IBAN, int minimumLimit, int userId) {
        this.balance = balance;
        this.dayLimit = dayLimit;
        this.IBAN = IBAN;
        this.minimumLimit = minimumLimit;
        this.userId = userId;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(int dayLimit) {
        this.dayLimit = dayLimit;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public int getMinimumLimit() {
        return minimumLimit;
    }

    public void setMinimumLimit(int minimumLimit) {
        this.minimumLimit = minimumLimit;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

}
