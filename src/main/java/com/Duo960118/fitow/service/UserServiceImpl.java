package com.Duo960118.fitow.service;

import com.Duo960118.fitow.entity.GenderEnum;
import com.Duo960118.fitow.entity.UserDto;
import com.Duo960118.fitow.entity.UserEntity;
import com.Duo960118.fitow.exception.PasswordNotMatchesException;
import com.Duo960118.fitow.mapper.UserMapper;
import com.Duo960118.fitow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /*BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    user.setPasswd(passwordEncoder.encode(passwd));*/
    /*BCryptPasswordEncoder 객체를 직접 new로 생성하는 방식보다
    PasswordEncoder 객체를 빈으로 등록해서 사용하는 것이 좋다.
    암호화 방식을 변경하면 BCryptPasswordEncoder를 사용한 모든 프로그램을
    일일이 찾아다니며 수정해야 하기 때문. SecurityConfig.java에 Bean 설정*/

    // 회원가입
    @Override
    public UserDto.JoinResponseDto join(UserDto.JoinRequestDto joinRequest) {
        // User user = new User(email, passwordE    private final NoticeRepository noticeRepository;ncoder.encode(passwd), name, nickName, gender, birth, profileImgDir);
        // 아래와 같이 생성자를 여러개 할 필요없이 Entity에 @Builder 설정해두고
        // 빌더 패턴 활용해서 각 기능별로 필요한 변수만 불러와서 작업에 용이 및 가독성 증가
        UserEntity userEntity = UserEntity.builder()
                .email(joinRequest.getEmail())
                .passwd(passwordEncoder.encode(joinRequest.getPasswd()))
                .name(joinRequest.getName())
                .nickName(joinRequest.getNickName())
                .gender(GenderEnum.fromString(joinRequest.getGender()))
                .birth(joinRequest.getBirth())
                .role(UserEntity.UserRoleEnum.NORMAL)
                .build();
        this.userRepository.save(userEntity);

        return UserMapper.entityToJoinResponseDto(userEntity);
    }

    // 회원 탈퇴
    @Override
    public void withdraw(UserDto.WithDrawRequestDto withdrawRequest) {
        UserEntity userEntity = this.findByEmail( withdrawRequest.getEmail());
        if (!passwordEncoder.matches(withdrawRequest.getPasswd(), userEntity.getPasswd())) {
            // 예외: 비밀번호 일치 하지 않음
            throw new PasswordNotMatchesException();
        }

        userRepository.delete(userEntity);
    }

    // 주어진 이메일이 DB에 존재하는지 확인
    @Override
    public UserEntity findByEmail(String email) {
        // 예외: 존재하지 않는 회원
        return userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("존재하지 않는 회원" + email ));
    }

    // 이메일 중복확인
    @Override
    public UserDto.CheckDuplicateResponseDto checkEmail(UserDto.CheckEmailRequestDto checkEmailRequest) {
        return userRepository.existsByEmail(checkEmailRequest.getEmail()) ?
                new UserDto.CheckDuplicateResponseDto(true) : new UserDto.CheckDuplicateResponseDto(false);
    }

    // 닉네임 중복확인
    @Override
    public UserDto.CheckDuplicateResponseDto checkNickName(UserDto.CheckNickNameRequestDto checkNickNameRequest) {
        return userRepository.existsByNickName(checkNickNameRequest.getNickName()) ?
                new UserDto.CheckDuplicateResponseDto(true) : new UserDto.CheckDuplicateResponseDto(false);
    }

    // 비밀번호 수정
    @Override
    // 연산이 독립적으로 이루어지고, 연산 중 다른 연산이 끼어들 수 없다. one by one.
    public UserDto.EditPasswdResponseDto editPasswd(UserDto.EditPasswdRequestDto editPasswdRequest) {
        // 이메일 존재하는지 확인
        UserEntity user = this.findByEmail(editPasswdRequest.getEmail());

        // 현재 비밀번호 맞는지 확인
        // 예외: 비밀번호 일치하지 않음
        if (!passwordEncoder.matches(editPasswdRequest.getCurrentPasswd(), user.getPasswd())) {
            throw new PasswordNotMatchesException();
        }
        // 변경
        user.updatePasswd(passwordEncoder.encode(editPasswdRequest.getNewPasswd()));

        return new UserDto.EditPasswdResponseDto(user.getPasswdEditDate());
    }

    // 닉네임 수정
    @Override
    public UserDto.EditNickNameResponseDto editNickName(UserDto.EditNickNameRequestDto editNickNameRequest) {
        // 이메일 존재하는지 확인
        UserEntity user = this.findByEmail(editNickNameRequest.getEmail());

        // 닉네임 수정
        user.updateNickName(editNickNameRequest.getNewNickName());

        return new UserDto.EditNickNameResponseDto(editNickNameRequest.getNewNickName());
    }

    // 프로필 사진 수정
    @Override
    public void editProfileImgName(String email, String profileImg) {
        // 이메일 존재하는지 확인
        UserEntity user = this.findByEmail(email);

        // 프로필 사진 수정
        user.updateProfileImg(profileImg);
    }

    @Override
    public UserEntity findByNickName(String nickName) {
        return userRepository.findByNickName(nickName).orElseThrow();
    }

    // 모든 유저 조회
    @Override
    public Page<UserDto.UserInfoDto> getAllUser(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserMapper::entityToUserInfoDto);
    }


    @Override
    public UserDto.UserInfoDto getUserInfo(String email) {
        return UserMapper.entityToUserInfoDto(this.findByEmail(email));
    }

    // 이메일  찾기
    @Override
    public UserDto.EmailListResponseDto findEmail(UserDto.FindEmailRequestDto findEmailRequest) {
        return new UserDto.EmailListResponseDto(userRepository.findByNameAndGenderAndBirth(findEmailRequest.getName(), GenderEnum.fromString(findEmailRequest.getGender()), findEmailRequest.getBirth()));
    }

    // 가입정보 찾기
    @Override
    public UserDto.FindUserInfoResponseDto findUserInfo(UserDto.FindUserInfoRequestDto findUserInfoRequest) {
        boolean isJoined = userRepository.existsByEmailAndNameAndGenderAndBirth(findUserInfoRequest.getEmail(), findUserInfoRequest.getName(), GenderEnum.fromString(findUserInfoRequest.getGender()), findUserInfoRequest.getBirth());
        return new UserDto.FindUserInfoResponseDto(isJoined);
    }

    // todo: querydsl 이용해서 어드민 페이지에서 역할 과 이름, 이메일로 검색하는 기능 구현
    // 만나이 계산
    @Override
    public int calculateAge(LocalDate birth) {
        return Period.between(birth, LocalDate.now()).getYears();
    }

    @Override
    public UserDto.AgeGenderResponseDto getUserAgeGender(UserDto.AgeGenderRequestDto ageGenderRequest) {
        UserEntity user = this.findByEmail(ageGenderRequest.getEmail());
        return UserDto.AgeGenderResponseDto.builder()
                .age(Period.between(user.getBirth(), LocalDate.now()).getYears())
                .gender(user.getGender())
                .build();
    }
}
