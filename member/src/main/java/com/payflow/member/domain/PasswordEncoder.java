package com.payflow.member.domain;

/**
 * @description    :
 */
public interface PasswordEncoder {
	String encode(String rawPassword);
	boolean matches(String rawPassword, String encodedPassword);
}
