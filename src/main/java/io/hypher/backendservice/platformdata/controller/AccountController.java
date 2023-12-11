package io.hypher.backendservice.platformdata.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import io.hypher.backendservice.platformdata.model.Account;
import io.hypher.backendservice.platformdata.service.AccountService;
import io.hypher.backendservice.platformdata.utillity.error.ResourceNotFoundException;
import io.hypher.backendservice.platformdata.utillity.error.WrongBodyException;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/v1")
public class AccountController {

    @Autowired
    AccountService accountService;

    @PostMapping("/accounts")
    public Optional<Account> create(@RequestBody Account account) {
        return accountService.save(account);        
    }
    
    
    @GetMapping("/accounts")
    public List<Account> getAll() {
        return accountService.findAll();   
    }

    @GetMapping("/accounts/{id}")
    public Optional<Account> getById(@PathVariable(value = "id") UUID accountId) {
        
        return accountService.findById(accountId);
    }


    @PutMapping("/accounts/{id}")
    public ResponseEntity<Account> update(@PathVariable(value = "id") UUID accountId, @RequestBody Account updatedAccount) throws ResourceNotFoundException, WrongBodyException{

        // security: only update if account exists
        accountService.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        // security: only save if body and path variable are the same
        if (updatedAccount.getAccountId().toString().equals(accountId.toString())) {
            Account updatedAccountFromDatabase = accountService.save(updatedAccount)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
            return ResponseEntity.ok(updatedAccountFromDatabase);

        } else {
            throw new WrongBodyException("Your Body conflicts with the ID provided in the request.");
        }
    }


    @DeleteMapping("/accounts/{id}")
    public String delete(@PathVariable(value = "id") UUID accountId) throws ResourceNotFoundException{

        Account accountToDelete = accountService.findById(accountId).orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        return accountService.delete(accountToDelete);
    }
    
    
    
}
