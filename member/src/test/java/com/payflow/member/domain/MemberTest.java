package com.payflow.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @Test
    @DisplayName("Member.create sets ACTIVE status and assigns all fields")
    void create() {
        Member member = Member.create("user@example.com", "John", "encoded-pw");

        assertThat(member.getEmail()).isEqualTo("user@example.com");
        assertThat(member.getName()).isEqualTo("John");
        assertThat(member.getPassword()).isEqualTo("encoded-pw");
        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
        assertThat(member.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("deactivate changes status to WITHDRAWN")
    void deactivate() {
        Member member = Member.create("user@example.com", "John", "encoded-pw");

        member.deactivate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.WITHDRAWN);
    }

    @Test
    @DisplayName("activate changes status back to ACTIVE")
    void activate() {
        Member member = Member.create("user@example.com", "John", "encoded-pw");
        member.deactivate();

        member.activate();

        assertThat(member.getStatus()).isEqualTo(MemberStatus.ACTIVE);
    }
}
