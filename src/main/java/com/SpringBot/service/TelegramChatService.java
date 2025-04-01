package com.SpringBot.service;

import com.SpringBot.models.TelegramChat;
import com.SpringBot.models.TelegramChatId;
import com.SpringBot.models.TelegramUser;
import com.SpringBot.repository.TelegramChatRepository;
import com.SpringBot.repository.TelegramUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramChatService {

    private final TelegramChatRepository telegramChatRepository;

    private final TelegramUserRepository telegramUserRepository;

    public void createNewChat(Long userId){
        TelegramUser user = telegramUserRepository.findById(userId).get();

        if(telegramChatRepository.findByUserAndIsActive(user,true).isPresent()) {
            telegramChatRepository.findByUserAndIsActive(user, true).get();
            return;
        }

        List<TelegramChat> userChats = telegramChatRepository.findByUser(user);
        int i = (userChats.isEmpty()) ? 1 : userChats.size()+1;

        TelegramChat telegramChat = TelegramChat.builder()
                .telegramChatId(new TelegramChatId(i, user))
                .isActive(true)
                .build();
        telegramChatRepository.save(telegramChat);
        log.info("Chat {} has been added in DB", telegramChat.getTelegramChatId().getChatId());
    }

    public TelegramChat getLatestChatByUser(TelegramUser user){
        if(telegramChatRepository.findByUserAndIsActive(user, true).isPresent()){
            return telegramChatRepository.findByUserAndIsActive(user, true).get();
        }else {
            return null;
        }
    }

    public void disableLatestChat(TelegramUser user){
        if(telegramChatRepository.findByUserAndIsActive(user, true).isPresent()){
            TelegramChat telegramChat = telegramChatRepository.findByUserAndIsActive(user, true).get();
            telegramChat.setIsActive(false);
            telegramChatRepository.save(telegramChat);
        }
    }

    public void updateLastInteraction(Long userId) {
        TelegramUser user = telegramUserRepository.findById(userId).get();
        telegramChatRepository.findByUserAndIsActive(user, true).ifPresent(chat -> {
            chat.updateLastInteraction();
            telegramChatRepository.save(chat);
        });
    }
}
