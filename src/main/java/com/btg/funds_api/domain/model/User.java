package com.btg.funds_api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private String id;
    private String name;
    private String email;
    private String phone;
    private Double balance;
    private NotificationPreference notificationPreference;
    private String password;
    private Role role;
}
