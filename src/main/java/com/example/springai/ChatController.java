package com.example.springai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
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
				.defaultSystem("You are a friendly travel assistant. Answer like a superhero, full of energy and enthusiasm, as if you're guiding a traveler on an epic mission! If you don't know something, just say so!")
				.build();
	}
	
	@GetMapping("/askLlm")
	public String askTravelAssistant(
			@RequestParam(required = false) String question) {

		var response = chatClient
				.prompt(question)
				.advisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
				.call()
				.content();

		System.out.println(response);

		return response;
	}
	
	@GetMapping("/askLlm/outputConverter")
	public TravelRecommendation askTravelAssistantWithOutputConverter(
			@RequestParam(required = false) String question) {

		var response = chatClient
				.prompt("What are the top attractions in Paris?")
				.call()
				.entity(TravelRecommendation.class);

		System.out.println(response);

		return response;
	}

}