package org.example.chatserver.repository;

import org.example.chatserver.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // ✅ Example custom query: Find all messages from a specific sender
    List<ChatMessage> findBySender(String sender);

    // ✅ Example: Find all messages containing specific text
    List<ChatMessage> findByContentContaining(String keyword);
}