package com.codeemma.revolut.seeder;

import com.codeemma.revolut.account.AccountDao;

import java.math.BigDecimal;

public class AccountSeeder {

    public static void seedData(){
        AccountDao accountDao = new AccountDao();
        accountDao.create("11112222","Verijus Majid", BigDecimal.valueOf(40000.00));
        accountDao.create("22221111","Verijus Majid", BigDecimal.valueOf(40000.00));
        accountDao.create("22223333","Verijus Majid", BigDecimal.valueOf(40000.00));
        accountDao.create("33332222","Verijus Majid", BigDecimal.valueOf(40000.00));
    }
}
