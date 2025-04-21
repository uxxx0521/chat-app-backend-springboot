package org.example.chatserver.controller;

import lombok.RequiredArgsConstructor;
import org.example.chatserver.config.CustomUserDetails;
import org.example.chatserver.model.Conversation;
import org.example.chatserver.service.ConversationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chatapi/api/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    @GetMapping("/private")
    public ResponseEntity<Conversation> getOrCreatePrivateConversation(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam Long friendId) {

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Conversation convo = conversationService.getOrCreatePrivateConversation(currentUser.getId(), friendId);
        return ResponseEntity.ok(convo);
    }
    @GetMapping("/public")
    public ResponseEntity<Conversation> getPublicConversation() {
        // Assuming public chatroom always has id = 1
        Conversation publicRoom = conversationService.getConversationById(1L);
        return ResponseEntity.ok(publicRoom);
    }


}
