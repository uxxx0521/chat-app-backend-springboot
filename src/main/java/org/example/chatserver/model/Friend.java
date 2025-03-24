package org.example.chatserver.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "friends")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(FriendId.class)
public class Friend {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "friend_id")
    private Long friendId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_id", insertable = false, updatable = false)
    private User friend;

    @Enumerated(EnumType.STRING)
    private FriendStatus status = FriendStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;
}

