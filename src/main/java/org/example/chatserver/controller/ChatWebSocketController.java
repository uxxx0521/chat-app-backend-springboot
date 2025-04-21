package org.example.chatserver.controller;

import org.example.chatserver.model.Message;
import org.example.chatserver.repository.UserRepository;
import org.example.chatserver.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserRepository userRepository;

    //  Listen for messages from WebSocket clients
    @MessageMapping("/chat.send")
    public void sendMessage(Message message) {
        System.out.println("Incoming: " + message.getSenderId() + ": " + message.getMessage());

        // Save to DB
        Message saved = chatService.saveMessage(message);

        // Force-fetch the sender so frontend receives sender object (not null)
        saved.setSender(userRepository.findById(saved.getSenderId()).orElse(null));
        System.out.println("Sender: " + saved.getSender());

        // Destination based on conversation
        String destination = message.getConversationId() == 1
                ? "/topic/public"
                : "/topic/private." + message.getConversationId();

        // Send the saved message (includes sender info, timestamp, etc.)
        messagingTemplate.convertAndSend(destination, saved);
    }

}
