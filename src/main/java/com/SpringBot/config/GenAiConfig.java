package com.SpringBot.config;

import com.SpringBot.service.GenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenAiConfig {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.system-message}")
    private String systemMessage;

    @Bean
    public GenAiService genAiService(){
        return new GenAiService(apiKey, systemMessage);
    }
}
