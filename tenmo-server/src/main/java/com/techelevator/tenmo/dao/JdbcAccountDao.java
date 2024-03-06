package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getBalance(int id){

        String sql = "Select balance from account where user_id = ?";

        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            return results.getBigDecimal("balance");

        }catch (CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database.", e);
        }
    }

    @Override
    public Account getAccountById(int accountId) {
        Account account = null;
        String sql = "SELECT * FROM account WHERE account_id = ?";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
            if(results.next()) {
                account = mapRowToAccount(results);
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database.", e);
        }

        return account;
    }

    @Override
    public List<Account> getAccountList() {
        List<Account> accountList = null;
        String sql = "SELECT * FROM account";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while(results.next()) {
                accountList.add(mapRowToAccount(results));
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database.", e);
        }
        return accountList;
    }


    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setAccountId(rowSet.getInt("account_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        account.setUserId(rowSet.getInt("user_id"));
        return account;
    }
}
