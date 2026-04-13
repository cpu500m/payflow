package com.payflow.member.domain;

import static java.util.Objects.*;
import static org.springframework.util.Assert.*;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import com.payflow.common.domain.AbstractEntity;
import com.payflow.member.domain.enums.MemberStatus;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description    :
 */

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@NaturalIdCache
public class Member extends AbstractEntity {

	@Embedded
	@NaturalId
	@AttributeOverride(name = "address", column = @Column(name = "email"))
	private Email email;

	private String nickname;

	private String encodedPassword;

	@Enumerated(value = EnumType.STRING)
	private MemberStatus status;

	private LocalDateTime registeredAt;

	public static Member register(MemberRegisterRequest request, PasswordEncoder passwordEncoder) {
		Member member = new Member();
		member.email = new Email(request.email());
		member.nickname = requireNonNull(request.nickName());
		member.encodedPassword = passwordEncoder.encode(requireNonNull(request.password()));

		member.registeredAt = LocalDateTime.now();
		member.status = MemberStatus.ACTIVE;

		return member;
	}

	public void changeNickname(String newNickname) {
		this.nickname = requireNonNull(newNickname);
	}

	public boolean verifyPassword(String password, PasswordEncoder passwordEncoder) {
		return passwordEncoder.matches(requireNonNull(password), this.encodedPassword);
	}

	public void changePassword(String newPassword, PasswordEncoder passwordEncoder) {
		this.encodedPassword = passwordEncoder.encode(requireNonNull(newPassword));
	}

	public void suspend() {
		state(this.status == MemberStatus.ACTIVE, "[회원 정지 실패] ACTIVE 상태가 아닙니다. 현재 상태 : " + this.status);

		this.status = MemberStatus.SUSPENDED;
	}

	public void resume() {
		state(this.status == MemberStatus.SUSPENDED, "[회원 정지해제 실패] SUSPENDED 상태가 아닙니다. 현재 상태 : " + this.status);

		this.status = MemberStatus.ACTIVE;
	}

	public void deactivate() {
		state(List.of(MemberStatus.ACTIVE, MemberStatus.SUSPENDED).contains(this.status), "[회원 탈퇴 실패] 탈퇴 가능 상태가 아닙니다. 현재 상태 : " + this.status);

		this.status = MemberStatus.DEACTIVATED;
	}
}
