package baliharko.labs_solo.application.impl;

import baliharko.labs_solo.domain.Account;
import baliharko.labs_solo.persistence.AccountRepository;
import baliharko.labs_solo.risk.RiskAssessmentClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    private AccountRepository mockRepository = mock(AccountRepository.class);
    private RiskAssessmentClient riskAssessmentClient = mock(RiskAssessmentClient.class);
    private AccountServiceImpl service;

    @BeforeEach
    void pre() {
        service = new AccountServiceImpl(mockRepository, riskAssessmentClient);
    }

    @Test
    void openAccountTest() {
        when(mockRepository.existsAccountByHolder("newCustomer")).thenReturn(false);
        when(mockRepository.save(any())).thenReturn(new Account("newCustomer", 0.0));
        when(riskAssessmentClient.passingCreditCheck(any())).thenReturn(true);

        Account acc = service.openAccount("newCustomer");

        assertEquals("newCustomer", acc.getHolder());
        verify(mockRepository, times(1)).existsAccountByHolder(any());
        verify(mockRepository, times(1)).save(any());
    }

    @Test
    void getAccountByHolderShouldThrowIllegalArgumentException() {
        String holder = "nonExistingCustomer";

        when(mockRepository.findAccountByHolder(holder)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> service.getAccountByHolder(holder));
    }

    @Test
    void getAccountByHolder() {
        String holder = "existingCustomer";

        when(mockRepository.findAccountByHolder(holder)).thenReturn(new Account(holder, 0.0));

        Account acc = service.getAccountByHolder(holder);
        assertEquals(holder, acc.getHolder());

        verify(mockRepository).findAccountByHolder(holder);
    }
}