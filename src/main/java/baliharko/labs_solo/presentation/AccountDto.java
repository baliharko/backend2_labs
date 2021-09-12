package baliharko.labs_solo.presentation;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AccountDto {
    private final long id;
    private final String holder;
    private final double balance;
}
