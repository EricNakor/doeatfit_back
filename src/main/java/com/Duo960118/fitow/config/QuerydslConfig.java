package com.Duo960118.fitow.config;

import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuerydslConfig {
    //@PersistenceContext
    //
    //EntityManager 를 빈으로 주입할 때 사용하는 어노테이션
    //빈으로 주입받을 때 EntityManager 의 경우 @Autowired 가 아니라 해당 어노테이션으로 주입
    //JPAQueryFactory 를 Bean 으로 등록해서 프로젝트 전역에서 사용할 수 있도록 한다
    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(JPQLTemplates.DEFAULT, entityManager);
    }
}