package com.btg.funds_api.infrastructure.controller.request;

import com.btg.funds_api.domain.model.NotificationPreference;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String name;
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    private String email;
    @NotBlank(message = "El teléfono es obligatorio")
    private String phone;
    @NotNull(message = "La preferencia de notificación es obligatoria")
    private NotificationPreference notificationPreference;
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
