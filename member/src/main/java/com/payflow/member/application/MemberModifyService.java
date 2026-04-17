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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @description    : 회원 정보 등록 및 수정 application service
 */

@Service
@Transactional
@RequiredArgsConstructor
public class MemberModifyService implements MemberModifier {

	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;
	private final MemberFinder memberFinder;
	private final WalletClient walletClient;

	@Override
	public Member register(@Valid MemberRegisterRequest request) {
		checkDuplicatedEmail(request);

		Member member = Member.register(request, passwordEncoder);

		memberRepository.save(member);

		walletClient.createWallet(member.getId());

		return member;
	}

	public void changeNickname(Long memberId, String newNickname) {
		Member member = memberFinder.find(memberId);

		member.changeNickname(newNickname);

		memberRepository.save(member);
	}

	public void changePassword(Long memberId, String newPassword) {
		Member member = memberFinder.find(memberId);

		member.changePassword(newPassword, passwordEncoder);

		memberRepository.save(member);
	}

	@Override
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
