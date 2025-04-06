package com.example.springai.config;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class RagConfiguration {


	private static final Logger log = LoggerFactory.getLogger(RagConfiguration.class);

	@Value("vectorstore.json")
	private String vectorStoreName;

	@Value("classpath:/docs/paris_2025_2026.pdf")
	private Resource parisResource;

	@Bean
	SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) {
		var simpleVectorStore = SimpleVectorStore.builder(embeddingModel).build();
		var vectorStoreFile = getVectorStoreFile();
		if (vectorStoreFile.exists()) {
			log.info("Vector Store File Exists,");
			simpleVectorStore.load(vectorStoreFile);
		} else {
			var reader = new TikaDocumentReader(parisResource);
			var splitter = new TokenTextSplitter();
			log.info("Loading document into vector store...");
			List<Document> documents = splitter.split(reader.read());
			simpleVectorStore.add(documents);
			simpleVectorStore.save(vectorStoreFile);
			log.info("Document loaded into vector store.");
		}
		return simpleVectorStore;
	}

	private File getVectorStoreFile() {
		Path path = Paths.get("src", "main", "resources", "data");
		String absolutePath = path.toFile().getAbsolutePath() + "/" + vectorStoreName;
		return new File(absolutePath);
	}

}
