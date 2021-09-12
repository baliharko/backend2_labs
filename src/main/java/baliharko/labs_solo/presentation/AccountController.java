package baliharko.labs_solo.presentation;

import baliharko.labs_solo.application.IAccountService;
import baliharko.labs_solo.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5500", "http://127.0.0.1:5500"}, maxAge = 3600)
public class AccountController {

    private final IAccountService service;

    @GetMapping("/accounts/all")
    public ResponseEntity<List<AccountDto>> getAllAccounts() {
        return ResponseEntity.ok(
                service.getAllAccounts().stream()
                .map(l -> new AccountDto(l.getId(), l.getHolder(), l.getBalance()))
                .collect(Collectors.toList()));
    }

    @GetMapping("/accounts/{holder}")
    public ResponseEntity<AccountDto> getAccountByHolder(@PathVariable("holder") String holder) {
        final Account acc = service.getAccountByHolder(holder);
        return ResponseEntity.ok(new AccountDto(acc.getId(), acc.getHolder(), acc.getBalance()));
    }

    @PostMapping("/openaccount")
    public ResponseEntity<AccountDto> openAccount(@RequestBody OpenAccountRequest request) {
        Account acc = service.openAccount(request.getHolder());
        return ResponseEntity.ok(new AccountDto(acc.getId(), acc.getHolder(), acc.getBalance()));
    }


    @PostMapping("/deposit")
    public ResponseEntity<AccountDto> deposit(@RequestBody TransactionRequest request) {
        final Account acc = service.deposit(request.getHolder(), request.getAmount());
        return ResponseEntity.ok(new AccountDto(acc.getId(), acc.getHolder(), acc.getBalance()));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<AccountDto> withdraw(@RequestBody TransactionRequest request) {
        final Account acc = service.withdraw(request.getHolder(), request.getAmount());
        return ResponseEntity.ok(new AccountDto(acc.getId(), acc.getHolder(), acc.getBalance()));
    }
}
