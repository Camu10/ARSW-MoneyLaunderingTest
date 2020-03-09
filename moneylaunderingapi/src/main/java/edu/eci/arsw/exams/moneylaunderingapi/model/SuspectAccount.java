package edu.eci.arsw.exams.moneylaunderingapi.model;

public class SuspectAccount {
    public String accountId;
    public int amountOfSmallTransactions;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public SuspectAccount(String a, int b){
        accountId = a;
        amountOfSmallTransactions = b;
    }

    public int getAmountOfSmallTransactions() {
        return amountOfSmallTransactions;
    }

    public void setAmountOfSmallTransactions(int amountOfSmallTransactions) {
        this.amountOfSmallTransactions = amountOfSmallTransactions;
    }
}
