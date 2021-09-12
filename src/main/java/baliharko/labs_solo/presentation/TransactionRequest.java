package baliharko.labs_solo.presentation;

import lombok.Data;

@Data
public class TransactionRequest {
    private String holder;
    private double amount;
}
