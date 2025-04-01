package com.SpringBot.service;

import com.SpringBot.models.TelegramChatMessage;
import com.SpringBot.models.TelegramChat;
import com.SpringBot.models.TelegramUser;
import com.SpringBot.repository.ChatMessagesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.InputMismatchException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessagesService {

    @Autowired
    private TelegramUserService telegramUserService;

    @Autowired
    private TelegramChatService telegramChatService;

    private final ChatMessagesRepository chatMessagesRepository;

    public void saveMessage(Message message){
        TelegramUser user;
        if(telegramUserService.getUserById(message.getFrom().getId()).isPresent()){
            user = telegramUserService.getUserById(message.getFrom().getId()).get();
        } else {
            throw new InputMismatchException(String.format("The user %s doesn't exist in data base. ", message.getFrom().getId()));

        }
        TelegramChatMessage newMessage = TelegramChatMessage.builder()
                .telegramChat(telegramChatService.getLatestChatByUser(user))
                .messageThreadId(message.getMessageThreadId())
                .date(LocalDateTime.ofInstant(
                        Instant.ofEpochSecond(message.getDate()), ZoneId.systemDefault() ))
                .isTopicMessage(message.getIsTopicMessage())
                .isAutomaticForward(message.getIsAutomaticForward())
                .editDate(LocalDateTime.ofInstant(
                        Instant.ofEpochSecond((message.getEditDate() == null) ? 0 : message.getEditDate()), ZoneId.systemDefault() ))
                .hasProtectedContent(message.getHasProtectedContent())
                .mediaGroupId(message.getMediaGroupId())
                .authorSignature(message.getAuthorSignature())
                .text(message.getText())
                .hasMediaSpoiler(message.getHasMediaSpoiler())
                .newChatTitle(message.getNewChatTitle())
                .deleteChatPhoto(message.getDeleteChatPhoto())
                .groupChatCreated(message.getGroupchatCreated())
                .supergroupChatCreated(message.getSuperGroupCreated())
                .channelChatCreated(message.getChannelChatCreated())
                .migrateToChatId(message.getMigrateToChatId())
                .migrateFromChatId(message.getMigrateFromChatId())
                .connectedWebsite(message.getConnectedWebsite())
                .build();
        chatMessagesRepository.save(newMessage);
    }

    public void saveAiMessages(User user, String message, String json){
        TelegramUser telegramUser = telegramUserService.getUserById(user.getId()).get();
        TelegramChatMessage newMessage = TelegramChatMessage.builder()
                .telegramChat(telegramChatService.getLatestChatByUser(telegramUser))
                .text(message)
                .date(LocalDateTime.now())
                .jsonValue(json)
                .build();
        chatMessagesRepository.save(newMessage);

    }

    public List<TelegramChatMessage> getChatHistory(TelegramChat chat){
        return chatMessagesRepository.findByTelegramChatOrderByMessageIdAsc(chat);
    }


}
