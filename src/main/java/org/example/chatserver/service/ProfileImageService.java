package org.example.chatserver.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.example.chatserver.model.User;
import org.example.chatserver.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;


import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileImageService {

    private final AmazonS3 amazonS3;
    private final UserRepository userRepository;

    private final String bucketName = "portfolio-chat-app-profile-images";

    public String uploadProfileImage(MultipartFile file, Long userId, String username) {
        try {
            String key = buildKey(username, file.getOriginalFilename());
            ObjectMetadata metadata = buildMetadata(file);

            System.out.println("🚀 [ProfileImageService] Starting upload to S3: " + key);
            amazonS3.putObject(bucketName, key, file.getInputStream(), metadata);
            System.out.println("✅ [ProfileImageService] Upload successful, updating user...");

            String imageUrl = buildImageUrl(key);

            User user = getUserById(userId);
            user.setProfileImageUrl(imageUrl);
            userRepository.save(user);

            return imageUrl;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    public String updateProfileImage(MultipartFile file, Long userId, String username) {
        try {
            User user = getUserById(userId);

            // Delete old image if exists
            if (user.getProfileImageUrl() != null) {
                String oldKey = extractKeyFromUrl(user.getProfileImageUrl());
                amazonS3.deleteObject(bucketName, oldKey);
            }

            // Upload new image
            String key = buildKey(username, file.getOriginalFilename());
            ObjectMetadata metadata = buildMetadata(file);

            amazonS3.putObject(bucketName, key, file.getInputStream(), metadata);

            String imageUrl = buildImageUrl(key);
            user.setProfileImageUrl(imageUrl);
            userRepository.save(user);

            return imageUrl;
        } catch (IOException e) {
            throw new RuntimeException("Failed to update image", e);
        }
    }

    public void deleteProfileImage(Long userId) {
        User user = getUserById(userId);

        if (user.getProfileImageUrl() != null) {
            String key = extractKeyFromUrl(user.getProfileImageUrl());
            amazonS3.deleteObject(bucketName, key);

            user.setProfileImageUrl(null);
            userRepository.save(user);
        }
    }


    // Helpers
    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private ObjectMetadata buildMetadata(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        return metadata;
    }

    private String buildKey(String username, String filename) {
        return "profile-pics/" + username + "/" + UUID.randomUUID() + "-" + filename;
    }

    private String buildImageUrl(String key) {
        return "https://" + bucketName + ".s3.amazonaws.com/" + key;
    }

    private String extractKeyFromUrl(String imageUrl) {
        return imageUrl.substring(imageUrl.indexOf(".amazonaws.com/") + ".amazonaws.com/".length());
    }
}