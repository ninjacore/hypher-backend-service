package io.hypher.backendservice.platformdata.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.hypher.backendservice.platformdata.model.Account;
import io.hypher.backendservice.platformdata.repository.AccountRepository;

@Service
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;

    public Account save(Account account){
        return accountRepository.save(account);
    }

    public Optional<Account> findById(UUID accountId){
        return accountRepository.findById(accountId);
    }

    public List<Account> findAll(){
        return accountRepository.findAll();
    }

    public String delete(Account account){

        boolean accountDeleted;

        try {
            accountRepository.delete(account);
            accountDeleted = true;
        } catch (Exception e) {
            accountDeleted = false;
        }

        return "Account deleted? -> " + accountDeleted;
    }



}
