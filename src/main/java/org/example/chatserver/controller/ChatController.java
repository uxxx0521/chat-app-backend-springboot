package org.example.chatserver.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.example.chatserver.model.User;
import org.example.chatserver.service.AuthService;
import org.example.chatserver.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/chatapi/api")
public class ChatController {

    @Autowired
    private AuthService authService;

    private final ChatService chatService;
    @Autowired
    public ChatController(ChatService chatService){    //constructor INJECTS service
        this.chatService = chatService;
    }


    @PostMapping("/auth")
    public ResponseEntity<Map<String, String>> auth(@RequestBody User user, HttpServletResponse response) {
        Map<String, String> result = new HashMap<>();

        try {
            String token = authService.login(user.getUsername(), user.getPassword());

            //  Use Spring's ResponseCookie
            ResponseCookie cookie = ResponseCookie.from("token", token)
                    .httpOnly(true)
                    .secure(true) // Use HTTPS in production
                    .path("/")
                    .maxAge(Duration.ofHours(1))
                    .sameSite("Lax")
                    .build();

            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            result.put("message", "Login successful");
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            result.put("message", "Invalid username or password");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user){
        String result = authService.register(user.getUsername(), user.getPassword(), user.getEmail(), user.getNickname());
        if (result.equals("success")) {
            return ResponseEntity.ok("Registration successful");
        } else if(result.equals("Enter different username")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already taken");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
        }

    }


}
