package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{

    private final MemberRepository memberRepository;

    //로그인
    public Optional<Member> login(String loginId, String password){
        Optional<Member> findMember = memberRepository.findMemberByMemberIdAndMemberPassword(loginId, password);

        //검색이 안되면 null 반환
        if(findMember == null){
            return null;
        }
        return findMember;
    }

    //로그아웃
}
