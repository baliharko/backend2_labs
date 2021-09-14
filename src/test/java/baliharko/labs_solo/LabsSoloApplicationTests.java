package baliharko.labs_solo;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.MimeTypeUtils;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.images.RemoteDockerImage;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Testcontainers
@ContextConfiguration(initializers = LabsSoloApplicationTests.ApplicationTestContextInitializer.class)
@AutoConfigureMockMvc
class LabsSoloApplicationTests {

    public static class ApplicationTestContextInitializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            String host = db.getJdbcUrl();
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext
                    , "spring.datasource.url=" + host
                    , "spring.flyway.url=" + host
                    , "risk-url=http://localhost:9091");
        }
    }

    @Container
    private static final MySQLContainer db = new MySQLContainer("mysql:latest").withPassword("password");

    private WireMockServer wireMockServer;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void before() {
        wireMockServer = new WireMockServer(9091);
        wireMockServer.start();
    }

    @BeforeEach
    void setupWireMockServer() {
        wireMockServer.stubFor(get(urlEqualTo("/risk/test"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON.toString())
                        .withBody("{\"pass\": true}")));
    }

    @AfterEach
    void after() {
        wireMockServer.stop();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void openAccountShouldReturn200() throws Exception {

        final String openAccountRequestBody = "{\"holder\": \"test\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/openaccount")
                .header("content-type", "application/json")
                .content(openAccountRequestBody))
                .andExpect(status().is2xxSuccessful());
    }



}
