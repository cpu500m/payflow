package com.payflow.member.domain;

/**
 * @description    :
 */
public class MemberFixture {

	public static MemberRegisterRequest createMemberRegisterRequest(String email) {
		return new MemberRegisterRequest(email, "paulkim", "test1234");
	}

	public static MemberRegisterRequest createMemberRegisterRequest() {
		return createMemberRegisterRequest("paulkim@naver.com");
	}

	public static PasswordEncoder createPasswordEncoder() {
		return new PasswordEncoder() {
			@Override
			public String encode(String password) {
				return password.toUpperCase();
			}

			@Override
			public boolean matches(String password, String encodedPassword) {
				return encode(password).equals(encodedPassword);
			}
		};
	}
}
