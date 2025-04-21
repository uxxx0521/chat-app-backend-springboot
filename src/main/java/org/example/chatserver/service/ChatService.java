package org.example.chatserver.service;

import org.example.chatserver.model.Message;
import org.example.chatserver.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {

    @Autowired
    private MessageRepository messageRepository;

    public Message saveMessage(Message message) {
        // You can set timestamps or defaults here if needed
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
}
