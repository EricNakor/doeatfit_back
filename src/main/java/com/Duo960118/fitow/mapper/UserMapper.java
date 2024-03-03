package com.Duo960118.fitow.mapper;

import com.Duo960118.fitow.entity.UserDto;
import com.Duo960118.fitow.entity.UserEntity;
import org.springframework.context.annotation.Bean;

public class UserMapper {
    @Bean
    public static UserDto.UserInfoDto entityToUserInfoDto(UserEntity userEntity){
        return new UserDto.UserInfoDto(
                userEntity.getEmail(),
                userEntity.getNickName(),
                userEntity.getName(),
                userEntity.getGender(),
                userEntity.getBirth(),
                userEntity.getJoinDate(),
                userEntity.getPasswdEditDate(),
                userEntity.getProfileImg()
        );
    }
}
