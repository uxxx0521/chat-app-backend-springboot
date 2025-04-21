package org.example.chatserver.service;


import org.example.chatserver.model.Friend;
import org.example.chatserver.model.FriendStatus;
import org.example.chatserver.model.User;
import org.example.chatserver.repository.FriendRepository;
import org.example.chatserver.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    public FriendService(FriendRepository friendRepository, UserRepository userRepository) {
        this.friendRepository = friendRepository;
        this.userRepository = userRepository;
    }

    public List<User> getAcceptedFriends(Long currentUserId) {
        List<Long> friendIds = friendRepository.findAcceptedFriendIds(currentUserId);
        return userRepository.findAllById(friendIds);
    }
    // 🔹 Send a friend request to another user by username
    public ResponseEntity<?> sendFriendRequest(Long senderId, String recipientUsername) {
        Optional<User> recipientOpt = userRepository.findByUsername(recipientUsername);
        if (recipientOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User recipient = recipientOpt.get();
        Long recipientId = recipient.getId();

        if (senderId.equals(recipientId)) {
            return ResponseEntity.badRequest().body("You cannot send a friend request to yourself.");
        }

        boolean alreadyExists = friendRepository.existsByUserIdAndFriendId(senderId, recipientId);
        if (alreadyExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Friend request already sent or already friends.");
        }

        Friend friendRequest = new Friend();
        friendRequest.setUserId(senderId);
        friendRequest.setFriendId(recipientId);
        friendRequest.setStatus(FriendStatus.PENDING);
        friendRepository.save(friendRequest);

        return ResponseEntity.ok("Friend request sent to " + recipientUsername);
    }
    // 🔹 Get pending incoming friend requests for current user
    public List<User> getPendingRequests(Long userId) {
        List<Friend> requests = friendRepository.findByFriendIdAndStatus(userId, FriendStatus.PENDING);

        return requests.stream()
                .map(Friend::getUser)  // Get sender User entity
                .collect(Collectors.toList());
    }

    // 🔹 Accept a friend request from a user (by ID)
    public ResponseEntity<String> acceptFriendRequest(Long currentUserId, Long requesterId) {
        Optional<Friend> optionalRequest = friendRepository.findByUserIdAndFriendId(requesterId, currentUserId);

        if (optionalRequest.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No pending request from this user.");
        }

        Friend request = optionalRequest.get();
        if (request.getStatus() != FriendStatus.PENDING) {
            return ResponseEntity.badRequest().body("Friend request already handled.");
        }

        // Update original request
        request.setStatus(FriendStatus.ACCEPTED);
        friendRepository.save(request);

        // Optional: add reciprocal row
        if (!friendRepository.existsByUserIdAndFriendId(currentUserId, requesterId)) {
            Friend reciprocal = new Friend();
            reciprocal.setUserId(currentUserId);
            reciprocal.setFriendId(requesterId);
            reciprocal.setStatus(FriendStatus.ACCEPTED);
            friendRepository.save(reciprocal);
        }

        return ResponseEntity.ok("Friend request accepted.");
    }
    // 🔹 Reject a friend request from a user (by ID)
    public ResponseEntity<String> rejectFriendRequest(Long currentUserId, Long requesterId) {
        Optional<Friend> request = friendRepository.findByUserIdAndFriendId(requesterId, currentUserId);

        if (request.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Friend request not found.");
        }

        friendRepository.delete(request.get());
        return ResponseEntity.ok("Friend request rejected.");
    }

}