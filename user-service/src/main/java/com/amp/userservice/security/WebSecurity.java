package com.amp.userservice.security;

import com.amp.userservice.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	private Environment env;
	private UserService userService;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public WebSecurity(Environment env, UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.env = env;
		this.userService = userService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/users/**").permitAll()
				.and()
				.addFilter(getAuthenticationFilter());
//		http.authorizeRequests().antMatchers("/**")
//						.hasIpAddress("192.168.0.7")
//						.and()
//						.addFilter(getAuthenticationFilter());

		http.headers().frameOptions().disable();
	}

	private AuthenticationFilter getAuthenticationFilter() throws Exception{
		AuthenticationFilter authenticationFilter = new AuthenticationFilter();
		authenticationFilter.setAuthenticationManager(authenticationManager());

		return authenticationFilter;
	}

	//select pwd from users where email = ?
	//db_pwd(encrypted) == input_pwd(encryted)
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//사용자가 전달한 메일, 비밀번호로 로그인 처리
		auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
	}
}
