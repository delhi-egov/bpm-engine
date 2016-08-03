package in.gov.bpm.engine.config

import com.fasterxml.jackson.databind.ObjectMapper
import in.gov.bpm.engine.impl.NullKeySerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created by user-1 on 24/6/16.
 */
@Configuration
public class JacksonConfiguration {

    @Bean()
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.getSerializerProvider().setNullKeySerializer(new NullKeySerializer());
        return mapper;
    }

}
