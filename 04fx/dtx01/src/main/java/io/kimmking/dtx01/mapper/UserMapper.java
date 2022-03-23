package io.kimmking.dtx01.mapper;

import io.kimmking.dtx01.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {

    @Select("select * from user where id = #{id};")
    User find(@Param("id") Long id);

    @Select("select * from user;")
    List<User> list();

}
