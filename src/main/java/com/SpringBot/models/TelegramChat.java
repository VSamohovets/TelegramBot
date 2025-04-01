package com.SpringBot.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "telegram_chats")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TelegramChat {

    @EmbeddedId
    private TelegramChatId telegramChatId;

    @ManyToOne
    @MapsId("user")
    @JoinColumn(name = "telegram_user_id", insertable = false, updatable = false)
    private TelegramUser user;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_interaction_at", nullable = false)
    private LocalDateTime lastInteractionAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.lastInteractionAt = LocalDateTime.now();
    }

    public void updateLastInteraction() {
        this.lastInteractionAt = LocalDateTime.now();
    }

}


//
//CREATE TABLE public.telegram_chats (
//        chat_id INT , -- Unique chat identifier
//                telegram_user_id BIGINT REFERENCES telegram_users(user_id), -- User who creates the chat
//is_active BOOLEAN NOT NULL, -- Chat status flag
//created_at TIMESTAMP NOT NULL, -- Timestamp when the chat was created
//last_interaction_at TIMESTAMP NOT null, -- Timestamp of the last interaction in the chat
//PRIMARY KEY (chat_id, telegram_user_id));
//
//        -- Adding comments to the database
//COMMENT ON COLUMN telegram_chats.chat_id IS 'Unique identifier for the chat';
//COMMENT ON COLUMN telegram_chats.telegram_user_id IS 'User who created the chat';
//COMMENT ON COLUMN telegram_chats.is_active IS 'Indicates whether the chat is active';
//COMMENT ON COLUMN telegram_chats.created_at IS 'Timestamp when the chat was created';
//COMMENT ON COLUMN telegram_chats.last_interaction_at IS 'Timestamp of the last interaction in the chat';