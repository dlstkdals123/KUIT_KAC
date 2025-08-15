package org.example.kuit_kac.domain.user_information.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.kuit_kac.domain.home.model.DietVelocity;
import org.example.kuit_kac.domain.terms.dto.TermAgreementItem;
import org.example.kuit_kac.domain.user.model.GenderType;
import org.example.kuit_kac.domain.user_information.model.AppetiteType;
import org.example.kuit_kac.domain.user_information.model.EatingOutType;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Table(name = "user")
public class OnboardingRequest {
    private String kakaoId;
    private String nickname;
    private GenderType gender;
    private Integer age;
    private Integer height;
    private Double targetWeight;

    private boolean hasDietExperience;
    private String dietFailReason;
    private AppetiteType appetiteType;
    private String weeklyEatingOutCount;
    private EatingOutType eatingOutType;
    private DietVelocity dietVelocity;

    private List<TermAgreementItem> agreements; // null 가능
}

