package hello.hellospring.dto.request;

import hello.hellospring.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor //기본 생성자 생성
@AllArgsConstructor
@Getter
@Setter
public class MemberUpdateDto {
    //회원정보수정 Dto

    private String password;


    public Member toEntity(){
        return Member.builder()
                .memberPassword(password)
                .build();
    }
}
