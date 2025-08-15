package com.example.demo.controllers;

import com.example.demo.output.MovieList;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AIAgentStructure {

    private final ChatClient chatClient;

    public AIAgentStructure(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @GetMapping("/askAgent")
    public MovieList askLLM(@RequestParam String query) {

        String systemMessage = """
                Vous êtes un spécialiste dans le domaine du cinéma.
                Répond à la question de l'utilisateur à ce propos.
                """;

        return chatClient.prompt()
                .system(systemMessage)
                .user(query)
                .call()
                .entity(MovieList.class);
    }
}