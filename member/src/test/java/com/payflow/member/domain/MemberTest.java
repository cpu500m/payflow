package com.payflow.member.domain;

import static com.payflow.member.domain.Member.*;
import static com.payflow.member.domain.MemberFixture.*;
import static com.payflow.member.domain.enums.MemberStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @description    :
 */
class MemberTest {

	Member member;
	PasswordEncoder passwordEncoder;

	@BeforeEach
	void setUp() {
		this.passwordEncoder = createPasswordEncoder();
		MemberRegisterRequest memberRegisterRequest = createMemberRegisterRequest();
		this.member = register(memberRegisterRequest, passwordEncoder);
	}

	@Test
	@DisplayName("가입 - 정상")
	void 가입_정상() throws Exception {
		assertEquals(ACTIVE, member.getStatus());
	}

	@Test
	@DisplayName("가입 - 유효성검증 실패")
	void 가입_실패() throws Exception {

		// 유효하지 않은 이메일
		MemberRegisterRequest req = createMemberRegisterRequest("abcd");
		assertThrows(IllegalArgumentException.class,() -> register(req,passwordEncoder));

		MemberRegisterRequest req2 = createMemberRegisterRequest("kabc@naver");
		assertThrows(IllegalArgumentException.class,() -> register(req2,passwordEncoder));
	}
	@Test
	@DisplayName("닉네임변경 - 성공")
	void 닉네임변경_성공() throws Exception {
		String newNickname = "podongdong";
		member.changeNickname(newNickname);
	   assertEquals(newNickname, member.getNickname());
	}

	@Test
	@DisplayName("닉네임변경 - 실패")
	void 닉네임변경_실패() throws Exception {
		assertThrows(NullPointerException.class,() -> member.changeNickname(null));
	}

	@Test
	@DisplayName("비밀번호검증 - 성공")
	void 비밀번호검증_성공() throws Exception {
		assertEquals(true,member.verifyPassword("test1234", passwordEncoder));
		assertEquals(false,member.verifyPassword("haha1234", passwordEncoder));
	}

	@Test
	@DisplayName("비밀번호변경")
	void changePassword() throws Exception {
		String newPassword = "haha1512";
		member.changePassword(newPassword, passwordEncoder);

	   assertEquals(passwordEncoder.encode(newPassword), member.getEncodedPassword());
	}

	@Test
	@DisplayName("상태변경 - 정지")
	void suspend() throws Exception {
		member.suspend();
		assertEquals(SUSPENDED, member.getStatus());
	}

	@Test
	@DisplayName("상태변경 - 정지 (실패)")
	void suspendFail() throws Exception {
		member.deactivate();
		assertThrows(IllegalStateException.class,() -> member.suspend());
	}

	@Test
	@DisplayName("상태변경 - 정지해제")
	void resume() throws Exception {
		member.suspend();

		member.resume();

		assertEquals(ACTIVE,member.getStatus());
	}

	@Test
	@DisplayName("상태변경 - 정지해제 (실패)")
	void resumeFail() throws Exception {
		member.deactivate();

		assertThrows(IllegalStateException.class,() -> member.resume());
	}

	@Test
	@DisplayName("상태변경 - 탈퇴")
	void deactivate() throws Exception {
	    member.deactivate();

		assertEquals(DEACTIVATED, member.getStatus());
	}

	@Test
	@DisplayName("상태변경 - 탈퇴 (실패)")
	void deactivateFail() throws Exception {
	    member.deactivate();

		assertThrows(IllegalStateException.class,() -> member.deactivate());
	}
}