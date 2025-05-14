package org.example.chatserver.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.example.chatserver.config.CustomUserDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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

        Map<String, Object> userData = new HashMap<>();
        userData.put("id", userDetails.getId());
        userData.put("username", userDetails.getUsername());
        userData.put("email", userDetails.getEmail());
        userData.put("nickname", userDetails.getNickname());
        userData.put("profileImageUrl", userDetails.getProfileImageUrl());

        return ResponseEntity.ok(Map.of("user", userData));
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(true) // match how it was originally set
                .path("/")
                .maxAge(0) // delete immediately
                .sameSite("None")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok("Logged out");
    }

}