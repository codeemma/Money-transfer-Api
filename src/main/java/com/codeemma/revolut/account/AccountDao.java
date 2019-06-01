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
        return null;
    }

    public Account get(String accountNumber) {
        return null;
    }

    public Account update(Account account) {

        return null;
    }

    public List<Account> listAccounts() {
        return accountDataStore.values()
                .stream()
                .sorted(Comparator.comparing((u) -> u.getAccountNumber()))
                .collect(Collectors.toList());
    }

}
