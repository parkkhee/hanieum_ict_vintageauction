package hello.hellospring.service;


import hello.hellospring.domain.Member;
import hello.hellospring.dto.MemberInfoDto;
import hello.hellospring.dto.request.MemberSignupDto;
import hello.hellospring.dto.request.MemberUpdateDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public interface MemberService{

    //회원가입
    Member save(MemberSignupDto memberDto) throws Exception;

    //회원가입 시 아이디 중복 체크
    boolean checkMemberIdDuplicate(String memberId);

    //특정 회원 조회
    Optional<Member> findOne(Long memberNo) throws Exception;

    //회원 전체 조회
    List<Member> findAll() throws Exception;

    //내 정보 조회
    MemberInfoDto getMyInfo(String memberId) throws Exception;

    //회원 정보 수정(비밀번호 변경)
    void memberUpdate(String id, MemberUpdateDto memberUpdateDto, MultipartFile multipartFile);

    //회원 탈퇴
    void withdraw(Member deleteMember) throws Exception;
}
