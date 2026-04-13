package com.payflow.member.domain;

import jakarta.validation.constraints.Size;

/**
 * @description    :
 */
public record MemberNicknameChangeRequest(
	@Size(min = 5, max = 20) String newNickName
) {
}
