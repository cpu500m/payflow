package com.payflow.member.domain;

import java.util.regex.Pattern;

import jakarta.persistence.Embeddable;

/**
 * @description    :
 */
@Embeddable
public record Email(String address) {
	private static final Pattern EMAIL_PATTERN = Pattern.compile(
		"^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

	public Email {
		if (address == null || address.length() > 255
			|| !EMAIL_PATTERN.matcher(address).matches()) {
			throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다. :" + address);
		}
	}
}
