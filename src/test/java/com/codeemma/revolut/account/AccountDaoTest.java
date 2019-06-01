package com.codeemma.revolut.account;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class AccountDaoTest {
    private AccountDao accountDao;


    @Before
    public void setUp() throws Exception {
        accountDao = new AccountDao();
    }

    @Test
    public void create() {
        String dumAccountNumber = "11111";
        String dumAccountName = "Emmanuel Olayinka";

        Account result = accountDao.create(dumAccountNumber,dumAccountName, BigDecimal.ZERO);

        assertEquals(dumAccountNumber, result.getAccountNumber());
        assertEquals(dumAccountName, result.getAccountName());
        assertEquals(BigDecimal.ZERO, result.getAccountBalance());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void createAnExistingAccountShouldThrowException() {
        String dumAccountNumber = "222222";
        String dumAccountName = "Emmanuel Olayinka";

        accountDao.create(dumAccountNumber,dumAccountName, BigDecimal.ZERO);

        accountDao.create(dumAccountNumber,dumAccountName, BigDecimal.valueOf(1000));
    }

    @Test
    public void get() {
        String dumAccountNumber = "33333";
        String dumAccountName = "Emmanuel Olayinka";
        Account testAccount = accountDao.create(dumAccountNumber,dumAccountName, BigDecimal.ZERO);

        Account result = accountDao.get(testAccount.getAccountNumber());

        assertEquals(dumAccountNumber, result.getAccountNumber());
        assertEquals(dumAccountName, result.getAccountName());
        assertEquals(BigDecimal.ZERO, result.getAccountBalance());
    }

    @Test
    public void getShouldReturnNullWhenAccountIsNotInStore() {

        Account result = accountDao.get("zzzzzzz");

        assertNull(result);
    }

    @Test
    public void update() {
        String accNumber = "555555";
        String dumAccountName = "Emmanuel Olayinka";

        Account account = accountDao.create(accNumber, dumAccountName, BigDecimal.ZERO);
        String newName = "Emmanuel";
        BigDecimal newBalance = BigDecimal.valueOf(2000.00);

        //edit account
        account.setAccountName(newName);
        account.setAccountBalance(newBalance);
        Account updatedAccount = accountDao.update(account);

        assertEquals(accNumber, updatedAccount.getAccountNumber());
        assertEquals(newName, updatedAccount.getAccountName());
        assertEquals(newBalance, updatedAccount.getAccountBalance());
    }

    @Test
    public void listAccounts() {
        createNewAccounts(3);

        var accountList = accountDao.listAccounts();

        assertTrue(accountList.size() >= 3); //for concurrent run of test, it shouldn't be less than 3
    }

    private void createNewAccounts(int i) {
        for(var j = 1; j<=i; j++){
            accountDao.create("123456" + j, "account name" + j, BigDecimal.valueOf(100));
        }
    }
}
