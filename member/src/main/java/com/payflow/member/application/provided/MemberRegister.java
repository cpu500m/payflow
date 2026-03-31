package com.payflow.member.application.provided;

import com.payflow.member.domain.MemberStatus;

public interface MemberRegister {

    MemberResponse register(MemberRegisterRequest request);

    MemberResponse findById(Long id);

    record MemberRegisterRequest(
            String email,
            String name,
            String password
    ) {}

    record MemberResponse(
            Long id,
            String email,
            String name,
            MemberStatus status
    ) {}
}
