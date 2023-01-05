//package hello.hellospring;
//
//import hello.hellospring.domain.Member;
//import hello.hellospring.repository.MemberRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//
//@Component
//@RequiredArgsConstructor
//public class TestData {
//    private final MemberRepository memberRepository;
//
//    @PostConstruct
//    public void init(){
//        Member member1 = new Member();
//        member1.setMemberId("test");
//        member1.setMemberPassword("test");
//        member1.setMemberName("test");
//        member1.setMemberPoint(100000L);
//        Member saveMember1 = memberRepository.save(member1);
//
//        Member member2 = new Member();
//        member2.setMemberId("mwkim");
//        member2.setMemberPassword("mwkim");
//        member2.setMemberName("mwkim");
//        member2.setMemberPoint(100000L);
//        Member saveMember2 = memberRepository.save(member2);
//
//
//        Member member3 = new Member();
//        member3.setMemberId("test2");
//        member3.setMemberPassword("test2");
//        member3.setMemberName("test2");
//        member3.setMemberPoint(100000L);
//        Member saveMember3 = memberRepository.save(member3);
//
//    }
//}
