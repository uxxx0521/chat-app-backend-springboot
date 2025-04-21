package org.example.chatserver.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;

    @Column(name = "conversation_id")
    private Long conversationId;

    @ManyToOne
    @JoinColumn(name = "conversation_id", insertable = false, updatable = false) //define foreign key.
    private Conversation conversation;               //It maps foreign key to target entity's primary key by default.

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "sender_id", nullable = true)
    private Long senderId;

    @ManyToOne(optional = true)
    @JoinColumn(name = "sender_id", insertable = false, updatable = false)
    private User sender;

    @Column(columnDefinition = "TEXT")
    private String message;

    @CreationTimestamp
    @Column(name = "sent_at")
    private Timestamp sentAt;
}