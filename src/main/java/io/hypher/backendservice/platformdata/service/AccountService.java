package io.hypher.backendservice.platformdata.service;

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


    public Optional<Account> findById(UUID accountId){
        return accountRepository.findById(accountId);
    }


}
