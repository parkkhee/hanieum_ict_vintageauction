package hello.hellospring.controller;

import hello.hellospring.domain.Member;
import hello.hellospring.dto.MemberInfoDto;
import hello.hellospring.dto.request.MemberSignupDto;
import hello.hellospring.dto.request.MemberUpdateDto;
import hello.hellospring.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    /**
     * 회원 가입
     */
    @PostMapping(value = "/api/members/new")
    public ResponseEntity<?> create(@Valid @RequestBody MemberSignupDto memberSignupDto,
                                    BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        memberService.save(memberSignupDto);
        return new ResponseEntity<>("회원가입 성공", HttpStatus.OK);
    }
    /**
     * 회원가입 시 id 중복체크 (작성 중) -> 클라이언트에서 중복체크 버튼 클릭 시 중복체크로 구상..  id 존재할시 true/ id 없을시 false
     */
    @GetMapping("/api/memberid/{memberId}/exists")
    public ResponseEntity<?> checkMemberIdDuplicate(@PathVariable("memberId") String memberId) throws Exception{
        return ResponseEntity.ok(memberService.checkMemberIdDuplicate(memberId));
    }

    /**
     * 회원정보수정
     */
    @PostMapping("/api/memberUpdate")
    public ResponseEntity<?> memberUpdate(HttpServletRequest request,
                                          MemberUpdateDto memberUpdateDto,
                                          @RequestParam(value="memberImgUrl", required = false) MultipartFile multipartFile) throws Exception {
        HttpSession session = request.getSession(false);

        Member updateMember = (Member) session.getAttribute(session.getId());

        memberService.memberUpdate(updateMember.getMemberId(), memberUpdateDto, multipartFile);
        return new ResponseEntity<>("회원변경완료",HttpStatus.OK);

    }


    /**
     * 회원 전부 조회
     */
    @GetMapping(value = "/api/members")
    public ResponseEntity<?> memberList() throws Exception {
        List<Member> members = memberService.findAll();
        return ResponseEntity.ok(members);
    }

    /**
     * 내정보조회
     */
    @GetMapping("/api/member")
    public MemberInfoDto getMyInfo(HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession(false);
        Member member = (Member) session.getAttribute(session.getId());

        MemberInfoDto getMemberInfo = memberService.getMyInfo(member.getMemberId());


        return getMemberInfo;
    }

    /**
     * 회원탈퇴
     */
    @PostMapping("/api/members/withdraw")
    public void withdraw(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession(false);

        Member deleteMember = (Member) session.getAttribute(session.getId());
        memberService.withdraw(deleteMember);

        session.invalidate(); //세션 만료
    }

}