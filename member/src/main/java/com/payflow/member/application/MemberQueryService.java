package com.payflow.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.payflow.member.application.provided.MemberFinder;
import com.payflow.member.application.required.MemberRepository;
import com.payflow.member.domain.Member;
import com.payflow.member.domain.exception.MemberNotFoundException;

import lombok.RequiredArgsConstructor;

/**
 * @description    :
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryService implements MemberFinder {

	private final MemberRepository memberRepository;

	@Override
	public Member find(Long memberId) {
		return memberRepository.findById(memberId)
			.orElseThrow(() -> new MemberNotFoundException(memberId));
	}
}
