package org.example.chatserver.controller;

import org.example.chatserver.model.ChatMessage;
import org.example.chatserver.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.example.chatserver.model.User;
import java.util.Map;
import java.util.HashMap;

import java.util.List;


@RestController
@RequestMapping("/chatapi")
public class ChatController {

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
    public ResponseEntity<Map<String, String>> auth(@RequestBody User user){
        String result = chatService.handleLogin(user.getUsername(), user.getPassword());
        Map<String, String> response = new HashMap<>();
        if (result.equals("success")) {
            response.put("message", "Login successful");
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user){
        String result = chatService.handleRegister(user.getUsername(), user.getPassword(), user.getEmail(), user.getNickname());
        if (result.equals("success")) {
            return ResponseEntity.ok("Registration successful");
        } else if(result.equals("Enter different username")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already taken");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong");
        }

    }

}
