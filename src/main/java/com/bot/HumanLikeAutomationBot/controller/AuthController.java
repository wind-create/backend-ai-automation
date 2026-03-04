package com.bot.HumanLikeAutomationBot.controller;

import com.bot.HumanLikeAutomationBot.dto.LoginRequest;
import com.bot.HumanLikeAutomationBot.dto.LoginResponse;
import com.bot.HumanLikeAutomationBot.entity.AdminUser;
import com.bot.HumanLikeAutomationBot.repository.AdminUserRepository;
import com.bot.HumanLikeAutomationBot.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AdminUserRepository adminUserRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
    
        AdminUser user = adminUserRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
    
        String token = jwtUtil.generateToken(
                user.getUsername(),
                user.getRole()
        );
    
        return new LoginResponse(
                token,
                user.getUsername(),
                user.getRole().name()
        );
    }
}