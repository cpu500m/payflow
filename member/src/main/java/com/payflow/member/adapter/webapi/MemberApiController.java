package com.payflow.member.adapter.webapi;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.payflow.member.adapter.webapi.dto.MemberRegisterResponse;
import com.payflow.member.application.provided.MemberModifier;
import com.payflow.member.domain.Member;
import com.payflow.member.domain.MemberRegisterRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @description    : WEB controller
 */

@RestController
@RequiredArgsConstructor
public class MemberApiController {
	private final MemberModifier memberModifier;

	@PostMapping("/api/members")
	public MemberRegisterResponse register(@RequestBody @Valid MemberRegisterRequest request) {
		Member member = memberModifier.register(request);

		return MemberRegisterResponse.from(member);
	}
}
