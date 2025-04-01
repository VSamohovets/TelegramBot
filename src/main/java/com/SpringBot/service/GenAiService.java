package com.SpringBot.service;

import com.SpringBot.models.TelegramChatMessage;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiChatModelName;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.output.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class GenAiService {

    @Autowired
    private ChatMessagesService chatMessagesService;

    @Autowired
    private TelegramUserService telegramUserService;

    @Autowired
    private TelegramChatService telegramChatService;

    private String systemMessage;

    private final OpenAiChatModel model;

    public GenAiService(String apiKey, String systemMessage){
        this.systemMessage = systemMessage;
        model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(OpenAiChatModelName.GPT_3_5_TURBO)
                .build();
    }

    public String getGptResponse(Message message){
        List<ChatMessage> messageSequence = new ArrayList<>();
        SystemMessage sysMessage = SystemMessage.systemMessage(systemMessage);
        messageSequence.add(sysMessage);
        List<TelegramChatMessage> savedMessages =  chatMessagesService.getChatHistory(telegramChatService.getLatestChatByUser(telegramUserService.getUserById(message.getFrom().getId()).get()));
        for(TelegramChatMessage savedMessage : savedMessages){
            if(savedMessage.getJsonValue() == null){
                messageSequence.add(UserMessage.userMessage(savedMessage.getText()));
            }else {
                messageSequence.add(AiMessage.aiMessage(savedMessage.getText()));
            }
        }
        messageSequence.add(UserMessage.userMessage(message.getText()));
        Response<AiMessage> answer = model.generate(messageSequence);
        saveGptResponse(message.getFrom(), answer);
        return answer.content().text();
    }

    private void saveGptResponse(User user, Response<AiMessage> response){
        chatMessagesService.saveAiMessages(user, response.content().text(), response.tokenUsage().toString());
    }

}
