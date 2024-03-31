package com.Duo960118.fitow.service;

import com.Duo960118.fitow.entity.UserDto;
import com.Duo960118.fitow.entity.UserEntity;
import com.Duo960118.fitow.mapper.UserMapper;
import com.Duo960118.fitow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
        /*BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    user.setPasswd(passwordEncoder.encode(passwd));*/
    /*BCryptPasswordEncoder 객체를 직접 new로 생성하는 방식보다
    PasswordEncoder 객체를 빈으로 등록해서 사용하는 것이 좋다.
    암호화 방식을 변경하면 BCryptPasswordEncoder를 사용한 모든 프로그램을
    일일이 찾아다니며 수정해야 하기 때문. SecurityConfig.java에 Bean 설정*/

    // 회원가입
    @Override
    @Transactional
    public boolean join(UserDto.JoinRequestDto joinRequest) {
        // User user = new User(email, passwordEncoder.encode(passwd), name, nickName, gender, birth, profileImgDir);
        // 아래와 같이 생성자를 여러개 할 필요없이 Entity에 @Builder 설정해두고
        // 빌더 패턴 활용해서 각 기능별로 필요한 변수만 불러와서 작업에 용이 및 가독성 증가
        try {
            UserEntity userEntity = UserEntity.builder()
                    .email(joinRequest.getEmail())
                    .passwd(new BCryptPasswordEncoder().encode(joinRequest.getPasswd()))
                    .name(joinRequest.getName())
                    .nickName(joinRequest.getNickName())
                    .gender(joinRequest.getGender())
                    .birth(joinRequest.getBirth())
                    .role(UserEntity.UserRoleEnum.NORMAL)
                    .build();
            this.userRepository.save(userEntity);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 회원 탈퇴
    @Override
    @Transactional
    public boolean withdraw(String email, String passwd) {
        UserEntity userEntity = this.findByEmail(email);
        if (!new BCryptPasswordEncoder().matches(passwd, userEntity.getPasswd())) {
            return false;
        }
        userRepository.delete(userEntity);
        return true;
    }

    // 주어진 이메일이 DB에 존재하는지 확인
    @Override
    @Transactional
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("존재하지 않는 회원"));
    }

    // 이메일 중복확인
    @Override
    @Transactional
    public boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    // 닉네임 중복확인
    @Override
    @Transactional
    public boolean checkNickName(String nickName) {
        return userRepository.existsByNickName(nickName);
    }

    // 비밀번호 수정
    @Override
    @Transactional
    // 연산이 독립적으로 이루어지고, 연산 중 다른 연산이 끼어들 수 없다. one by one.
    public boolean editPasswd(String email, UserDto.EditPasswdRequestDto editPasswdRequest) {
        try {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            // 이메일 존재하는지 확인
            UserEntity user = this.findByEmail(email);

            // 현재 비밀번호 맞는지 확인
            if (!passwordEncoder.matches(editPasswdRequest.getCurrentPasswd(), user.getPasswd())) {
                return false;
            }
            // 변경
            user.updatePasswd(passwordEncoder.encode(editPasswdRequest.getNewPasswd()));

        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 닉네임 수정
    @Override
    @Transactional
    public boolean editNickName(String email, UserDto.EditNickNameRequestDto editNickNameRequest) {
        try {
            // 이메일 존재하는지 확인
            UserEntity user = this.findByEmail(email);

            // 닉네임 수정
            user.updateNickName(editNickNameRequest.getNewNickName());
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 프로필 사진 수정
    @Override
    @Transactional
    public boolean editProfileImgName(String email, String profileImg) {
        try {
            UserEntity user = this.findByEmail(email);
            user.updateProfileImg(profileImg);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public UserEntity findByNickName(String nickName) {
        return userRepository.findByNickName(nickName).orElseThrow();
    }

    // 모든 유저 조회
    @Override
    public List<UserDto.UserInfoDto> getAllUser() {
        return userRepository.findAll().stream().map(UserMapper::entityToUserInfoDto).collect(Collectors.toList());
    }



    @Override
    public UserDto.UserInfoDto getUserInfo(String email) {
        return  UserMapper.entityToUserInfoDto(userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("존재하지 않는 회원")));
    }

    // 이메일  찾기
    @Override
    @Transactional
    public List<String> findEmail(UserDto.FindEmailRequestDto findEmailRequest) {
        return userRepository.findByNameAndGenderAndBirth(findEmailRequest.getName(), findEmailRequest.getGender(), findEmailRequest.getBirth());
    }

    // 가입정보 찾기
    @Override
    @Transactional
    public boolean findUserInfo(UserDto.FindUserInfoRequestDto userInfoDto) {
        return userRepository.existsByEmailAndNameAndGenderAndBirth(userInfoDto.getEmail(), userInfoDto.getName(), userInfoDto.getGender(), userInfoDto.getBirth());
    }

    // todo: querydsl 이용해서 어드민 페이지에서 역할 과 이름, 이메일로 검색하는 기능 구현
}
