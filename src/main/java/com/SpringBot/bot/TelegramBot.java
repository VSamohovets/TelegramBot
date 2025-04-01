package com.SpringBot.bot;

import com.SpringBot.service.TelegramUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final String botUsername;
    private final Set<Long> agreedUsers = new HashSet<>();

    @Autowired
    private TelegramUserService telegramUserService;
    
    @Autowired
    private MessageDistributor messageDistributor;

    public TelegramBot(String botUsername, String botToken) {
        super(botToken);
        this.botUsername = botUsername;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                sendMessage(messageDistributor.handleTextMessage(update));
            } else if (update.hasCallbackQuery()) {
                sendMessage(messageDistributor.handleCallbackQuery(update));
            } else {
                SendMessage sendMessage = new SendMessage(update.getMessage().getChatId().toString(), "The bot supports text conversation only. \uD83D\uDE25");
                sendMessage(sendMessage);
            }
        } catch (Exception e) {
            log.error("Error processing update", e);
        }
    }

    private void sendMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error sending message", e);
        }
    }

    public void start() throws TelegramApiException {
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("start", "Start the bot and show landing message"));
        commands.add(new BotCommand("reset", "Reset your session, if you want to start new chat"));
        SetMyCommands setMyCommands = new SetMyCommands(commands, new BotCommandScopeDefault(),null);
        super.execute(setMyCommands);
    }
}
