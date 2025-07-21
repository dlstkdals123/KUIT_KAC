package org.example.kuit_kac.domain.home.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HomeWeightRequest {
    private long userId;
    private double weight;
}
