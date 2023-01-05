package hello.hellospring.repository;


import hello.hellospring.domain.Member;
import hello.hellospring.dto.MemberDuplicateDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByMemberId(String memberId);
    Optional<Member> findByMemberNo(Long memberNo);
    Optional<Member> findMemberByMemberIdAndMemberPassword(String loginId, String password);
    Member findByMemberName(String memberName); //닉네임으로 회원 찾기
    Boolean existsByMemberId(@Param("memberId") String memberId);
}
