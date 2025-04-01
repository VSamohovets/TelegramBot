package com.SpringBot.bot.messages;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class AgreementMessage {

    public SendMessage agreementMessage(long chatId, String userName){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("""
                Hello %s!
                This bot can tell everything you ask about Valentin's skills, experience, and interests. Feel free to ask in your language if english is not preferred. He prepared it as a pet project when he was bored. Source code is using Spring Boot and Lombok frameworks for Java code and LangChain4J for GPT integration.
                Please press Proceed button to start the chat.""".formatted(userName));

        // Create buttons
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton agreeButton = new InlineKeyboardButton();
        agreeButton.setText("✅Proceed");
        agreeButton.setCallbackData("agree");

        InlineKeyboardButton cancelButton = new InlineKeyboardButton();
        cancelButton.setText("\uD83D\uDED1Cancel");
        cancelButton.setCallbackData("cancel");

        rowInline.add(agreeButton);
        rowInline.add(cancelButton);
        rowsInline.add(rowInline);

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        return message;
    }
}
