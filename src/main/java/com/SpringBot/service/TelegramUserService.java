package com.SpringBot.service;

import com.SpringBot.models.TelegramUser;
import com.SpringBot.repository.TelegramUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramUserService {
    
    private final TelegramUserRepository telegramUserRepository;
    
    /**
     * Save or update a user from Telegram API
     */
    public void saveUser(User telegramUser) {
        Optional<TelegramUser> userOptional = telegramUserRepository.findById(telegramUser.getId());
        
        if (userOptional.isPresent()) {
            TelegramUser existingUser = userOptional.get();
            // Update user information
            existingUser.setFirstName(telegramUser.getFirstName());
            existingUser.setLastName(telegramUser.getLastName());
            existingUser.setUserName(telegramUser.getUserName());
            existingUser.setLanguageCode(telegramUser.getLanguageCode());
            existingUser.setIsPremium(telegramUser.getIsPremium());
            existingUser.setAddedToAttachmentMenu(telegramUser.getAddedToAttachmentMenu());
            existingUser.updateLastInteraction();
            
            telegramUserRepository.save(existingUser);
            log.info("User {} {} id: {} has been updated in DB", telegramUser.getUserName(), telegramUser.getLastName(), telegramUser.getId());
        } else {
            // Create new user
            TelegramUser newUser = TelegramUser.builder()
                    .userId(telegramUser.getId())
                    .isBot(telegramUser.getIsBot())
                    .firstName(telegramUser.getFirstName())
                    .lastName(telegramUser.getLastName())
                    .userName(telegramUser.getUserName())
                    .languageCode(telegramUser.getLanguageCode())
                    .isPremium(telegramUser.getIsPremium())
                    .addedToAttachmentMenu(telegramUser.getAddedToAttachmentMenu())
                    .build();
            
            telegramUserRepository.save(newUser);
            log.info("User {} {} id: {} has been added in DB", telegramUser.getUserName(), telegramUser.getLastName(), telegramUser.getId());
        }
    }
    
    /**
     * Get user by ID
     */
    public Optional<TelegramUser> getUserById(Long id) {
        return telegramUserRepository.findById(id);
    }
    
    /**
     * Update user's last interaction
     */
    public void updateLastInteraction(Long userId) {
        telegramUserRepository.findById(userId).ifPresent(user -> {
            user.updateLastInteraction();
            telegramUserRepository.save(user);
        });
    }
}
