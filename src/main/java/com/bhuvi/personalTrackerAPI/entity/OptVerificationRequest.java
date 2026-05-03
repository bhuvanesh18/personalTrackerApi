package com.bhuvi.personalTrackerAPI.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OptVerificationRequest {

    private String mailId;
    private String otp;

}
