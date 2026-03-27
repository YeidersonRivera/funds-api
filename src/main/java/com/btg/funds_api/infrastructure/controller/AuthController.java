package com.btg.funds_api.infrastructure.controller;

import com.btg.funds_api.application.usecase.CreateUserUseCase;
import com.btg.funds_api.application.usecase.GetUserByEmailUseCase;
import com.btg.funds_api.domain.model.Role;
import com.btg.funds_api.domain.model.User;
import com.btg.funds_api.infrastructure.controller.request.LoginRequest;
import com.btg.funds_api.infrastructure.controller.request.UserRequest;
import com.btg.funds_api.infrastructure.controller.response.AuthResponse;
import com.btg.funds_api.infrastructure.mapper.UserMapper;
import com.btg.funds_api.infrastructure.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final CreateUserUseCase createUserUseCase;
    private final GetUserByEmailUseCase getUserByEmailUseCase;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserRequest request) {
        User user = userMapper.requestToDomain(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_USER);

        User created = createUserUseCase.execute(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(AuthResponse.builder()
                .token(jwtService.generateToken(created))
                .id(created.getId())
                .name(created.getName())
                .role(created.getRole().name())
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = getUserByEmailUseCase.execute(request.getEmail());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(AuthResponse.builder()
                .token(jwtService.generateToken(user))
                .id(user.getId())
                .name(user.getName())
                .role(user.getRole().name())
                .build());
    }
}
