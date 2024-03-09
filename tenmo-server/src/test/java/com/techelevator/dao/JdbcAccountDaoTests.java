package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccountDaoTests extends BaseDaoTests{

    private static final Account ACCOUNT_1 = new Account(2001,1001, new BigDecimal(1000));
    private static final Account ACCOUNT_2 = new Account(2002,1002, new BigDecimal(1000));
    private static final Account ACCOUNT_3 = new Account(2003,1003, new BigDecimal(1000));

    private JdbcAccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void getBalance_using_id_should_return_balance() {
        //Arrange
        BigDecimal balance = sut.getBalance(1001);
        Assert.assertEquals("Balance amount did not match",ACCOUNT_1.getBalance(), balance);
    }

    @Test
    public void getAccountById_should_return_correct_account() {
        Account account = sut.getAccountById(2001);
        assertAccountsMatch(ACCOUNT_1,account);
        Account account2 = sut.getAccountById(2002);
        assertAccountsMatch(ACCOUNT_2,account);
        Account account3 = sut.getAccountById(2003);
        assertAccountsMatch(ACCOUNT_3,account);
    }



    private void assertAccountsMatch(Account expected, Account actual) {
        Assert.assertEquals("Did not match Account Ids",expected.getAccountId(),actual.getAccountId());
        Assert.assertEquals("Did not match User Ids",expected.getUserId(),actual.getUserId());
        Assert.assertEquals("Did not match Balance amount",expected.getBalance(),actual.getBalance());
    }



}
