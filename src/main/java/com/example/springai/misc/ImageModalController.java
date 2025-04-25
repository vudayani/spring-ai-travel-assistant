package com.example.springai.misc;

import java.io.IOException;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageModalController {
	
	private final ChatClient chatClient;
    @Value("classpath:/images/java-open-ai.png")
    Resource sampleImage;

    public ImageModalController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/image-to-text")
    public String image() throws IOException {
        return chatClient.prompt()
                .user(u -> u
                        .text("Can you please explain what you see in the following image?")
                        .media(MimeTypeUtils.IMAGE_PNG,sampleImage)
                )
                .call()
                .content();
    }

}
