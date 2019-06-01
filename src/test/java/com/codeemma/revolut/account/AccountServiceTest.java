package com.codeemma.revolut.account;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class AccountServiceTest {

    private AccountService accountService;

    private Account destinationAccount;
    private Account originatingAccount;

    private String originatingAccountNumber = "12345";
    private String destinationAccountNumber = "67890";

    private AccountDao accountDao;

    @Before
    public void setUp() throws Exception {
        accountService = new AccountServiceImpl();
        accountDao = new AccountDao();

        originatingAccount = accountDao.create(originatingAccountNumber,"holder 2", BigDecimal.valueOf(5000.00));
        destinationAccount = accountDao.create(destinationAccountNumber,"holder 1", BigDecimal.valueOf(1000.00));

    }

    @Test
    public void transferFund() {
        accountService.transferFund(originatingAccountNumber, destinationAccountNumber, BigDecimal.valueOf(2000.00));

        Account originator = accountDao.get(originatingAccountNumber);
        Account destination = accountDao.get(destinationAccountNumber);

        assertEquals(3000, originator.getAccountBalance().longValue());
        assertEquals(3000, destination.getAccountBalance().longValue());
    }
}