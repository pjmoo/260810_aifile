package org.example.aifile.service;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.content.Media;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class ImageService {
    @Qualifier("googleGenAiChatModel")
    private final ChatModel chatModel;

    public String explain(MultipartFile file) {
        ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultSystem("너는 이미지를 해석하는 역할이야. 이미지의 특징적인 부분을 설명해줘")
                .build();
        try {
            byte[] resized = resize(file.getInputStream(),
                    file.getContentType().split("/")[1]
                    , 512, 512);
            Media media = new Media(
                    MimeTypeUtils.parseMimeType(file.getContentType()),
                    new ByteArrayResource(resized));
            return chatClient.prompt()
                    .user(u -> u
                            .text("첨부한 이미지를 해석해주세요")
                            .media(media)).call().content();
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public byte[] resize(InputStream inputStream, String format, int width, int height) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            Thumbnails.of(inputStream)
                    .size(width, height)
                    .outputFormat(format)
                    .toOutputStream(outputStream);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return outputStream.toByteArray();
    }
}
