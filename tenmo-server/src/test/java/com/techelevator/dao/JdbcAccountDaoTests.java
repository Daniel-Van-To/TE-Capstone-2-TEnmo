package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccountDaoTests extends BaseDaoTests{

//    private static final Account ACCOUNT_1 = new Account(2001,1001, new BigDecimal(1000));
//    private static final Account ACCOUNT_2 = new Account(2002,1002, new BigDecimal(1000));
//    private static final Account ACCOUNT_3 = new Account(2003,1003, new BigDecimal(1000));

    private JdbcAccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

//    @Test
//    public void getBalance_using_id_should_return_balance() {
//        //Arrange
//        BigDecimal balance = sut.getBalance(2001);
//        assert
//
//        //Act
//
//        //Assert
//    }




}
