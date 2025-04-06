# spring-ai-travel-assistant
Demo project to explore key features of Spring AI using a Travel Assistant example. It showcases how to interact with LLMs in Spring Boot applications with support for:

- ChatClient (basic LLM interaction)
- Output Parsers
- Chat Memory
- Prompt Stuffing
- Retrieval-Augmented Generation (RAG)
- LLM Response Evaluation


## Getting Started

### Prerequisites

- Java 21
- Maven 
- API key for a supported LLM provider (e.g., OpenAI or Anthropic)

Set your API key as an environment variable before running:

```bash
export SPRING_AI_OPENAI_API_KEY=your-openai-key
# or for Anthropic
export SPRING_AI_ANTHROPIC_API_KEY=your-anthropic-key
```

**Note**: Make sure the matching Spring AI starter (OpenAI or Anthropic) is included in pom.xml

### Build the Project
```bash
mvn clean install
```

### Run the Application
```bash
mvn spring-boot:run
```
Once the application starts, you can access the following endpoints:

Available Endpoints         Endpoint	Description
/askLlm	                    Talk to the LLM (ChatClient, chat memory)
/askLlm/outputConverter     Use output converters to get structured responses from LLMs
/disney	                    Prompt stuffing with static Disney show info
/disneyRag	                RAG example using vector store for Paris 2025 info

All endpoints accept a question query parameter:
```
curl "http://localhost:8080/askLlm?question=Suggest travel tips for Paris"
```

### Evaluation
The project includes a JUnit test using RelevancyEvaluator to validate LLM responses during development.
```bash
mvn test
```

### Resources
- [Spring AI Reference Docs](https://docs.spring.io/spring-ai/reference/)

