package org.example.chatserver.service;

import lombok.RequiredArgsConstructor;
import org.example.chatserver.model.Conversation;
import org.example.chatserver.model.ConversationParticipant;
import org.example.chatserver.repository.ConversationParticipantRepository;
import org.example.chatserver.repository.ConversationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationParticipantRepository participantRepository;

    public Conversation getOrCreatePrivateConversation(Long userId, Long friendId) {

        // Find all conversations this user is in
        List<Long> userConvoIds = participantRepository.findConversationIdsByUserId(userId);

        // Find any conversations where both are participants and it's not a group
        for (Long convoId : userConvoIds) {
            List<Long> participantIds = participantRepository.findUserIdsByConversationId(convoId);
            if (participantIds.size() == 2 && participantIds.contains(friendId)) {
                return conversationRepository.findById(convoId).orElseThrow();
            }
        }

        // Otherwise, create new conversation
        Conversation convo = Conversation.builder()
                .name("private-" + UUID.randomUUID())
                .isGroup(false)
                .isPublic(false)
                .build();

        Conversation saved = conversationRepository.save(convo);

        // Add both participants
        participantRepository.save(new ConversationParticipant(saved.getId(), userId));
        participantRepository.save(new ConversationParticipant(saved.getId(), friendId));

        return saved;
    }
    public Conversation getConversationById(Long id) {
        return conversationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));
    }
}
