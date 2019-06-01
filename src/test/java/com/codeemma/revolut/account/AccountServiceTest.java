package com.codeemma.revolut.account;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class AccountServiceTest {

    private AccountService accountService;

    private AccountDao accountDao;

    @Before
    public void setUp() throws Exception {
        accountService = new AccountServiceImpl();
        accountDao = new AccountDao();
    }

    @Test
    public void transferFund() {
        String originatingAccountNumber = "12345";
        String destinationAccountNumber = "67890";
        accountDao.create(originatingAccountNumber,"holder 2", BigDecimal.valueOf(5000.00));
        accountDao.create(destinationAccountNumber,"holder 1", BigDecimal.valueOf(1000.00));

        accountService.transferFund(originatingAccountNumber, destinationAccountNumber, BigDecimal.valueOf(2000.00));

        Account originator = accountDao.get(originatingAccountNumber);
        Account destination = accountDao.get(destinationAccountNumber);

        assertEquals(3000, originator.getAccountBalance().longValue());
        assertEquals(3000, destination.getAccountBalance().longValue());
    }


    @Test(expected = UnsupportedOperationException.class)
    public void transferFundShouldThrowExceptionWhenInsufficientFund() {
        String originatingAccountNumber = "2345";
        String destinationAccountNumber = "23456";
        accountDao.create(originatingAccountNumber,"holder 2", BigDecimal.valueOf(5000.00));
        accountDao.create(destinationAccountNumber,"holder 1", BigDecimal.valueOf(1000.00));


        accountService.transferFund(originatingAccountNumber, destinationAccountNumber, BigDecimal.valueOf(5001.00));

    }
}