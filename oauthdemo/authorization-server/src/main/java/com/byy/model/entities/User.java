package com.byy.model.entities;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import lombok.Data;

@ToString
@Data
@TableName("users")
public class User{
    @TableId
    private int id;

    private String password;


    @TableField("name")
    private String username;


}
