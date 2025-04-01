package com.SpringBot.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TelegramChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_messages_seq")
    @SequenceGenerator(name = "chat_messages_seq", sequenceName = "chat_messages_seq", allocationSize = 1)
    @Column(name = "message_id", unique = true, nullable = false)
    private Integer messageId;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "chat_id", referencedColumnName = "chat_id"),
            @JoinColumn(name = "telegram_user_id", referencedColumnName = "telegram_user_id")
    })
    private TelegramChat telegramChat;

    @Column(name = "message_thread_id")
    private Integer messageThreadId;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "is_topic_message")
    private Boolean isTopicMessage;

    @Column(name = "is_automatic_forward")
    private Boolean isAutomaticForward;

    @Column(name = "edit_date")
    private LocalDateTime editDate;

    @Column(name = "has_protected_content")
    private Boolean hasProtectedContent;

    @Column(name = "media_group_id")
    private String mediaGroupId;

    @Column(name = "author_signature")
    private String authorSignature;

    @Column(name = "text")
    private String text;

    @Column(name = "has_media_spoiler")
    private Boolean hasMediaSpoiler;

    @Column(name = "new_chat_title")
    private String newChatTitle;

    @Column(name = "delete_chat_photo")
    private Boolean deleteChatPhoto;

    @Column(name = "group_chat_created")
    private Boolean groupChatCreated;

    @Column(name = "supergroup_chat_created")
    private Boolean supergroupChatCreated;

    @Column(name = "channel_chat_created")
    private Boolean channelChatCreated;

    @Column(name = "migrate_to_chat_id")
    private Long migrateToChatId;

    @Column(name = "migrate_from_chat_id")
    private Long migrateFromChatId;

    @Column(name = "connected_website")
    private String connectedWebsite;

    @Column(name = "json_value")
    private String jsonValue;
}



//CREATE TABLE messages (
//        message_id INTEGER PRIMARY KEY, -- Unique message identifier inside this chat
//                telegram_user_id INTEGER REFERENCES telegram_users(id), -- User who sent the message
//message_thread_id INTEGER, -- Unique identifier of a message thread to which the message belongs (supergroups only)
//date INTEGER NOT NULL, -- Date the message was sent in Unix time
//is_topic_message BOOLEAN, -- True if the message is sent to a forum topic
//is_automatic_forward BOOLEAN, -- True if the message is a channel post that was automatically forwarded
//edit_date INTEGER, -- Date the message was last edited in Unix time
//has_protected_content BOOLEAN, -- True if the message can't be forwarded
//media_group_id TEXT, -- Unique identifier of a media message group this message belongs to
//author_signature TEXT, -- Signature of the post author for messages in channels
//text TEXT, -- UTF-8 text of the message (for text messages)
//show_caption_above_media BOOLEAN, -- True if the caption must be shown above the message media
//has_media_spoiler BOOLEAN, -- True if the message media is covered by a spoiler animation
//new_chat_title TEXT, -- New chat title when changed
//delete_chat_photo BOOLEAN, -- True if the chat photo was deleted
//group_chat_created BOOLEAN, -- True if the group has been created
//supergroup_chat_created BOOLEAN, -- True if the supergroup has been created
//channel_chat_created BOOLEAN, -- True if the channel has been created
//migrate_to_chat_id BIGINT, -- The group has been migrated to a supergroup with this identifier
//migrate_from_chat_id BIGINT, -- The supergroup has been migrated from a group with this identifier
//connected_website TEXT -- The domain name of the website on which the user has logged in
//);
//
//        -- Adding comments to the database
//COMMENT ON COLUMN messages.message_id IS 'Unique message identifier inside this chat';
//COMMENT ON COLUMN messages.telegram_user_id IS 'User who sent the message';
//COMMENT ON COLUMN messages.message_thread_id IS 'Unique identifier of a message thread to which the message belongs (supergroups only)';
//COMMENT ON COLUMN messages.date IS 'Date the message was sent in Unix time';
//COMMENT ON COLUMN messages.is_topic_message IS 'True if the message is sent to a forum topic';
//COMMENT ON COLUMN messages.is_automatic_forward IS 'True if the message is a channel post that was automatically forwarded';
//COMMENT ON COLUMN messages.edit_date IS 'Date the message was last edited in Unix time';
//COMMENT ON COLUMN messages.has_protected_content IS 'True if the message cannot be forwarded';
//COMMENT ON COLUMN messages.media_group_id IS 'Unique identifier of a media message group this message belongs to';
//COMMENT ON COLUMN messages.author_signature IS 'Signature of the post author for messages in channels';
//COMMENT ON COLUMN messages.text IS 'UTF-8 text of the message (for text messages)';
//COMMENT ON COLUMN messages.show_caption_above_media IS 'True if the caption must be shown above the message media';
//COMMENT ON COLUMN messages.has_media_spoiler IS 'True if the message media is covered by a spoiler animation';
//COMMENT ON COLUMN messages.new_chat_title IS 'New chat title when changed';
//COMMENT ON COLUMN messages.delete_chat_photo IS 'True if the chat photo was deleted';
//COMMENT ON COLUMN messages.group_chat_created IS 'True if the group has been created';
//COMMENT ON COLUMN messages.supergroup_chat_created IS 'True if the supergroup has been created';
//COMMENT ON COLUMN messages.channel_chat_created IS 'True if the channel has been created';
//COMMENT ON COLUMN messages.migrate_to_chat_id IS 'The group has been migrated to a supergroup with this identifier';
//COMMENT ON COLUMN messages.migrate_from_chat_id IS 'The supergroup has been migrated from a group with this identifier';
//COMMENT ON COLUMN messages.connected_website IS 'The domain name of the website on which the user has logged in';



//CREATE SEQUENCE chat_messages_seq START WITH 1 INCREMENT BY 1;