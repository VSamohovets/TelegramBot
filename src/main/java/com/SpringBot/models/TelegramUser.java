package com.SpringBot.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "telegram_users")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TelegramUser {

    @Id
    @Column(name = "user_id", unique = true, nullable = false)
    private Long userId;

    @Column(name = "is_bot")
    private Boolean isBot;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username")
    private String userName;

    @Column(name = "language_code")
    private String languageCode;

    @Column(name = "is_premium")
    private Boolean isPremium;

    @Column(name = "added_to_attachment_menu")
    private Boolean addedToAttachmentMenu;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_interaction_at", nullable = false)
    private LocalDateTime lastInteractionAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.lastInteractionAt = LocalDateTime.now();
    }

    /**
     * Updates the last interaction time to now
     */
    public void updateLastInteraction() {
        this.lastInteractionAt = LocalDateTime.now();
    }

    /**
     * Convenience method to check if user is a new user
     * @return true if this is the first time we've seen this user
     */
    public boolean isNewUser() {
        return lastInteractionAt == null || lastInteractionAt.equals(createdAt);
    }
}



//CREATE TABLE public.telegram_users (
//        user_id bigint NOT NULL,
//        is_bot boolean NOT NULL,
//        first_name varchar NOT NULL,
//        last_name varchar NULL,
//        username varchar NULL,
//        language_code varchar NULL,
//        is_premium varchar NULL,
//        added_to_attachment_menu varchar NULL,
//        created_at varchar NOT NULL,
//        last_interaction_at varchar NOT NULL,
//        CONSTRAINT telegram_users_pk PRIMARY KEY (id)
//);
//
//        -- Column comments
//
//COMMENT ON COLUMN public.telegram_users.user_id IS 'Unique identifier for this user or bot. This number may have more than 32 significant bits and some programming languages may have difficulty/silent defects in interpreting it. But it has at most 52 significant bits, so a 64-bit integer or double-precision float type are safe for storing this identifier.';
//COMMENT ON COLUMN public.telegram_users.is_bot IS 'True, if this user is a bot';
//COMMENT ON COLUMN public.telegram_users.first_name IS 'User''s or bot''s first name';
//COMMENT ON COLUMN public.telegram_users.last_name IS 'Optional. User''s or bot''s last name';
//COMMENT ON COLUMN public.telegram_users.username IS 'Optional. User''s or bot''s username';
//COMMENT ON COLUMN public.telegram_users.language_code IS 'Optional. IETF language tag of the user''s language';
//COMMENT ON COLUMN public.telegram_users.is_premium IS 'Optional. True, if this user is a Telegram Premium user';
//COMMENT ON COLUMN public.telegram_users.added_to_attachment_menu IS 'Optional. True, if this user added the bot to the attachment menu';
//COMMENT ON COLUMN public.telegram_users.created_at IS 'create at timestamp';
//COMMENT ON COLUMN public.telegram_users.last_interaction_at IS 'last interaction timestamp';
