package org.example.chatserver.controller;

import org.example.chatserver.config.CustomUserDetails;
import org.example.chatserver.model.User;
import org.example.chatserver.service.FriendService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chatapi/api/friends")
public class FriendController {

    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }


    @GetMapping("/load")
    public ResponseEntity<List<User>> getFriends(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("❌ No authenticated user found");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof CustomUserDetails)) {
            System.out.println("❌ Principal is not instance of CustomUserDetails");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        CustomUserDetails userDetails = (CustomUserDetails) principal;
        Long userId = userDetails.getId(); // Or getUsername() if your DB queries by that

        System.out.println("✅ Authenticated user ID: " + userId);

        List<User> friends = friendService.getAcceptedFriends(userId);
        return ResponseEntity.ok(friends);
    }
    @PostMapping("/add")
    public ResponseEntity<?> sendFriendRequest(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam String username) {

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }

        return friendService.sendFriendRequest(currentUser.getId(), username);
    }

    // 🔹 View incoming friend requests (to current user)
    @GetMapping("/requests")
    public ResponseEntity<List<User>> getIncomingRequests(@AuthenticationPrincipal CustomUserDetails currentUser) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<User> pendingRequests = friendService.getPendingRequests(currentUser.getId());
        return ResponseEntity.ok(pendingRequests);
    }

    // 🔹 Accept a friend request from someone (by ID)
    @PostMapping("/accept")
    public ResponseEntity<String> acceptRequest(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam Long friendId) {

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }

        return friendService.acceptFriendRequest(currentUser.getId(), friendId);
    }
    @PostMapping("/reject")
    public ResponseEntity<String> rejectFriendRequest(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam Long friendId) {

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }

        return friendService.rejectFriendRequest(currentUser.getId(), friendId);
    }

}