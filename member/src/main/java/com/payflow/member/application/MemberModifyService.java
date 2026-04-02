package com.payflow.member.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.payflow.member.application.provided.MemberFinder;
import com.payflow.member.application.provided.MemberModifier;
import com.payflow.member.application.required.MemberRepository;
import com.payflow.member.application.required.WalletClient;
import com.payflow.member.domain.exception.DuplicatedEmailException;
import com.payflow.member.domain.Email;
import com.payflow.member.domain.Member;
import com.payflow.member.domain.MemberRegisterRequest;
import com.payflow.member.domain.PasswordEncoder;
import com.payflow.member.domain.exception.WalletBalanceNotZeroException;

import lombok.RequiredArgsConstructor;

/**
 * @description    :
 */

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberModifyService implements MemberModifier {

	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;
	private final MemberFinder memberFinder;
	private final WalletClient walletClient;

	@Override
	@Transactional
	public Member register(MemberRegisterRequest registerRequest) {
		checkDuplicatedEmail(registerRequest);

		Member member = Member.register(registerRequest, passwordEncoder);

		memberRepository.save(member);

		walletClient.createWallet(member.getId());

		return member;
	}

	@Override
	@Transactional
	public Member deactivate(Long memberId) {
		Member member = memberFinder.find(memberId);

		checkWalletBalance(memberId);

		member.deactivate();

		return memberRepository.save(member);
	}

	private void checkWalletBalance(Long memberId) {
		long walletBalance = walletClient.getWalletBalance(memberId);
		if (walletBalance > 0) {
			throw new WalletBalanceNotZeroException(walletBalance);
		}
	}

	private void checkDuplicatedEmail(MemberRegisterRequest request) {
		if (memberRepository.findByEmail(new Email(request.email())).isPresent()) {
			throw new DuplicatedEmailException(request.email());
		}
	}
}
