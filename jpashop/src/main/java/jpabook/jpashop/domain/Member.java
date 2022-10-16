package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    //컬렉션은 필드에서 초기화하는 것이 안전
    //영속성 컨텍스트에 의해 관리되는 순간 하이버네이트 내장 컬렉션으로 객체변환
    //영속된 상태이후로는 컬렉션 객체 변경하지 않는 것 추천
    @OneToMany(mappedBy = "member")
    @JsonIgnore  //Json API 생성시 무시되는 속성, 엔티티에 프레젠테이션 계층을 위한 로직이 추가된 경우
    private List<Order> orders = new ArrayList<>();
}
