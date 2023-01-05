package hello.hellospring.controller;

import hello.hellospring.domain.Member;
import hello.hellospring.dto.request.MemberSigninDto;
import hello.hellospring.dto.response.LoginMemberDto;
import hello.hellospring.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final LoginService loginService;


    //4. filter를 적용한 로그인
    @PostMapping("/api/members/login")
    public ResponseEntity<?> login(@Valid @RequestBody MemberSigninDto form, BindingResult bindingResult,
                                   HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam(defaultValue = "/api") String redirectURL) {

        //유효하지 않은 입력 폼 입력 시 로그인 폼으로 이동
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>("올바른 id, password 값을 입력하세요.", HttpStatus.BAD_REQUEST);
        }

        Optional<Member> loginMember = (Optional<Member>) loginService.login(form.getId(), form.getPassword());

        //조회된 회원이 없는 경우
        if (loginMember.isEmpty()) {
            //reject()는 글로벌 오류이다.
            bindingResult.reject("loginfail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return new ResponseEntity<>("아이디 혹은 비밀번호가 올바르지 않습니다.", HttpStatus.BAD_REQUEST);
        }


        /*로그인 성공 처리 TODO*/

        //request.getSession()은 세션이 있으면 반환하고
        //없으면 신규 생성해서 반환한다.
        HttpSession session = request.getSession(true);
        //세션에 로그인 회원 정보를 보관한다.
        session.setAttribute(session.getId(), loginMember.get());
        session.setAttribute("memberNo", loginMember.get().getMemberNo()); //게시글 올릴 때를 위해서 session에 저장
        session.setAttribute("memberId",loginMember.get().getMemberId());


        /*filter 에서 넘겨받은 redirectURL을 적용 시키기 위해서 이렇게 바꾸었다.
        로그인을 하지 않고 /items 로 갔다가 로그인 페이지로 리다이렉트 되었다가
        로그인을 하면 /items 페이지로 이동할 것이다. 반면에 로그인을 했으면 defaultvalue 인 "/"가
        적용되어서 home 으로 돌아갈 것이다.
         */
        Member member = loginMember.get();
        LoginMemberDto loginMemberDto = new LoginMemberDto(member);
        return new ResponseEntity<>(loginMemberDto, HttpStatus.OK);
    }


    //3. HttpSession 을 이요한 로그아웃
    @PostMapping("/api/members/logout")
    public ResponseEntity<?> logoutV3(HttpServletRequest request) {
        //세션을 없애는 것이 목적이기 때문에 false 옵션을 주고 조회해 온다.
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate(); //세션 만료
            return new ResponseEntity<>("로그아웃 완료", HttpStatus.OK);
        }
        return new ResponseEntity<>("세션이 만료되었거나 로그인을 하지 않았습니다.", HttpStatus.UNAUTHORIZED);
    }
}