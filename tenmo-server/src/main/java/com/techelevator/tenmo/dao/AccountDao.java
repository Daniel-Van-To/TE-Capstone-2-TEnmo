package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    public BigDecimal getBalance(int id);

    public Account getAccountById(int accountId);

    public List<Account> getAccountList();

    public Account updateAccount(Account account);

    public Account getAccountByUserId(int userId);
}
