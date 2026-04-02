package com.payflow.member.application.provided;

import com.payflow.member.domain.Member;

/**
 * @description    :
 */
public interface MemberFinder {
	Member find(Long memberId);
}
