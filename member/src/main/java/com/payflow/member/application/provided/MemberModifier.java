package com.payflow.member.application.provided;

import com.payflow.member.domain.Member;
import com.payflow.member.domain.MemberRegisterRequest;

import jakarta.validation.Valid;

/**
 * @description    :
 */
public interface MemberModifier {
	Member register(@Valid MemberRegisterRequest registerRequest);

	Member deactivate(Long memberId);
}
