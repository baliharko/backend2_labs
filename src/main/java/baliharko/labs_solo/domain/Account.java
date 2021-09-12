package baliharko.labs_solo.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Table(name = "account")
@RequiredArgsConstructor
public class Account {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "holder")
    private String holder;

    @Column(name = "balance")
    private double balance;

    public Account(String holder, double balance) {
        this.holder = holder;
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public String getHolder() {
        return holder;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount < 0)
            throw new IllegalArgumentException("Can't deposit negative amount");

        this.balance += amount;
    }

    public void withdraw(double amount) {
        if (amount < 0)
            throw new IllegalArgumentException("Can't withdraw negative amounts");

        if (amount > this.balance)
            throw new IllegalArgumentException("Insufficient funds");

        this.balance -= amount;
    }

}
