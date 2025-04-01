package com.SpringBot.repository;

import com.SpringBot.models.TelegramChat;
import com.SpringBot.models.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TelegramChatRepository extends JpaRepository<TelegramChat, Long> {

    Optional<TelegramChat> findByUserAndIsActive(TelegramUser user, Boolean isActive);
    List<TelegramChat> findByUser(TelegramUser user);

}
