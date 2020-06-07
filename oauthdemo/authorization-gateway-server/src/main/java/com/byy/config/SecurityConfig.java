package com.byy.config;

import com.byy.auth.MyUserDetails;
import com.byy.auth.MyUserDetailsService;
import com.byy.auth.UserServiceInMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    MyUserDetailsService userDetailsService;
    @Autowired
    UserServiceInMemory userServiceInMemory;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        1、在内存中保存用户信息，用于快速测试
/*        auth.inMemoryAuthentication()
                .withUser("admin")
                .password(passwordEncoder().encode("admin"))
                //.authorities(Collections.emptyList());
                .authorities("read","write");*/
//        2、实现接口，还是将用户在内存中写死
//        auth.userDetailsService(userServiceInMemory).passwordEncoder(passwordEncoder());
        //3、用数据库保存用户信息
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("*/actuator").permitAll()
                .antMatchers("auth/**").permitAll()
                .anyRequest().authenticated() //所有请求都需要通过认证
                .and()
                .httpBasic() //Basic登录
                .and()
                .csrf().disable(); //关跨域保护
    }

    //@Bean
    //@Override
    //protected UserDetailsService userDetailsService(){
    //    InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    //    manager.createUser(User.withUsername("demoUser1").password("123456").authorities("USER").build());
    //    manager.createUser(User.withUsername("admin").password("admin").authorities("USER").build());
    //    manager.createUser(User.withUsername("demoUser2").password("123456").authorities("USER").build());
    //    return manager;
    //}
    /**
     * 需要配置这个支持password模式
     * support password grant type
     * @return
     * @throws Exception
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
