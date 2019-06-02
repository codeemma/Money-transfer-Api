package com.codeemma.revolut.account;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.Lock;
import java.util.logging.Logger;

public class AccountServiceImpl implements AccountService {

    Logger logger = Logger.getLogger(getClass().getName());

    private AccountDao accountDao = new AccountDao();


    @Override
    public synchronized void transferFund(String originatingAccount, String destinationAccount, BigDecimal amount) {
        logger.info(String.format("initiating transfer from %s to %s, amount = %s", originatingAccount,destinationAccount,amount));
        Account originator = getNotNullAccount(originatingAccount);
        Account destination = getNotNullAccount(destinationAccount);

        checkBalanceSufficiency(originator, amount );

        originator.setAccountBalance(originator.getAccountBalance().subtract(amount));
        logger.info(amount +" deducted from account: "+ originator);
        destination.setAccountBalance(destination.getAccountBalance().add(amount));
    }

    private Account getNotNullAccount(String accountNumber) {
        Account account = accountDao.get(accountNumber);
        if (account == null){
            throw new NoSuchElementException("account NOT FOUND: no account with account number <"+ accountNumber + ">");
        }

        return account;
    }

    private void checkBalanceSufficiency(Account originator, BigDecimal amount) {
        if (originator.getAccountBalance().compareTo(amount) < 0){
            logger.severe("insufficient balance in account " + originator.getAccountNumber());
            throw  new UnsupportedOperationException("insufficient balance in account " + originator.getAccountNumber());
        }
    }
}
