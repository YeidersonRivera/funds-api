package com.btg.funds_api.infrastructure.controller.response;

import com.btg.funds_api.domain.model.NotificationPreference;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {
    private String id;
    private String name;
    private String email;
    private String phone;
    private Double balance;
    private NotificationPreference notificationPreference;
}
