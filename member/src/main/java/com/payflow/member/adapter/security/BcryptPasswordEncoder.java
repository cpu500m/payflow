package com.payflow.member.adapter.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.payflow.member.domain.PasswordEncoder;

/**
 * @description    : passwordEncoder adapter
 */
@Component
public class BcryptPasswordEncoder implements PasswordEncoder {

	private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Override
	public String encode(String rawPassword) {
		return encoder.encode(rawPassword);
	}

	@Override
	public boolean matches(String rawPassword, String encodedPassword) {
		return encoder.matches(rawPassword, encodedPassword);
	}
}
