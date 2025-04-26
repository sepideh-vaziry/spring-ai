package com.example.springllm.rag;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Slf4j
@Configuration
public class RagConfiguration {

  @Value("classpath:/docs/olympic-faq.txt")
  private Resource faqResource;

  @Value("vectorstore.json")
  private String vectorStoreName;

  @Bean
  SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) {
    SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(embeddingModel)
        .build();

    File vectorStoreFile = getVectorStoreFile();

    if (vectorStoreFile.exists()) {
      log.info("Vector store file exist.");

      simpleVectorStore.load(vectorStoreFile);
    } else {
      log.info("Vector store file does not exist. Loading documents.");

      TextReader textReader = new TextReader(faqResource);
      textReader.getCustomMetadata().put("filename", "olympic-faq.txt");
      List<Document> documents = textReader.get();

      TokenTextSplitter textSplitter = new TokenTextSplitter();
      List<Document> splitDocuments = textSplitter.apply(documents);

      simpleVectorStore.add(splitDocuments);
      simpleVectorStore.save(vectorStoreFile);
    }

    return simpleVectorStore;
  }

  private File getVectorStoreFile() {
    Path path = Paths.get("src", "main", "resources", "data");
    String absolutePath = path.toFile().getAbsolutePath() + "/" + vectorStoreName;

    return new File(absolutePath);
  }
}
