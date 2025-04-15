package com.example.springai.misc;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class ToolCallingController {
	
	private final ChatClient chatClient;
	
	public ToolCallingController(ChatClient.Builder builder) {
		this.chatClient = builder.build();
	}
	
	@GetMapping("/toolCalling")
	public String askTravelAssistant(
			@RequestParam(required = false) String question) {
		
		String response = chatClient
		        .prompt("What day is tomorrow?")
		        .tools(new DateTimeTools())
		        .call()
		        .content();

		System.out.println(response);

		return response;
	}
	
	

}
