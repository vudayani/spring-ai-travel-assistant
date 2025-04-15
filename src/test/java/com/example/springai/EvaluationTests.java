package com.example.springai;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.evaluation.RelevancyEvaluator;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EvaluationTests {
	
	@Autowired
	private ChatModel chatModel;
	
	@Autowired
	private SimpleVectorStore simpleVectorStore;

	@Test
	void testEvaluation() {

	    String userText = "What are some of the attractions opening in Paris in 2025 and 2026?";
	    ChatResponse response = ChatClient.builder(chatModel)
                .build().prompt()
                .advisors(new QuestionAnswerAdvisor(simpleVectorStore))
                .user(userText)
                .call()
                .chatResponse();
	    
	    String responseContent = response.getResult().getOutput().getText();


	    var relevancyEvaluator = new RelevancyEvaluator(ChatClient.builder(chatModel));
	    
        EvaluationRequest evaluationRequest = new EvaluationRequest(userText,
        		(List<Document>) response.getMetadata().get(QuestionAnswerAdvisor.RETRIEVED_DOCUMENTS), responseContent);
        
        EvaluationResponse evaluationResponse = relevancyEvaluator.evaluate(evaluationRequest);
        assertTrue(evaluationResponse.isPass(), "Response is not relevant to the question");

	}
}
