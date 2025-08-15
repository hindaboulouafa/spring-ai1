package com.example.demo.controllers;

import com.example.demo.output.CINModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/multimodal")
public class MultimodalController {

    private final ChatClient chatClient;

    @Value("classpath:/images/img1.jpg")
    private Resource image;

    public MultimodalController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    // Endpoint that returns text description
    @GetMapping("/describeText")
    public String describeImageText() {
        return chatClient.prompt()
                .system("Donne-moi une description de l'image")
                .user(u -> u.text("Décris cette image").media(MediaType.IMAGE_JPEG, image))
                .call()
                .content();
    }

    // Endpoint that returns a CINModel entity
    @GetMapping("/describeEntity")
    public CINModel describeImageEntity() {
        return chatClient.prompt()
                .system("Donne-moi une description de l'image")
                .user(u -> u.text("Décris cette image").media(MediaType.IMAGE_JPEG, image))
                .call()
                .entity(CINModel.class);
    }

    // Endpoint that accepts uploaded image + user query
    @PostMapping(value = "/askImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String askImage(@RequestParam String query, @RequestParam MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        return chatClient.prompt()
                .system("Répond à la question de l'utilisateur à propos de l'image fournie")
                .user(u -> u.text(query).media(MediaType.IMAGE_JPEG, new ByteArrayResource(bytes)))
                .call()
                .content();
    }
}