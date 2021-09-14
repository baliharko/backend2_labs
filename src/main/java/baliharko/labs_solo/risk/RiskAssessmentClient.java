package baliharko.labs_solo.risk;

import baliharko.labs_solo.domain.IRiskAssessment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
public class RiskAssessmentClient implements IRiskAssessment {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    @Override
    public boolean passingCreditCheck(String holder) {

        ResponseEntity<RiskAssessmentDto> response = restTemplate
                .getForEntity(baseUrl + "/risk/" + holder, RiskAssessmentDto.class);

        log.info("Got status {} for name {}", response.getStatusCodeValue(), holder);

        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Got body {} for {}", response.getBody(), holder);
            return response.getBody().isPass();
        }

        throw new RuntimeException("Error fetching risk assessment: " + response.getStatusCodeValue()
                + " - " + response.getStatusCode().getReasonPhrase());
    }
}
