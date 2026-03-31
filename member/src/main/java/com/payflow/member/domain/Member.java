package com.payflow.member.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private Member(String email, String name, String encodedPassword) {
        this.email = email;
        this.name = name;
        this.password = encodedPassword;
        this.status = MemberStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
    }

    public static Member create(String email, String name, String encodedPassword) {
        return new Member(email, name, encodedPassword);
    }

    public void deactivate() {
        this.status = MemberStatus.WITHDRAWN;
    }

    public void activate() {
        this.status = MemberStatus.ACTIVE;
    }
}
