package baliharko.labs_solo.config;

import baliharko.labs_solo.risk.RiskAssessmentClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
public class ApplicationConfiguration {

    @Value("${risk-url}")
    private String riskAssessmentUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RiskAssessmentClient riskAssessmentClient(RestTemplate restTemplate) {
        return new RiskAssessmentClient(restTemplate, riskAssessmentUrl);
    }
}
