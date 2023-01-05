package hello.hellospring.service;

import hello.hellospring.domain.Member;

import java.util.Optional;

public interface LoginService {
    Optional<Member> login(String loginId, String password);
}
