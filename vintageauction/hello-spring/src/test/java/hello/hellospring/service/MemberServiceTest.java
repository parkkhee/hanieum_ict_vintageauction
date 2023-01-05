//package hello.hellospring.service;
//
//import hello.hellospring.domain.Member;
////import hello.hellospring.domain.Role;
//import hello.hellospring.dto.MemberInfoDto;
//import hello.hellospring.dto.request.MemberSignupDto;
//import hello.hellospring.repository.MemberRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//
//@SpringBootTest
//@Transactional
//class MemberServiceTest {
//
//    @Autowired
//    EntityManager em;
//
//    @Autowired
//    MemberRepository memberRepository;
//    @Autowired
//    MemberService memberService;
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    String PASSWORD = "password";
//
//    private void clear(){
//        em.flush();
//        em.clear();
//    }
//
//    //MemberSignUpDto를 반환하는 메서드입니다. 테스트용 회원가입
//    private MemberSignupDto makeMemberSignUpDto() {
//        return new MemberSignupDto("khp52","박관",PASSWORD);
//    }
//
//    //회원가입을 진행한 후 SecurityContextHolder에 인증된 회원정보를 저장하는 메서드. 반환은 회원가입 시 사용했던 MemberSignupDto를 반환
//    private MemberSignupDto setMember() throws Exception {
//        MemberSignupDto memberSignUpDto = makeMemberSignUpDto();
//        memberService.save(memberSignUpDto);
//        clear();
//        //이게 뭘까
//        SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
//
//        emptyContext.setAuthentication(new UsernamePasswordAuthenticationToken(User.builder()
//                .username(memberSignUpDto.getId())
//                .password(memberSignUpDto.getPassword())
//                .roles(Role.MEMBER.name())
//                .build(),
//                null, null));
//
//        SecurityContextHolder.setContext(emptyContext);
//        return memberSignUpDto;
//    }
//
//    //테스트가 끝날 때 마다, SecurityContextHolder의 Authentication(인증)정보를 비운다
//    @AfterEach
//    public void removeMember(){
//        SecurityContextHolder.createEmptyContext().setAuthentication(null);
//    }
//
//    /**
//     * 회원가입
//     *    회원가입 시 아이디, 비밀번호, 이름, 별명, 나이를 입력하지 않으면 오류
//     *    이미 존재하는 아이디가 있으면 오류
//     *    회원가입 후 회원의 ROLE 은 MEMBER
//     *
//     *
//     */
//    @Test
//    public void 회원가입_성공() throws Exception {
//        //given
//        MemberSignupDto memberSignUpDto = makeMemberSignUpDto();
//
//        //when
//        memberService.save(memberSignUpDto);
//        clear();
//
//        //then
//        Member member = memberRepository.findByMemberId(memberSignUpDto.getId()).orElseThrow(() -> new Exception("회원이 없습니다"));
//        assertThat(member.getMemberNo()).isNotNull();
//        assertThat(member.getMemberId()).isEqualTo(memberSignUpDto.getId());
//        assertThat(member.getMemberName()).isEqualTo(memberSignUpDto.getName());
//        assertThat(member.getRole()).isSameAs(Role.MEMBER);
//
//    }
//
//    @Test
//    public void 내정보조회() throws Exception {
//        //given
//        MemberSignupDto memberSignUpDto = setMember();
//
//        //when
//        MemberInfoDto myInfo = memberService.getMyInfo();
//
//        //then
//        assertThat(myInfo.getId()).isEqualTo(memberSignUpDto.getId());
//        assertThat(myInfo.getName()).isEqualTo(memberSignUpDto.getName());
//
//    }
//
//
//
//}