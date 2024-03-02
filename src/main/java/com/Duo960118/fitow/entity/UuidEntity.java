package com.Duo960118.fitow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

// @Embeddable: 다른 Entity에서 필드로 선언하여 사용
// @MappedSuperclass: 다른 Entity에서 상속하여 사용
@Getter
@Embeddable
//@MappedSuperclass
// Entity 칼럼 상속
// 테이블과 매핑하지 않고 부모 클래스를 상속받는 자식 클래스에게 매핑 정보(엔티티 칼럼)만 제공하고 싶으면
// @MappedSuperclass를 사용하면 된다.
public class UuidEntity {
    @UuidGenerator
    /*PK 값에 대한 생성 전략
    @Id와 함께 엔티티 또는 매핑된 슈퍼클래스의 PK 속성 또는 필드에 적용 할 수 있음*/
    /*public UuidGenerator() {
    \this.uuid = UUID.randomUUID();
    \}*/
    @Column(columnDefinition = "BINARY(16)",unique = true,updatable = false)
    /*컬럼타입을 BINARY(16)으로 하지 않으면 공백이 들어가며 원하는 동작이 이루어지지 않는다고 한다.
    @Column 어노테이션을 꼭 사용할 필요는 없으며, DB의 컬럼 타입이 BINARY(16) 이면 된다.
    **UUID 크기 최소화하기
    각 필드를 구분하기 위한 dash('-')는 의미가 없으므로 제거 >> 총 32개의 문자열이 생성
    이걸 DB에 저장하게 되면 CHAR(32)의 필드를 PK로 사용하게 됩니다. 일반적으로 사용하는 BIGINT(8바이트)보다 4배 큽니다.
    MySQL의 경우 PK로 지정하면 자동으로 인덱스로 지정되므로, 4배나 큰 값을 계속해서 저장하는 것입니다.
    이를 개선하기 위해, CAHR(32)가 아닌 Binary형태로 변환해 BINARY(16)으로 저장하면 크기를 절반으로 줄일 수 있습니다.
    물론, DB에 Binary 타입으로 저장하게 되면, 앞으로 UUID를 조회할 땐 사람이 식별할 수 있는 값으로 변환하는 과정이 필요합니다.*/
    private UUID uuid;
    /*UUID는 문자열로 구성되므로, 기본 키 값이 숫자일 때와 달리 인덱스를 사용할 때 성능에 영향을 미칠 수 있다.
    또한, UUID는 고유성을 보장하지만, 순서를 보장하지 않기 때문에 순서에 의존하는 비지니스 로직에서는 추천하지 않음.*/
}
