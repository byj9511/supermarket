package com.byy.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceInMemory implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    private ArrayList<User> userList;

    @PostConstruct
    public void init() {
        String password = passwordEncoder.encode("admin");
        userList = new ArrayList<>();
        userList.add(new User("admin", password, AuthorityUtils.commaSeparatedStringToAuthorityList("admin")));
        userList.add(new User("user", password, AuthorityUtils.commaSeparatedStringToAuthorityList("client")));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> users = userList.stream().filter(user -> user.getUsername().equals(username)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(users)) {
            return users.get(0);
        } else {
            throw new UsernameNotFoundException("用户名或密码错误");
        }
    }
}
