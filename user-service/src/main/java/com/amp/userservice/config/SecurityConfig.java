package com.amp.userservice.config;

import com.amp.userservice.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/user").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedPage("/accessDenied")
                .and()
                .logout().logoutUrl("/logout")
                .logoutSuccessUrl("/").permitAll()
                .and()
                .oauth2Login().loginPage("/login")
                .userInfoEndpoint()
                .userService(customOAuth2UserService);

        /*
        * CSRF(Croos-Site Script) 방지
        * 인증된 사람(Spring Security) 로그인 한사람
        * "/", "/login"은 permitAll
        * "/user" 는 인가된 사용자만
        * 권한 없으면 deni
        * logout 시키고 "/"로 던진다. --여기까진 spring security
        * customeOauth2userService를 userService로 등록
        * */


    }

}
