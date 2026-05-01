package com.bhuvi.personalTrackerAPI.entity;

import java.time.LocalDateTime;

import lombok.*;

@Data
@NoArgsConstructor
public class User {

    private Integer userId;
    private String userName;
    private String password;
    private String mailId;
    private Character isActive = 'Y';
    private LocalDateTime createdDate;
    private LocalDateTime lud;

    // Custom constructor for creating users
    public User(String userName, String password, String mailId) {
        this.userName = userName;
        this.password = password;
        this.mailId = mailId;
        this.createdDate = LocalDateTime.now();
        this.lud = LocalDateTime.now();
    }
}




