package io.hypher.backendservice.platformdata.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import io.hypher.backendservice.platformdata.errors.ResourceNotFoundException;
import io.hypher.backendservice.platformdata.model.Account;
import io.hypher.backendservice.platformdata.service.AccountService;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/v1")
public class AccountController {

    @Autowired
    AccountService accountService;

    @PostMapping("/accounts")
    public Account addAccount(@RequestBody Account account) {
        return accountService.save(account);        
    }
    
    
    @GetMapping("/accounts")
    public List<Account> getAllAccounts() {
        return accountService.findAll();   
    }

    @GetMapping("/accounts/{id}")
    public Optional<Account> getAccount(@PathVariable(value = "id") UUID accountId) {
        
        return accountService.findById(accountId);
    }


    @PutMapping("/accounts/{id}")
    public Account updateAccount(@PathVariable(value = "id") UUID accountId, @RequestBody Account updatedAccount) throws ResourceNotFoundException{
        
        Optional<Account> accountData = accountService.findById(accountId);

        UUID idOfAccountToUpdate = accountData.map(account -> account.getAccountId())        
            .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        // update account via service
        return accountService.save(updatedAccount);
    }


    @DeleteMapping("/accounts/{id}")
    public String deleteAccount(@PathVariable(value = "id") UUID accountId){

        Optional<Account> accountData = accountService.findById(accountId);
        UUID idOfDeletedAccount = accountData.map(account -> account.getAccountId())        
            .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        return "Deleted account with ID: " + idOfDeletedAccount;
    }
    
    
    
}
