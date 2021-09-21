package baliharko.labs_solo.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    void deposit() {
        Account acc = new Account("test1", 0.0);

        acc.deposit(20.0);

        assertEquals(20.0, acc.getBalance());
    }

    @Test
    void withdraw() {
        Account acc = new Account("test2", 44.0);

        acc.withdraw(20.0);

        assertEquals(24.0, acc.getBalance());
    }
}