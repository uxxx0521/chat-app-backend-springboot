package org.example.chatserver.controller;

import org.example.chatserver.model.ChatMessage;
import org.example.chatserver.model.User;
import org.example.chatserver.service.AuthService;
import org.example.chatserver.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/chatapi")
public class ChatController {

    @Autowired
    private AuthService authService;

    private final ChatService chatService;
    @Autowired
    public ChatController(ChatService chatService){    //constructor INJECTS service
        this.chatService = chatService;
    }


    //  Get all chat messages
    @GetMapping("/messages")
    public List<ChatMessage> getMessages() {
        return chatService.getAllMessages();
    }

    @PostMapping("/auth")
    public ResponseEntity<Map<String, String>> auth(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();

        try {
            String token = authService.login(user.getUsername(), user.getPassword());
            response.put("message", "Login successful");
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
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
