package com.example.springllm.rag;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReferenceDocsLoader {

  private final JdbcClient jdbcClient;
  private final VectorStore vectorStore;

  @Value("classpath:/docs/spring-boot-reference.pdf")
  private Resource pdfResource;

  @PostConstruct
  public void init() {
    Integer count = jdbcClient.sql("select count(*) from vector_store")
        .query(Integer.class)
        .single();

    log.info("Current count of the Vector Store: {}", count);
    if (count == 0) {
      log.info("Loading Spring Boot Reference PDF into Vector Store");
      var config = PdfDocumentReaderConfig.builder()
          .withPageExtractedTextFormatter(
              new ExtractedTextFormatter.Builder()
                  .withNumberOfBottomTextLinesToDelete(0)
                  .withNumberOfTopPagesToSkipBeforeDelete(0)
                  .build()
          )
          .withPagesPerDocument(1)
          .build();

      var pdfReader = new PagePdfDocumentReader(pdfResource, config);
      var textSplitter = new TokenTextSplitter();
      vectorStore.accept(textSplitter.apply(pdfReader.get()));

      log.info("Application is ready");
    }

  }

}
