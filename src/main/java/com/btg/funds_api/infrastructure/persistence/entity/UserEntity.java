package com.btg.funds_api.infrastructure.persistence.entity;

import com.btg.funds_api.domain.model.NotificationPreference;
import com.btg.funds_api.domain.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    private String id;
    private String name;
    private String email;
    private String phone;
    private Double balance;
    private NotificationPreference notificationPreference;
    private String password;
    private Role role;
}
