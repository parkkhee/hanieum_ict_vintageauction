package hello.hellospring.interceptor;

import hello.hellospring.exception.UnauthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {

    // HandlerInterceptor 는 prehandle()만 있어도 된다.
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //요청 URI 가져오기
        String requestURI = request.getRequestURI();

        log.info("인증 체크 인터셉트 실행 {}", requestURI);

        //세션 가져오기
        HttpSession session = request.getSession();


        //미인증 사용자는 login 페이지로 보낸다.
        //인터셉트는 WebConfig 에서 등록해야 한다.
        if (session == null || session.getAttribute(session.getId()) == null) {
            log.info("미인증 사용자 요청");
            throw new UnauthorizedException("로그인이 필요합니다.");
        }
        return true; // 다음 진행으로 넘어가서 컨트롤러 호출
    }
}

