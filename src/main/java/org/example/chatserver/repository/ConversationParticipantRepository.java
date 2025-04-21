package org.example.chatserver.repository;

import org.example.chatserver.model.ConversationParticipant;
import org.example.chatserver.model.ConversationParticipantId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConversationParticipantRepository extends JpaRepository<ConversationParticipant, ConversationParticipantId> {

    @Query("SELECT cp.conversationId FROM ConversationParticipant cp WHERE cp.userId = :userId")
    List<Long> findConversationIdsByUserId(@Param("userId") Long userId);

    @Query("SELECT cp.userId FROM ConversationParticipant cp WHERE cp.conversationId = :conversationId")
    List<Long> findUserIdsByConversationId(@Param("conversationId") Long conversationId);
}
