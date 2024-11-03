package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.entity.Account;
import com.example.exception.AccountException;
import com.example.repository.AccountRepository;

@Service
public class AccountService{
    @Autowired
    private AccountRepository accountRepository;
    
    public Account createAccount(Account acc){
        if(accountRepository.findByUsername(acc.getUsername()) != null){
            throw new AccountException("Username already exists.");
        }
        if(acc.getUsername().isEmpty()){
            throw new AccountException("Username is blank.");
        }
        if(acc.getPassword().length() < 4){
            throw new AccountException("Password is less than 4 characters.");
        }
        return accountRepository.save(acc);
    }

    public Account login(Account acc){
        Account temp = accountRepository.findByUsername(acc.getUsername());
        if(temp == null){
            throw new AccountException("Username does not exist.");
        }
        if(temp.getPassword().equals(acc.getPassword())){
            return temp;
        }else{
            throw new AccountException("Password is incorrect.");
        }
    }
}
