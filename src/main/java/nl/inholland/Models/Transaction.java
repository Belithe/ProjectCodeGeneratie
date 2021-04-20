package nl.inholland.Models;

import java.util.Date;

public class Transaction {

    private int transactionID;
    private String ibanSender;
    private Date transactionDate;
    private float transferAmount;
    private String ibanReceiver;


    public Transaction(int transactionID, String ibanSender, Date transactionDate, float transferAmount, String ibanReceiver) {
        this.transactionID = transactionID;
        this.ibanSender = ibanSender;
        this.transactionDate = transactionDate;
        this.transferAmount = transferAmount;
        this.ibanReceiver = ibanReceiver;
    }


    public int getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    public String getIbanSender() {
        return ibanSender;
    }

    public void setIbanSender(String ibanSender) {
        this.ibanSender = ibanSender;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public float getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(float transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getIbanReceiver() {
        return ibanReceiver;
    }

    public void setIbanReceiver(String ibanReceiver) {
        this.ibanReceiver = ibanReceiver;
    }

}
