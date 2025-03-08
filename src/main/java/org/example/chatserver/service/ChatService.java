package org.example.chatserver.service;

import org.example.chatserver.model.ChatMessage;
import org.example.chatserver.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class ChatService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public ChatMessage saveMessage(String sender, String content, String timestamp) {
        return chatMessageRepository.save(new ChatMessage(sender, content, timestamp));
    }

    public List<ChatMessage> getAllMessages() {
        return chatMessageRepository.findAll();
    }
}
