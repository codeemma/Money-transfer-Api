package com.codeemma.revolut.account;

import java.math.BigDecimal;

public class Account {
    private Long id;
    private String AccountNumber;
    private String accountName;
    private BigDecimal accountBalance;

    public Account() {
    }

    public Account(String accountNumber, String accountName, BigDecimal accountBalance) {
        AccountNumber = accountNumber;
        this.accountName = accountName;
        this.accountBalance = accountBalance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        AccountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }
}
