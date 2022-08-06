package com.amp.userservice.config;

import com.amp.userservice.config.oauth.CustomOAuth2UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Slf4j
@Configuration // IoC 빈(bean)을 등록
@EnableWebSecurity // 필터 체인 관리 시작 어노테이션
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) // 특정 주소 접근시 권한 및 인증을 위한 어노테이션 활성화
public class SecurityConfig {

    CustomOAuth2UserService customOAuth2UserService;

    //WebSecurityConfigurerAdapter가 deprecated 되면서 chain을 bean으로 등록해서 사용한다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws
            Exception {

        log.info("[SecurityConfig]  filterChain 접근");
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/admin/**").access("hasRole(Role.ROlE_ADMIN)")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/")
                .and()
                .oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(customOAuth2UserService);
        //구글 로그인이 완료된 뒤 후처리 1. 코드받기(인증), 2.엑세스토큰(권한)
        //Tip. 코드X (엑세스토큰 + 사용자프로필정보)

        return http.build();
    }
}
