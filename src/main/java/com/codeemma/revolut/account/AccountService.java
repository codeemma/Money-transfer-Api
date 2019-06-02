package com.codeemma.revolut.account;

import java.math.BigDecimal;

public interface AccountService {
    Account transferFund(String originatingAccount, String destinationAccount, BigDecimal amount);

}
