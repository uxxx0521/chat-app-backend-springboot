package org.example.chatserver.controller;

import org.example.chatserver.config.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/chatapi/api/auth")
public class AuthController {

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        System.out.println("👋 [/me] Request received (AuthController)");

        if (authentication == null) {
            System.out.println("❌ [/me] Authentication object is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();
        System.out.println("👤 [/me] Principal: " + principal);

        if (!(principal instanceof CustomUserDetails)) {
            System.out.println("❌ [/me] Principal is not an instance of CustomUserDetails");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid authentication principal");
        }

        CustomUserDetails userDetails = (CustomUserDetails) principal;

        System.out.println("✅ [/me] Authenticated user: " + userDetails.getUsername());

        Map<String, Object> userData = Map.of(
                "id", userDetails.getId(),
                "username", userDetails.getUsername(),
                "email", userDetails.getEmail(),
                "nickname", userDetails.getNickname()
        );

        return ResponseEntity.ok(Map.of("user", userData));
    }

}