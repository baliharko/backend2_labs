package baliharko.labs_solo.persistence;

import baliharko.labs_solo.domain.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = AccountRepositoryTestIT.ApplicationTestContextInitializer.class)
class AccountRepositoryTestIT {

    public static class ApplicationTestContextInitializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            String host = mySQLContainer.getJdbcUrl();
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext
                    , "spring.datasource.url=" + host
                    , "spring.flyway.url=" + host);
        }
    }

    @Container
    private static final MySQLContainer mySQLContainer = new MySQLContainer("mysql:latest").withPassword("password");

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void before() {
        List<Account> accList = Arrays.asList(
                        new Account("testHolder1", 13.0),
                        new Account("testHolder2", 9_000_000.0),
                        new Account("testHolder3", 140.0),
                        new Account("testHolder4", 2021.0),
                        new Account("testHolder5", 500.0),
                        new Account("testHolder6", 0.0)
        );

        accountRepository.saveAll(accList);
    }

    @AfterEach
    void after() {
        accountRepository.deleteAll();
    }

    @Test
    void findAllTest() {
        int expected = 6;
        int actual = accountRepository.findAll().size();
        assertEquals(expected, actual);
    }

    @Test
    void findAccountByHolderTestShouldReturnHolder() {
        Account acc = accountRepository.findAccountByHolder("testHolder2");
        assertEquals("testHolder2", acc.getHolder());
        assertEquals(9_000_000.0, acc.getBalance());
    }

    @Test
    void existsAccountByHolderTestShouldReturnTrue() {
        boolean actual = accountRepository.existsAccountByHolder("testHolder3");
        assertEquals(true, actual);

        actual = accountRepository.existsAccountByHolder("nonExistingUser");
        assertEquals(false, actual);
    }
}