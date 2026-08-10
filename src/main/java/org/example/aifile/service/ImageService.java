package org.example.aifile.service;

import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.content.Media;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    @Qualifier("googleGenAiChatModel")
    private final ChatModel chatModel;
    @Qualifier("imageVectorStore")
    private final VectorStore imageVectorStore;
    // Supabase Storage
    private final S3Template s3Template;

    public String explain(MultipartFile file) {
        ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultSystem("너는 이미지를 해석하는 역할이야. 이미지의 특징적인 부분을 설명해줘")
                .build();
        try {
            String uuid = UUID.randomUUID().toString();
            String extension = StringUtils.getFilenameExtension(file.getContentType());
            String newFilename = uuid + "." + extension;
            S3Resource result = s3Template.upload("rag", newFilename, file.getInputStream());
            // https://wcjfoevpwelyqclvpshz.storage.supabase.co/storage/v1/s3/rag/movie.jpg
            // https://wcjfoevpwelyqclvpshz.supabase.co/storage/v1/object/public/rag/movie.jpg
            // storage.supabase.co/storage/v1/s3 -> supabase.co/storage/v1/object/public
            System.out.println("result = " + result.getURL());
            String publicUrl = result.getURL().toString()
                    .replace("storage.supabase.co/storage/v1/s3",
                            "supabase.co/storage/v1/object/public");
            System.out.println("publicUrl = " + publicUrl);
            byte[] resized = resize(file.getInputStream(),
                    file.getContentType().split("/")[1]
                    , 512, 512);
            Media media = new Media(
                    MimeTypeUtils.parseMimeType(file.getContentType()),
                    new ByteArrayResource(resized));
            // doc
            // import org.springframework.ai.document.Document;
            String caption = chatClient.prompt()
                    .user(u -> u
                            .text("첨부한 이미지를 해석해주세요")
                            .media(media)).call().content();
            Document document = Document.builder()
//                    .media(media)
                    .text(caption)
                    .metadata("caption", caption)
                    .metadata("publicUrl", publicUrl)
                    .build();
//            return chatClient.prompt()
//                    .user(u -> u
//                            .text("첨부한 이미지를 해석해주세요")
//                            .media(media)).call().content();
            imageVectorStore.add(List.of(document));
            return caption;
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
