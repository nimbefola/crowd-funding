package com.pentspace.crowdfundingservice.clients;

import com.pentspace.crowdfundingservice.dto.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "AccountServiceClient", url = "http://localhost:30301/account")
public interface AccountServiceClient {

    @GetMapping(path = "/{id}", produces = "application/json")
    Account getAccount(@PathVariable("id") String account);
    @PutMapping(path = "/balance/debit", produces = "text/plain")
    String debitBalance(@RequestParam("id") String id, @RequestParam("amount") String amount);
    @PutMapping(path = "/balance/credit", produces = "text/plain")
    String creditBalance(@RequestParam("id") String id, @RequestParam("amount") String amount);

}
