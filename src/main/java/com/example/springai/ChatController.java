package com.example.springai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springai.model.TravelRecommendation;


@RestController
public class ChatController {
	
	private final ChatClient chatClient;

	public ChatController(ChatClient.Builder builder) {
		this.chatClient = builder
				.defaultSystem("You are a friendly travel assistant")
				.defaultAdvisors(new PromptChatMemoryAdvisor(new InMemoryChatMemory()))
				.build();
	}
	
	@GetMapping("/askLlm")
	public String askTravelAssistant(@RequestParam String question) {

		var response = chatClient
				.prompt(question)
				.call()
				.content();

		System.out.println(response);

		return response;
	}
	
	@GetMapping("/askLlm/outputConverter")
	public TravelRecommendation askTravelAssistantWithOutputConverter(
			@RequestParam(required = false) String question) {

		var response = chatClient
				.prompt(question)
				.call()
				.entity(TravelRecommendation.class);

		System.out.println(response);

		return response;
	}

}