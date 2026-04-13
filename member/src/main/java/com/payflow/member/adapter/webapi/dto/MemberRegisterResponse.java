package com.payflow.member.adapter.webapi.dto;

import com.payflow.member.domain.Member;

/**
 * @description    : 회원등록 Web API 반환 DTO
 */
public record MemberRegisterResponse(Long memberId, String email) {
	public static MemberRegisterResponse from(Member member) {
		return new MemberRegisterResponse(member.getId(), member.getEmail().address());
	}
}
