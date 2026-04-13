package com.payflow.member.domain;

import jakarta.validation.constraints.Size;

/**
* @description    :
*/
public record MemberPasswordChangeRequest(
	@Size(min = 8, max = 100) String newPassword
) {
}
