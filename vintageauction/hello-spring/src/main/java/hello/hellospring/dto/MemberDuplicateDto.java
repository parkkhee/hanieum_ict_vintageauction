package hello.hellospring.dto;

import hello.hellospring.domain.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MemberDuplicateDto {

    private String id;

    public Member memberDuplicateDto(){
        return Member.builder()
                .memberId(id)
                .build();

    }

}
