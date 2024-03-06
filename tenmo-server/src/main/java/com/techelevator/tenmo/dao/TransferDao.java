package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.security.Principal;
import java.util.List;

public interface TransferDao {

    public List<Transfer> transferByAccountId(int accountId);
    public Transfer getTransferByTransferId(int transferId);
}
