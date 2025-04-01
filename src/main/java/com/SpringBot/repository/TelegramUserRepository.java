package com.SpringBot.repository;

import com.SpringBot.models.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelegramUserRepository extends JpaRepository<TelegramUser, Long> {
    // No need for custom methods at this point
    // JpaRepository provides basic CRUD operations
}
