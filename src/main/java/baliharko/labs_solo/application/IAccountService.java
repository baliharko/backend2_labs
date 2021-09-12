package baliharko.labs_solo.application;

import baliharko.labs_solo.domain.Account;

import java.util.List;

public interface IAccountService {
   List<Account> getAllAccounts();
   Account getAccountByHolder(final String holder);
   Account deposit(final String holder, final double amount);
   Account withdraw(final String holder, final double amount);
   Account openAccount(final String holder);
}
