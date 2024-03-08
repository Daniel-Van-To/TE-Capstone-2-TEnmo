package com.techelevator.tenmo.services;

import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {

    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public TransferService(String url) {
        this.baseUrl = url;
    }

    public Transfer[] listTransfers(int userId) {
        Transfer[] transfers = null;

        try {
            transfers = restTemplate.getForObject(baseUrl + "/account/" + userId + "/transfer", Transfer[].class);
        }
        catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public Transfer[] listPendingRequests(int userId){
        Transfer[] requests = null;


        try{
            requests = restTemplate.getForObject(baseUrl + "/user/" + userId + "/transfer/pending", Transfer[].class);
        }
        catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        Transfer[] relevantRequests = new Transfer[requests.length];
        int i = 0;
        for(Transfer transfer: requests){
            if(transfer.getAccountFrom() - 1000 != userId){
                relevantRequests[i++] = transfer;
            }
        }
        return relevantRequests;
    }

    public Transfer getTransferById(int transferId, Transfer[] requests) {
        Transfer transfer = null;

        for(Transfer transfers: requests) {
            if(transferId == transfers.getTransferId()) {
                transfer = transfers;
            }
        }

        return transfer;

    }

    public boolean updatedTransfer(Transfer transfer) {
        boolean success = false;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer,headers);

        try {
            restTemplate.put(baseUrl + "/user/transfer", entity );

            success = true;
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;

    }

}
