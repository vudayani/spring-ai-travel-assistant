package com.example.springai;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PromptStuffingController {
	
	private final ChatClient chatClient;
	
	@Value("classpath:/docs/disneyTalesOfMagicResource.txt")
	private Resource disneyResource;
	
	
	String prompt = """
			Use the following pieces of context to answer the question at the end. If you don't know the answer, just say so.
			{context}
			Question: {question}
			""";

	public PromptStuffingController(ChatClient.Builder builder) {
		this.chatClient = builder
				.defaultSystem("You are a friendly travel assistant. If you don't know something, just say so!")
				.build();
	}
	
	@GetMapping("/disney")
	public String askTravelAssistantWithContext(
			@RequestParam(defaultValue = "What are the featured stories in Disney Tales of Magic night show in Paris Disneyland", required = false) String question) throws IOException {

		String context = readResource(disneyResource);
		String response = chatClient.prompt()
				.user(question)
				.user(u -> u.text(prompt).params(Map.of("context", context, "question", question)))
				.call()
				.content();

		System.out.println(response);

		return response;

	}
	
	private String readResource(Resource resource) throws IOException {
		return new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);
	}
}