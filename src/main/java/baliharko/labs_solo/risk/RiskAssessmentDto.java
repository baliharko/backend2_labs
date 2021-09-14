package baliharko.labs_solo.risk;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RiskAssessmentDto {

    private final boolean pass;

    @JsonCreator
    public RiskAssessmentDto(@JsonProperty("pass") final boolean pass) {
        this.pass = pass;
    }
}
