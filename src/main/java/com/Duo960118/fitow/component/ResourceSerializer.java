package com.Duo960118.fitow.component;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Base64;

public class ResourceSerializer extends JsonSerializer<Resource> {

    @Override
    public void serialize(Resource resource, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        // 리소스를 읽어 바이트 배열로 변환
        byte[] bytes = resource.getInputStream().readAllBytes();
        // 바이트 배열을 Base64로 인코딩
        String base64String = Base64.getEncoder().encodeToString(bytes);
        // 직렬화된 Base64 문자열을 JSON으로 기록
        jsonGenerator.writeString(base64String);
    }
}