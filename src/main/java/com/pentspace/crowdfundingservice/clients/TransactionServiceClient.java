package com.pentspace.crowdfundingservice.clients;

import com.pentspace.crowdfundingservice.dto.Transaction;
import org.json.simple.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "TransactionServiceClient", url = "http://localhost:30305/transaction")
public interface TransactionServiceClient {

    @PostMapping( consumes = "application/json", produces = "application/json")
    Transaction create(@RequestBody Transaction transaction);

}
