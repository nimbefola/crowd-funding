package com.pentspace.crowdfundingservice.clients;

import com.pentspace.crowdfundingservice.dto.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "AccountServiceClient", url = "http://localhost:30301/account")
public interface AccountServiceClient {

    @GetMapping(path = "/{id}", produces = "application/json")
    Account getAccount(@PathVariable("id") String account);
    @PutMapping(path = "balance/update",consumes = "application/json", produces = "application/json")
    List<Account> updateBalances(@RequestBody List<Account> accounts);
}
