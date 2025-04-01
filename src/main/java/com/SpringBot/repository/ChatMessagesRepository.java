package com.SpringBot.repository;

import com.SpringBot.models.TelegramChatMessage;

import com.SpringBot.models.TelegramChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessagesRepository extends JpaRepository<TelegramChatMessage, Long> {
    List<TelegramChatMessage> findByTelegramChatOrderByMessageIdAsc(TelegramChat telegramChat);
}
