package hello.hellospring.dto;

import hello.hellospring.domain.Member;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberInfoDto {

    private String memberId;
    private String memberName;
    private String memberImgUrl;
    private Long point;


    @Builder
    public MemberInfoDto(Member member) {
        this.memberId = member.getMemberId();
        this.memberName = member.getMemberName();
        this.memberImgUrl = member.getMemberImgUrl();
        this.point = member.getMemberPoint();
    }
}
