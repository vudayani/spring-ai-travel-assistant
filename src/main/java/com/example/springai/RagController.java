package com.example.springai;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RagController {
	
	private final ChatClient chatClient;
	
	@Value("classpath:/prompts/disney-template.st")
	private Resource disneyPromptTemplate;
	
	private final SimpleVectorStore simpleVectorStore;
	
	String ragPrompt = """
			Use the following pieces of context to answer the question at the end. If you don't know the answer, just say so.
			Question: {question}
			""";

	public RagController(ChatClient.Builder builder, SimpleVectorStore simpleVectorStore) {
		this.chatClient = builder
				.defaultSystem("You are a friendly travel assistant. If you don't know something, just say so!")
				.build();
		this.simpleVectorStore = simpleVectorStore;
	}
	
	@GetMapping("/disneyRag")
	public String askTravelAssistantWithRag(
			@RequestParam(defaultValue = "What are some of the attractions opening in Paris in 2025 and 2026?", required = false) String question) {

		String response = chatClient.prompt()
				.user(u -> u.text(ragPrompt).params(Map.of("question", question)))
				.advisors(new QuestionAnswerAdvisor(this.simpleVectorStore))
				.call()
				.content();

		System.out.println(response);

		return response;

	}
}