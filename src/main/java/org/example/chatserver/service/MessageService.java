package org.example.chatserver.service;

import lombok.RequiredArgsConstructor;
import org.example.chatserver.model.Message;
import org.example.chatserver.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public List<Message> getMessagesForConversation(Long conversationId) {
        return messageRepository.findByConversationIdOrderBySentAtAsc(conversationId);
    }
}