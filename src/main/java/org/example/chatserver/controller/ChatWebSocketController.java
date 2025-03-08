package org.example.chatserver.controller;

import org.example.chatserver.model.ChatMessage;
import org.example.chatserver.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    @Autowired
    private ChatService chatService;

    //  Listen for messages from WebSocket clients
    @MessageMapping("/chat.send")
    @SendTo("/topic/public") //  Broadcast to all clients subscribed to "/topic/public"
    public ChatMessage sendMessage(ChatMessage chatMessage) {
        //  Log the received message
        System.out.println("Received message from: " + chatMessage.getSender() + " - " + chatMessage.getContent());

        //  Save the message in MySQL
        chatService.saveMessage(chatMessage.getSender(), chatMessage.getContent(), chatMessage.getTimestamp());

        //  Return the message so it gets sent to all subscribers
        return chatMessage;
    }
}
