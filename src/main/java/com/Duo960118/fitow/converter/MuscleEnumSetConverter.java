//package com.Duo960118.fitow.converter;
//
//import com.Duo960118.fitow.entity.WorkoutEntity;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.persistence.AttributeConverter;
//import jakarta.persistence.Converter;
//
//import java.util.Set;
//
//@Converter(autoApply = true)
//public class MuscleEnumSetConverter implements AttributeConverter<Set<WorkoutEntity.MuscleEnum> ,String> {
//
//    private final ObjectMapper mapper =  new ObjectMapper();
//    @Override
//    public String convertToDatabaseColumn(Set<WorkoutEntity.MuscleEnum> muscleEnums) {
//        try {
//            return mapper.writeValueAsString(muscleEnums);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public Set<WorkoutEntity.MuscleEnum> convertToEntityAttribute(String dbData) {
//        // valueOf에서 기본적인 Exception 처리됨
//        try {
//            return mapper.readValue(dbData, new TypeReference<Set<WorkoutEntity.MuscleEnum>>() {});
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
