package com.codeemma.revolut.account;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class AccountServiceTest {

    private AccountService accountService;

    private AccountDao accountDao;
    @Rule public ExpectedException expectedException = ExpectedException.none();

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

        Account result = accountService.transferFund(originatingAccountNumber, destinationAccountNumber, BigDecimal.valueOf(2000.00));

        Account destination = accountDao.get(destinationAccountNumber);

        assertEquals(3000, result.getAccountBalance().longValue());
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

    @Test(expected = NoSuchElementException.class)
    public void transferFundShouldThrowExceptionAccountNotValid() {
        String originatingAccountNumber = "zzzzzz";
        String destinationAccountNumber = "23456";

        accountService.transferFund(originatingAccountNumber, destinationAccountNumber, BigDecimal.valueOf(5001.00));

    }
}