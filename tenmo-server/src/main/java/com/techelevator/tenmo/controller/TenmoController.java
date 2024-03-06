package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.TransferStatusDao;
import com.techelevator.tenmo.dao.TransferTypeDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.TransferStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TenmoController {

    private final AccountDao accountDao;
    private final TransferDao transferDao;
    private final TransferStatusDao transferStatusDao;
    private final TransferTypeDao transferTypeDao;

    public TenmoController(AccountDao accountDao, TransferDao transferDao, TransferStatusDao transferStatusDao, TransferTypeDao transferTypeDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
        this.transferStatusDao = transferStatusDao;
        this.transferTypeDao = transferTypeDao;
    }

    @RequestMapping(path = "/account", method = RequestMethod.GET)
    public List<Account> listAccounts() {
        List<Account> accountList = new ArrayList<>();
        accountList = accountDao.getAccountList();
        return accountList;
    }

    @RequestMapping(path = "/account/{accountId}", method = RequestMethod.GET)
    public Account get(@PathVariable int accountId) {
        Account account = accountDao.getAccountById(accountId);
        if(account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
        else {
            return account;
        }
    }

    @RequestMapping(path = "/account/{accountId}/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(@PathVariable int accountId){
        Account account = null;
        BigDecimal balance = null;

        try {
            account = accountDao.getAccountById(accountId);
            balance = account.getBalance();
        }
        catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found.");
        }

        return balance;

    }

}
