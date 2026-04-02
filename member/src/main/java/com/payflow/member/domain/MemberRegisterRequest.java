package com.payflow.member.domain;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * @description    : member register DTO
 */
public record MemberRegisterRequest(
	@Email String email,
	@Size(min = 5, max = 20) String nickName,
	@Size(min = 8, max = 100) String password
) {
}
