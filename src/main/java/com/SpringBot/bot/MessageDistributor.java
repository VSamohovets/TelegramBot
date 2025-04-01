package com.SpringBot.bot;

import com.SpringBot.bot.messages.AgreementMessage;
import com.SpringBot.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Component
public class MessageDistributor{

    @Autowired
    private AgreementMessage agreementMessage;

    @Autowired
    private TelegramUserService telegramUserService;

    @Autowired
    private GenAiService genAiService;

    @Autowired
    private ChatMessagesService chatMessagesService;

    @Autowired
    private TelegramChatService telegramChatService;

    public SendMessage handleTextMessage(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        User user = message.getFrom();
        Long userId = user.getId();

        switch (message.getText()){
            case "/start":
                return agreementMessage.agreementMessage(chatId, user.getFirstName().isEmpty() ? user.getUserName() : user.getFirstName());
            case "/reset":
                telegramChatService.disableLatestChat(telegramUserService.getUserById(userId).get());
                return prepareMessage(chatId, """
                You chat session has been reset. Bot forgot about previosly discussed topics. You are welcomed to start new conversation.""");
        }

        // Check if it's a new user or a user who hasn't agreed yet
        if (telegramUserService.getUserById(userId).isEmpty()) {
            return agreementMessage.agreementMessage(chatId, user.getFirstName().isEmpty() ? user.getUserName() : user.getFirstName());
        } else if (telegramChatService.getLatestChatByUser(telegramUserService.getUserById(userId).get()) == null) {
            return agreementMessage.agreementMessage(chatId, user.getFirstName().isEmpty() ? user.getUserName() : user.getFirstName());
        }

        // Update last interaction for existing users
        telegramUserService.updateLastInteraction(userId);
        telegramChatService.updateLastInteraction(userId);

        // Process the message with ChatGPT if user has agreed
        chatMessagesService.saveMessage(message);
        String response = genAiService.getGptResponse(message);

        return prepareMessage(chatId,response);
    }

    public SendMessage handleCallbackQuery(Update update) {
        String callbackData = update.getCallbackQuery().getData();
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        User user = update.getCallbackQuery().getFrom();
        Long userId = user.getId();

        if ("agree".equals(callbackData)) {
            // Save user to the database
            telegramUserService.saveUser(user);
            telegramChatService.createNewChat(userId);
            // Send welcome message
            String welcomeMessage = "Thank you for agreeing! You can now ask me any questions, and I'll use ChatGPT to answer them.";
            return prepareMessage(chatId, welcomeMessage);
        } else if ("cancel".equals(callbackData)) {
            if(telegramUserService.getUserById(userId).isPresent()) {
                telegramChatService.disableLatestChat(telegramUserService.getUserById(userId).get());
                telegramChatService.createNewChat(userId);
            }
            String cancelMessage = "You've chosen not to proceed. If you change your mind, you can start a new conversation with me anytime.";
            return prepareMessage(chatId, cancelMessage);
        } else {
            String unknown = String.format("%s Unknown call back has been received. Please try again later", callbackData);
            return prepareMessage(chatId, unknown);
        }
    }

    private SendMessage prepareMessage(long chatId, String text){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        return message;
    }
}
