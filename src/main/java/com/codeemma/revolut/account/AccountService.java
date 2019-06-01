package com.codeemma.revolut.account;

import java.math.BigDecimal;

public interface AccountService {
    void transferFund(String originatingAccount, String destinationAccount, BigDecimal amount);

}
