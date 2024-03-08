package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.util.BasicLogger;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

public class AccountService {

    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public AccountService(String url) {
        this.baseUrl = url;
    }

    public Account getAccount(int userId){
        Account account = null;

        try{
            account = restTemplate.getForObject(baseUrl + "/user/" + userId + "/account", Account.class );

        }catch (RestClientResponseException e){
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        }catch (ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }catch (NullPointerException e){
            BasicLogger.log(e.getMessage());
        }
        return account;
    }

    public List<Integer> getUserIdList(){
        List<Integer> userIdList = null;

        try{
            userIdList = restTemplate.getForObject(baseUrl + "users/list", List.class);
        }catch (RestClientResponseException e){
            BasicLogger.log(e.getRawStatusCode() + " : " + e.getStatusText());
        }catch (ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }catch (NullPointerException e){
            BasicLogger.log(e.getMessage());
        }

        return userIdList;
    }


}
