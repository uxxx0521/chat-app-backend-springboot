package org.example.chatserver.service;

import org.example.chatserver.model.ChatMessage;
import org.example.chatserver.repository.ChatMessageRepository;
import org.example.chatserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {


    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void saveMessage(String sender, String content, String timestamp) {
        chatMessageRepository.save(new ChatMessage(sender, content, timestamp));
    }

    public List<ChatMessage> getAllMessages() {
        return chatMessageRepository.findAll();
    }

}
