package org.example.aifile.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class ImageRagConfig {
    @Bean
    @Primary
    public EmbeddingModel primaryEmbeddingModel(@Qualifier("googleGenAiTextEmbedding") EmbeddingModel embeddingModel) {
        return embeddingModel;
    }

    @Bean
    public VectorStore imageVectorStore(JdbcTemplate jdbcTemplate,
                                        @Qualifier("openAiEmbeddingModel") EmbeddingModel embeddingModel) {
        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .vectorTableName("image_vector_store")

                .dimensions(1536)
                .distanceType(PgVectorStore.PgDistanceType.COSINE_DISTANCE)
                .indexType(PgVectorStore.PgIndexType.HNSW)
                .initializeSchema(true) // ConfigurationProperties로 받아야하는 값
                .build();
    }
}
