package hello.hellospring.dto.request;

import hello.hellospring.domain.Member;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@NoArgsConstructor //기본 생성자 생성
@AllArgsConstructor
@Getter
@Setter
public class MemberSignupDto {
    @NotBlank(message = "아이디를 입력하세요.")
    @NotEmpty
    private String id;
    @NotBlank(message = "이름을 입력하세요.")
    @NotEmpty
    private String name;
    @NotBlank(message = "비밀번호를 입력하세요.")
    @NotEmpty
    private String password;


    // DTO -> 객체
    public Member toEntity(){
        return Member.builder()
                .memberId(id)
                .memberName(name)
                .memberPassword(password)
                .build();
    }


}
