package org.example.kuit_kac.global.util.dev;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@org.springframework.boot.context.properties.ConfigurationProperties(prefix = "debug.onboarding.autofill")
public class DevAutofillProperties {
    private boolean enabled = true;
    private org.example.kuit_kac.domain.user.model.GenderType defaultGender = org.example.kuit_kac.domain.user.model.GenderType.MALE;
    private Integer defaultAge = 25;
    private Integer defaultHeight = 170;
    private Double defaultTargetWeight = 65.0;
}
