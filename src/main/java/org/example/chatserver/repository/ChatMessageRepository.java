package org.example.chatserver.repository;


import org.example.chatserver.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {




    //  Example: Find all messages containing specific text
    List<ChatMessage> findByContentContaining(String keyword);


}