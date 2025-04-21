package org.example.chatserver.controller;

import lombok.RequiredArgsConstructor;
import org.example.chatserver.config.CustomUserDetails;
import org.example.chatserver.model.Message;
import org.example.chatserver.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chatapi/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    // 🔹 Get messages for a specific private conversation
    @GetMapping("/private/{conversationId}")
    public ResponseEntity<List<Message>> getMessagesByConversationId(
            @PathVariable Long conversationId,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Message> messages = messageService.getMessagesForConversation(conversationId);
        return ResponseEntity.ok(messages);
    }
    // 🔹 Public messages (conversationId = 1)
    @GetMapping("/public")
    public ResponseEntity<List<Message>> getPublicMessages() {
        Long publicConversationId = 1L;
        List<Message> messages = messageService.getMessagesForConversation(publicConversationId);
        return ResponseEntity.ok(messages);
    }

}