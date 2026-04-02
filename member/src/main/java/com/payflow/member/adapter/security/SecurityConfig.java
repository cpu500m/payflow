package com.payflow.member.adapter.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @description    :
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	//todo spring security는 이번 프로젝트에서 관심 대상이 아니므로 1차작업에선 우선 전부 open. 추후 작업.
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) {
		return httpSecurity
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
			.build();
	}
}
