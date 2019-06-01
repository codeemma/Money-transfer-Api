package com.codeemma.revolut.account;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

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

    @Test(expected = UnsupportedOperationException.class)
    public void createAnExistingAccountShouldThrowException() {
        Account result = accountDao.create(dumAccountNumber,dumAccountName, BigDecimal.ZERO);

        accountDao.create(dumAccountNumber,dumAccountName, BigDecimal.valueOf(1000));
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
        Account account = accountDao.create(dumAccountNumber,dumAccountName, BigDecimal.ZERO);
        String newName = "Emmanuel";
        BigDecimal newBalance = BigDecimal.valueOf(2000.00);

        //edit account
        account.setAccountName(newName);
        account.setAccountBalance(newBalance);
        Account updatedAccount = accountDao.update(account);

        assertEquals(dumAccountNumber, updatedAccount.getAccountNumber());
        assertEquals(newName, updatedAccount.getAccountName());
        assertEquals(newBalance, updatedAccount.getAccountBalance());
    }

    @Test
    public void listAccounts() {
        createNewAccounts(3);

        var acountList = accountDao.listAccounts();

        assertEquals(3, acountList.size());
    }

    private void createNewAccounts(int i) {
        for(var j = 1; j<=i; j++){
            accountDao.create("123456" + j, "account name" + j, BigDecimal.valueOf(100));
        }
    }
}
