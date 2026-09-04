package com.example.demo.mapper;

import com.example.demo.model.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ArticleMapper {

    @Select("SELECT * FROM article")
    List<Article> selectAll();

    @Select("SELECT * FROM article WHERE id = #{id}")
    Optional<Article> selectById(@Param("id") Long id);
}
