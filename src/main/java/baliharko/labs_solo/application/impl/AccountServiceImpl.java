package baliharko.labs_solo.application.impl;

import baliharko.labs_solo.application.IAccountService;
import baliharko.labs_solo.domain.Account;
import baliharko.labs_solo.persistence.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private final AccountRepository repository;


    @Override
    public List<Account> getAllAccounts() {
        return repository.findAll();
    }

    @Override
    public Account getAccountByHolder(String holder) {
        Account acc;
        if ((acc = repository.findAccountByHolder(holder)) == null)
            throw new IllegalArgumentException("No account with holder name {" + holder + "} found.");

        return acc;
    }

    @Override
    public Account deposit(String holder, double amount) {
        Account acc = getAccountByHolder(holder);
        acc.deposit(amount);
        return acc;
    }

    @Override
    public Account withdraw(String holder, double amount) {
        Account acc = getAccountByHolder(holder);
        acc.withdraw(amount);
        return acc;
    }

    @Override
    public Account openAccount(String holder) {
        if (repository.existsAccountByHolder(holder))
            throw new IllegalArgumentException("Account with holder name {" + holder + "} already exists.");

        return repository.save(new Account(holder, 0.0));
    }
}