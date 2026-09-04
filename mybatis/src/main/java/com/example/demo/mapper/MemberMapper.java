package com.example.demo.mapper;

import com.example.demo.model.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MemberMapper {
    List<Member> selectAll();
    Optional<Member> selectById(@Param("id")Long id);
    Optional<Member> selectByEmail(@Param("email")String email);
    //위 두개는 해당아이디의 멤버가 있을수도있고 없을 수도있기 때문에 Optinal 객체로 받는다
    List<Member> selectAllOrderByAgeAsc();
    List<Member> selectAllOrderBy(@Param("order") String order,
                                  @Param("dir") String dir);
    List<Member> selectByNameLike(@Param("name") String name);

    int selectAllCount();
    int insert(@Param("member") Member member);
    int update(@Param("member") Member member);
    int delete(@Param("member") Member member);
    int deleteById(@Param("id") Long id);
    int deleteAll();
}
