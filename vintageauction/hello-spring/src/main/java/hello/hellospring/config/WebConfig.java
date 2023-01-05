package hello.hellospring.config;

import hello.hellospring.interceptor.LogInterceptor;
import hello.hellospring.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Log 인터셉터  등록
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/*.ico", "/error");

        //로그인 check 인터셉터 등록
        //모든 경로에 적용하되, white list는 제외 시킨다.
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/api", "/api/members/new", "/api/members/login", "/api/members/logout", "/css/**", "/*.ico",
                        "/api/vintages", "/api/vintage/{vintageBoardId}", "/api/vintages/search","/api/vintages/category",
                        "/api/memberid/{memberId}/exists");

    }
}
