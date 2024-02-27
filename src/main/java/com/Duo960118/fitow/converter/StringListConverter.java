//package com.Duo960118.fitow.converter;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.persistence.AttributeConverter;
//
//import java.util.List;
//
//public class StringListConverter implements AttributeConverter<List<String>,String> {
//
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    // db 저장 시 기본적으로 json 형식으로 저장됨
//    // 다른 형식 지정 가능
//    @Override
//    public String convertToDatabaseColumn(List<String> attribute) {
//        try {
//            return mapper.writeValueAsString(attribute);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public List<String> convertToEntityAttribute(String dbData) {
//        try {
//            return mapper.readValue(dbData, List.class);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
