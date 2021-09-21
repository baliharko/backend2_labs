package baliharko.labs_solo.presentation;

import baliharko.labs_solo.domain.Account;
import baliharko.labs_solo.persistence.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
class AccountControllerTestIT {

    private static MySQLContainer mySQLContainer =
            new MySQLContainer("mysql:latest").withPassword("password");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository repository;

    @BeforeEach
    void before() {
        repository.deleteAll();
    }

    @Test
    void getAllAccountsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/all")).andExpect(status().is2xxSuccessful());
    }

    @Test
    void getAccountByHolderTest() throws Exception {
        String accHolder = "Leif";
        repository.save(new Account(accHolder, 20.0));

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/" + accHolder))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("holder").value("Leif"));
    }

    @Test
    void openAccountTest() throws Exception {

        String newUserJson = "{\"holder\": \"LeifTest\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/openaccount")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("holder").value("LeifTest"));
    }

    @Test
    void deposit() throws Exception {

        String transactionRequestJson =
                "{\n" +
                "\"holder\": \"Testsson1\",\n" +
                "\"amount\": 22.0\n" +
                "}";

        repository.save(new Account("Testsson1", 0.0));

        mockMvc.perform(MockMvcRequestBuilders.post("/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(transactionRequestJson))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("holder").value("Testsson1"))
                .andExpect(jsonPath("balance").value(22.0));


        mockMvc.perform(MockMvcRequestBuilders.post("/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionRequestJson))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("holder").value("Testsson1"))
                .andExpect(jsonPath("balance").value(44.0));
    }

    @Test
    void withdraw() throws Exception {

        String transactionRequestJson =
                "{\n" +
                        "\"holder\": \"Testsson1\",\n" +
                        "\"amount\": 22.0\n" +
                        "}";

        repository.save(new Account("Testsson1", 100.0));

        mockMvc.perform(MockMvcRequestBuilders.post("/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(transactionRequestJson))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("holder").value("Testsson1"))
                .andExpect(jsonPath("balance").value(78.0));

        mockMvc.perform(MockMvcRequestBuilders.post("/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionRequestJson))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("holder").value("Testsson1"))
                .andExpect(jsonPath("balance").value(56.0));
    }
}