package org.example.chatserver.controller;

import org.example.chatserver.model.ChatMessage;
import org.example.chatserver.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@RestController
@RequestMapping("/chatapi")
public class ChatController {

    private final ChatService chatService;


    @Autowired
    public ChatController(ChatService chatService){
        this.chatService = chatService;

    }


    //  Get all chat messages
    @GetMapping("/messages")
    public List<ChatMessage> getMessages() {
        return chatService.getAllMessages();
    }



}
