package com.byy.auth;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.byy.dao.UserMapper;
import com.byy.model.entities.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().eq("name", username);
        User user = userMapper.selectOne(queryWrapper);
        MyUserDetails userDetails=null;
        if (user!=null){
            userDetails=new MyUserDetails();
            int id = user.getId();
            List<String> roles=userMapper.getRolesByUserId(id);
            List<GrantedAuthority> roleAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(String.join(",", roles));
            BeanUtils.copyProperties(user, userDetails);
            userDetails.setAuthorities(roleAuthorities);
            return userDetails;
        }else {
            throw new UsernameNotFoundException("用户名错误");
        }
    }
}
