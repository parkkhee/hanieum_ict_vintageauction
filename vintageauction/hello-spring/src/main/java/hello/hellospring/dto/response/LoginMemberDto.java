package hello.hellospring.dto.response;

import hello.hellospring.domain.Member;
import lombok.Data;

@Data
public class LoginMemberDto {
    private Long memberNo;
    private String memberId;
    private String memberName;
    private Long point;
    private String memberImgUrl;

    public LoginMemberDto(Member member) {
        memberNo = member.getMemberNo();
        memberId = member.getMemberId();
        memberName = member.getMemberName();
        point = member.getMemberPoint();
        memberImgUrl = member.getMemberImgUrl();
    }

}
