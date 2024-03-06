package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao( JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Transfer> transferByAccountId(int accountId) {
        List<Transfer> transferList = new ArrayList<>();

        String sql = "SELECT * FROM transfer WHERE account_from = ? OR account_to = ?";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql,accountId, accountId);
            while(results.next()) {
                transferList.add(mapRowToTransfer(results));
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database.", e);
        }

        return transferList;
    }

    @Override
    public Transfer getTransferByTransferId(int transferId) {
        Transfer transfer = null;

        String sql = "SELECT * FROM transfer WHERE transfer_id = ?";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
            if(results.next()) {
                transfer = mapRowToTransfer(results);
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database.", e);
        }

        return transfer;
    }

    private Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferTypeId(rowSet.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rowSet.getInt("transfer_status_id"));
        transfer.setAccountFrom(rowSet.getInt("account_from"));
        transfer.setAccountTo(rowSet.getInt("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        return transfer;
    }
}
