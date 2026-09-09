package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class) //--> Entity가 생성되거나 수정될 때 auditing 기능을 사용해라
//--> CreatedDate / LastModifiedDate 가 동작할 수 있게 해줌
public class Article { //게시글 하나를 DB에 저장하기 위한 JPA Entity

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @CreatedDate //엔티티가 처음 생성된 시간을 자동으로 넣어라.
    private Date created;

    @LastModifiedDate //마지막으로 엔티티가 수정된 시간을 자동으로 넣어라
    private Date updated;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

//    Member 1명
//             ↓
//    Article 여러 개
    // Article 입장에서는 ManyToOne
}
