package com.codeemma.revolut.account;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class AccountDaoTest {
    private AccountDao accountDao;
    private String dumAccountNumber = "123568812";
    private String dumAccountName = "Emmanuel Olayinka";

    @Before
    public void setUp() throws Exception {
        accountDao = new AccountDao();
    }

    @Test
    public void create() {
        Account result = accountDao.create(dumAccountNumber,dumAccountName, BigDecimal.ZERO);

        assertEquals(dumAccountNumber, result.getAccountNumber());
        assertEquals(dumAccountName, result.getAccountName());
        assertEquals(BigDecimal.ZERO, result.getAccountBalance());
    }

    @Test
    public void get() {
        Account account = accountDao.create(dumAccountNumber,dumAccountName, BigDecimal.ZERO);

        Account result = accountDao.get(account.getAccountNumber());

        assertEquals(dumAccountNumber, result.getAccountNumber());
        assertEquals(dumAccountName, result.getAccountName());
        assertEquals(BigDecimal.ZERO, result.getAccountBalance());
    }

    @Test
    public void update() {
    }

    @Test
    public void listAccounts() {
    }
}