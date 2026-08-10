package org.example.aifile.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PDFService {
    private final PgVectorStore vectorStore;
    @Qualifier("googleGenAiChatModel")
    private final ChatModel chatModel;

    public String ragChat(String query) {
        ChatClient chatClient = ChatClient
                .builder(chatModel)
                .defaultSystem(
                        "제공된 검색 문서(context)에 기반해서 대답. 할 수 없다면 대답을 거부."
                )
                .defaultAdvisors(QuestionAnswerAdvisor.builder(vectorStore)
                        .searchRequest(SearchRequest.builder()
                                .topK(3)
                                .similarityThreshold(0.5)
                                .build())
                        .build())
                .build();
        return chatClient.prompt().user(query).call().content();
    }

    public int uploadPDF(MultipartFile file) {
        validate(file); // -> 문제가 있으면 throw 되면서 에러 핸들러로 처리가 됨
        // pdf 내용 추출
        try {
            Resource resource = new InputStreamResource(file.getInputStream());
            PagePdfDocumentReader reader = new PagePdfDocumentReader(resource,
                    PdfDocumentReaderConfig.builder().build());
            List<Document> pages = reader.read();
//            System.out.println(pages);
            TokenTextSplitter splitter = TokenTextSplitter.builder().build();
            List<Document> chunks = splitter.apply(pages); // 800 토큰으로 쪼개줌 (apply, split)
            chunks.forEach(chunk -> chunk.getMetadata().put("filename", file.getOriginalFilename()));
            // vector store
            vectorStore.add(chunks);
            return chunks.size();
        } catch (IOException e) {
            throw new IllegalArgumentException("파일을 읽는 중 오류가 발생했습니다.");
        }
//        return 0;
    }

    public void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }
        if (!file.getContentType().equals("application/pdf")) {
            throw new IllegalArgumentException("파일 형식이 PDF가 아닙니다.");
        }
        try (InputStream is = file.getInputStream()) {
            byte[] buffer = new byte[5];
            int read = is.read(buffer);
            if (read < 5 || !"%PDF-".equals(new String(buffer, StandardCharsets.US_ASCII))) {
                throw new IllegalArgumentException("유효한 PDF 파일이 아닙니다 (PDF Header Signature 미일치).");
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("파일을 읽는 중 오류가 발생했습니다.");
        }
    }
}
