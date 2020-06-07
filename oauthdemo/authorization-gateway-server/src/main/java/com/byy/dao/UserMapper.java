package com.byy.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.byy.model.entities.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {
    List<String> getRolesByUserId(@Param("id") int id);
}
