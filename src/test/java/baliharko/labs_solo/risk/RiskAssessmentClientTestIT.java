package baliharko.labs_solo.risk;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RiskAssessmentClientTestIT {

    private WireMockServer wireMockServer;

    @BeforeEach
    void pre() {
        wireMockServer = new WireMockServer(9090);
        wireMockServer.start();
    }

    @AfterEach
    void post() {
        wireMockServer.stop();
    }

    @Test
    void canFetchRiskAssessmentTest() {

        final String expectedResponseBody = "{\"pass\": true}";

        wireMockServer.stubFor(get(urlEqualTo("/risk/test"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON.toString())
                        .withBody(expectedResponseBody)));

        final RiskAssessmentClient risk = new RiskAssessmentClient(new RestTemplate(), wireMockServer.baseUrl());
        final boolean passingCreditCheck = risk.passingCreditCheck("test");
        assertTrue(passingCreditCheck);
    }
}