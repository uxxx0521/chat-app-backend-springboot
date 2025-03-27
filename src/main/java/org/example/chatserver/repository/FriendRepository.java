package org.example.chatserver.repository;


import org.example.chatserver.model.Friend;
import org.example.chatserver.model.FriendId;
import org.example.chatserver.model.FriendStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, FriendId> {

    @Query("SELECT CASE " +
            "WHEN f.userId = :userId THEN f.friendId " +
            "ELSE f.userId END " +
            "FROM Friend f " +
            "WHERE (f.userId = :userId OR f.friendId = :userId) " +
            "AND f.status = 'ACCEPTED'")
    List<Long> findAcceptedFriendIds(@Param("userId") Long userId);

    // 🔹 Check if a friend request or friendship already exists
    boolean existsByUserIdAndFriendId(Long userId, Long friendId);

    // 🔹 Find specific request or friendship by both user IDs
    Optional<Friend> findByUserIdAndFriendId(Long userId, Long friendId);

    // 🔹 Get all incoming friend requests for a user
    List<Friend> findByFriendIdAndStatus(Long friendId, FriendStatus status);
}
