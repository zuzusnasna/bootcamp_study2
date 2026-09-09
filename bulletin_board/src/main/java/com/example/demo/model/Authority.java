package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Authority { // Authority(권한) 테이블과 연결되는 객체
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //id 자동생성
    private Long id;
    private String authority;

    @ManyToOne //
    @JoinColumn(name = "member_id")
    //@JoinColumn(name = "member_id")는 Authority 테이블에 member_id라는 외래키(FK) 컬럼을 만들고,
    // 그 값으로 어떤 Member와 연결되는지를 저장하겠다는 뜻
    private Member member;
}

//Member  ←────── Authority
//  1                                     N
//
//Member 입장    → OneToMany
//Authority 입장 → ManyToOne
//Member 객체에는 OneToMany 가 선언되어있지 않기 때문에
//단방향 ManyToOne 관계