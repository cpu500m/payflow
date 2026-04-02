package com.payflow.member.application.required;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.payflow.member.domain.Email;
import com.payflow.member.domain.Member;

/**
 * @description    :
 */
public interface MemberRepository extends Repository<Member, Long> {
	Member save(Member member);

	Optional<Member> findById(Long memberId);

	Optional<Member> findByEmail(Email email);
}
