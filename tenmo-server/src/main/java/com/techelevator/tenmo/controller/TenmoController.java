package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferStatus;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TenmoController {

    private final AccountDao accountDao;
    private final TransferDao transferDao;
    private final TransferStatusDao transferStatusDao;
    private final TransferTypeDao transferTypeDao;
    private final UserDao userDao;


    public TenmoController(AccountDao accountDao, TransferDao transferDao, TransferStatusDao transferStatusDao, TransferTypeDao transferTypeDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
        this.transferStatusDao = transferStatusDao;
        this.transferTypeDao = transferTypeDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = "/account", method = RequestMethod.GET)
    public List<Account> listAccounts() {
        List<Account> accountList = new ArrayList<>();
        return accountDao.getAccountList();
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

    @RequestMapping(path = "/user/{userId}/account", method = RequestMethod.GET)
    public Account getUserId(@PathVariable int userId) {
        Account account = accountDao.getAccountByUserId(userId);
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

    @RequestMapping (path = "/account", method = RequestMethod.PUT)
    public Account updateAccount(@RequestBody Account account){
        Account updatedAccount = null;
        try{
            updatedAccount = accountDao.updateAccount(account);
        }catch (DaoException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found.");
        }
        return updatedAccount;
    }

    @RequestMapping(path = "/account/{userId}/transfer", method = RequestMethod.GET)
    public List<Transfer> transferList(@PathVariable int userId) {
        List<Transfer> transferList = transferDao.transferByUserId(userId);

        if(transferList == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found");
        }
        else {
            return transferList;
        }
    }

    @RequestMapping(path = "/user/{userId}/transfer/pending", method = RequestMethod.GET)
    public List<Transfer> pendingList(@PathVariable int userId) {
        List<Transfer> transferList = transferDao.transferByUserId(userId);
        List<Transfer> pendingList = new ArrayList<>();

        if(transferList == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found");
        }
        else {
            for (int i = 0; i < transferList.size(); i++){
                if (transferList.get(i).getTransferStatusId() == 1){
                    pendingList.add(transferList.get(i));
                }
            }
            return pendingList;
        }
    }

    @RequestMapping(path = "/account/{id}/transfer/{transferId}", method = RequestMethod.GET)
    public Transfer transferById(@PathVariable int id, @PathVariable int transferId) {
        Transfer transfer = transferDao.getTransferByTransferId(transferId);

        if(transfer == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found.");
        }
        else {
            return transfer;
        }
    }

    @RequestMapping(path = "/account/transfer", method = RequestMethod.POST)
    public Transfer createTransfer(@Valid @RequestBody Transfer transfer){
        Transfer newTransfer = null;
        Account accountFrom = accountDao.getAccountById(transfer.getAccountFrom());
        Account accountTo = accountDao.getAccountById(transfer.getAccountTo());
        BigDecimal zeroBalance = new BigDecimal(0);

        if(accountFrom.getAccountId() == accountTo.getAccountId()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot request/send funds to self");
          //  transfer.setTransferStatusId(3);
        }
        else if(transfer.getAmount().compareTo(zeroBalance) == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot request/send a zero ammount of funds");
          //  transfer.setTransferStatusId(3);
        }
        else if(transfer.getAmount().compareTo(zeroBalance) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot request/send a negative ammount of funds");
          //  transfer.setTransferStatusId(3);
        }

        if (transfer.getTransferTypeId() == 1){
            newTransfer = transferDao.transferFundsRequest(transfer);
        } else if (transfer.getTransferTypeId() == 2) {
            newTransfer = transferDao.transferFundsSend(transfer);
        }




        return newTransfer;
    }

    @RequestMapping(path = "/user/transfer", method = RequestMethod.PUT)
    public Transfer updateTransfer(@RequestBody Transfer transfer) {
        Transfer updatedTransfer = null;

        try {
            updatedTransfer = transferDao.updateTransfer(transfer);
        }
        catch(DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transfer not found");
        }

        Account accountFrom = accountDao.getAccountById(updatedTransfer.getAccountFrom());
        Account accountTo = accountDao.getAccountById(updatedTransfer.getAccountTo());

        if(updatedTransfer.getTransferStatusId() == 2) {
            accountFrom.setBalance(accountFrom.getBalance().subtract(updatedTransfer.getAmount()));
            accountTo.setBalance(accountTo.getBalance().add(updatedTransfer.getAmount()));

            updateAccount(accountFrom);
            updateAccount(accountTo);
        }
        return updatedTransfer;

    }

    @RequestMapping (path = "/users/list", method = RequestMethod.GET)
    public List<Integer> getUserIdList(){
        List<User> userList = userDao.getUsers();
        List<Integer> userIdList = new ArrayList<>();
        if (userList == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Users not found.");
        }
        for (User user : userList){
            userIdList.add(user.getId());

        }
        return userIdList;
    }


}
