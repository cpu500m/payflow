package com.payflow.member.application;

import com.payflow.member.application.provided.MemberRegister;
import com.payflow.member.application.required.MemberRepository;
import com.payflow.member.application.required.PasswordEncoder;
import com.payflow.member.domain.Email;
import com.payflow.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MemberService implements MemberRegister {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public MemberResponse register(MemberRegisterRequest request) {
        Email email = new Email(request.email());

        if (memberRepository.existsByEmail(email.value())) {
            throw new IllegalStateException("Email already registered: " + email.value());
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        Member member = Member.create(email.value(), request.name(), encodedPassword);
        Member saved = memberRepository.save(member);

        return toResponse(saved);
    }

    @Override
    public MemberResponse findById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + id));

        return toResponse(member);
    }

    private MemberResponse toResponse(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getStatus()
        );
    }
}
