package baliharko.labs_solo.application.impl;

import baliharko.labs_solo.application.IAccountService;
import baliharko.labs_solo.domain.Account;
import baliharko.labs_solo.domain.IRiskAssessment;
import baliharko.labs_solo.persistence.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private final AccountRepository repository;
    private final IRiskAssessment riskClient;

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
        log.info("Depositing {} into account with holder: {}", amount, holder);
        Account acc = getAccountByHolder(holder);
        acc.deposit(amount);
        return acc;
    }

    @Override
    public Account withdraw(String holder, double amount) {
        log.info("Withdrawing {} from account with holder: {}", amount, holder);
        Account acc = getAccountByHolder(holder);
        acc.withdraw(amount);
        return acc;
    }

    @Override
    public Account openAccount(String holder) {

        log.info("Attempting to open account for {}", holder);

        if (repository.existsAccountByHolder(holder))
            throw new IllegalArgumentException("Account with holder name {" + holder + "} already exists.");

        if(!riskClient.passingCreditCheck(holder))
            throw new RuntimeException("Failed credit check for holder: " + holder);

        log.info("Successfully opened account for {}", holder);
        return repository.save(new Account(holder, 0.0));
    }
}
