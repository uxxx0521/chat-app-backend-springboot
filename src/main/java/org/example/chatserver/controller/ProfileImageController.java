package org.example.chatserver.controller;

import lombok.RequiredArgsConstructor;
import org.example.chatserver.config.CustomUserDetails;
import org.example.chatserver.service.ProfileImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/chatapi/api/profile-image")
@RequiredArgsConstructor
public class ProfileImageController {

    private final ProfileImageService profileImageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadProfilePicture(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        if (currentUser == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String imageUrl = profileImageService.uploadProfileImage(file, currentUser.getId(), currentUser.getUsername());
        return ResponseEntity.ok(imageUrl);
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateProfilePicture(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        if (currentUser == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        String imageUrl = profileImageService.updateProfileImage(file, currentUser.getId(), currentUser.getUsername());
        return ResponseEntity.ok(imageUrl);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteProfilePicture(
            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {
        if (currentUser == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        profileImageService.deleteProfileImage(currentUser.getId());
        return ResponseEntity.ok("Profile picture deleted successfully.");
    }
}
