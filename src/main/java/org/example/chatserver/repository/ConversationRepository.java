package org.example.chatserver.repository;

import org.example.chatserver.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    // No custom methods are required for basic create/findById
}
