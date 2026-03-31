package com.payflow.member.adapter.in.web;

import com.payflow.member.application.provided.MemberRegister;
import com.payflow.member.application.provided.MemberRegister.MemberRegisterRequest;
import com.payflow.member.application.provided.MemberRegister.MemberResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/members")
public class MemberController {

    private final MemberRegister memberRegister;

    public MemberController(MemberRegister memberRegister) {
        this.memberRegister = memberRegister;
    }

    @PostMapping
    public ResponseEntity<MemberResponse> register(@RequestBody MemberRegisterRequest request) {
        MemberResponse response = memberRegister.register(request);
        return ResponseEntity
                .created(URI.create("/api/v1/members/" + response.id()))
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponse> findById(@PathVariable Long id) {
        MemberResponse response = memberRegister.findById(id);
        return ResponseEntity.ok(response);
    }
}
