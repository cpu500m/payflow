package com.payflow.member.application.provided;

import com.payflow.member.domain.Member;

/**
 * @description    : 회원 정보 조회 port
 */
public interface MemberFinder {
	Member find(Long memberId);
}
