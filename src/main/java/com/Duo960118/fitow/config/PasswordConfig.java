package com.Duo960118.fitow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {
    private static final char[] PASSWD_COLLECTION_SP_CHA = {'!', '@', '#', '$', '%', '^', '&', '*', '(', ')'};
    private static final char[] PASSWD_COLLECTION_NUM = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',};
    private static final char[] PASSWD_COLLECTION_ALL = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            '!', '@', '#', '$', '%', '^', '&', '*', '(', ')'};

    // 비밀번호 인코더
    // Bean을 만드는 가장 쉬운 방법으로 @Configuration이 적용된 파일에 메서드를 새로 추가하는 것.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static String getRandPasswd(int size, char[] passwdCollection) {
        StringBuilder randPasswd = new StringBuilder();
        for (int i = 0; i < size; i++) {
            int selectRandomPasswd = (int) (Math.random() * (passwdCollection.length));
            randPasswd.append(passwdCollection[selectRandomPasswd]);
        }
        return randPasswd.toString();
    }

    public static String generateTempPasswd(int spSize, int allSize, int numSize) {
        return getRandPasswd(spSize, PASSWD_COLLECTION_SP_CHA) + getRandPasswd(allSize, PASSWD_COLLECTION_ALL) + getRandPasswd(numSize, PASSWD_COLLECTION_NUM);
    }
}
