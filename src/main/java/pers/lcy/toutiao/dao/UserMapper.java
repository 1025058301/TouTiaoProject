package pers.lcy.toutiao.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import pers.lcy.toutiao.model.User;
@Mapper
public interface UserMapper {

    void addUser(User user);

    User selectUserById(@Param("id") int id);

    User selectUserByName(@Param("name") String name);

    int selectUserIdByName(@Param("name") String name);
}