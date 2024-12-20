package com.Duo960118.fitow.mapper;

import com.Duo960118.fitow.entity.HomeContentEntity;
import com.Duo960118.fitow.entity.HomeContentDto;
import org.springframework.context.annotation.Bean;

public class HomeContentMapper {
    @Bean
    public static HomeContentDto.HomeContentInfoDto entityToHomeContentInfoDto(HomeContentEntity homeContentEntity){
        return HomeContentDto.HomeContentInfoDto.builder()
                .uuid(homeContentEntity.getUuidEntity().getUuid())
                .category(homeContentEntity.getCategory())
                .content(homeContentEntity.getContent())
                .isBeingUsed(homeContentEntity.getIsBeingUsed())
                .createdAt(homeContentEntity.getCreatedAt())
                .editedAt(homeContentEntity.getEditedAt())
                .build();
    }
}
