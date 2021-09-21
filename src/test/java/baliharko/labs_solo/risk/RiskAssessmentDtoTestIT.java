package baliharko.labs_solo.risk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RiskAssessmentDtoTestIT {

    @Test
    void canSerializeTest() throws JsonProcessingException {
        String json = "{\"pass\": \"false\"}";

        RiskAssessmentDto testDto = new ObjectMapper().readValue(json, RiskAssessmentDto.class);

        assertEquals(false, testDto.isPass());
    }
}