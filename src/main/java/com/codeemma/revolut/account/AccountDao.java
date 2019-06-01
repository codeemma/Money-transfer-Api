package com.codeemma.revolut.account;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class AccountDao {
    private static ConcurrentMap<String, Account> accountDataStore;

    public AccountDao() {
        accountDataStore = new ConcurrentHashMap<>();
    }

    public Account create(String accountNumber, String accountName, BigDecimal initialAmount) {
        Account account = new Account(accountNumber,accountName,initialAmount);

        if (accountDataStore.putIfAbsent(accountNumber, account) != null){
            throw new UnsupportedOperationException("account with account number already exist");//this could return custom exception
        }

        return account;
    }

    public Account get(String accountNumber) {
        return accountDataStore.get(accountNumber);
    }

    public Account update(Account account) {
        accountDataStore.put(account.getAccountNumber(), account);

        return account;
    }

    public List<Account> listAccounts() {
        return accountDataStore.values()
                .stream()
                .sorted(Comparator.comparing((u) -> u.getAccountNumber()))
                .collect(Collectors.toList());
    }

}
