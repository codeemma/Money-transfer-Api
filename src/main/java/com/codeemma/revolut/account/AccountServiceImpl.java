package com.codeemma.revolut.account;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;
import java.util.logging.Logger;

public class AccountServiceImpl implements AccountService {

    Logger logger = Logger.getLogger(getClass().getName());

    private AccountDao accountDao = new AccountDao();


    @Override
    public synchronized void transferFund(String originatingAccount, String destinationAccount, BigDecimal amount) {
        logger.info(String.format("initiating transfer from %s to %s, amount = %s", originatingAccount,destinationAccount,amount));
        Account originator = accountDao.get(originatingAccount);
        Account destination = accountDao.get(destinationAccount);

        if (originator.getAccountBalance().compareTo(amount) < 0){
            throw  new UnsupportedOperationException("insufficient balance in account " + originatingAccount);
        }

        originator.setAccountBalance(originator.getAccountBalance().subtract(amount));
        logger.info(amount +" deducted from account: "+ originator);
        destination.setAccountBalance(destination.getAccountBalance().add(amount));
    }
}
