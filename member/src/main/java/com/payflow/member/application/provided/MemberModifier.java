package com.payflow.member.application.provided;

import com.payflow.member.domain.Member;
import com.payflow.member.domain.MemberRegisterRequest;

import jakarta.validation.Valid;

/**
 * @description    : 회원 정보 등록 및 수정 port
 */
public interface MemberModifier {
	Member register(@Valid MemberRegisterRequest registerRequest);

	Member deactivate(Long memberId);
}
