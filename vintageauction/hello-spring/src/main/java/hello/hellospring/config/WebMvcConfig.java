package hello.hellospring.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

//cors 오류 제어
@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000")  //3000포트 허용
                .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE")  //모든옵션에 대해서
                .allowCredentials(true);
    }

    @Value("C:/Users/root/Desktop/memberImg")
    private String memberImgUploadFolder;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);

        registry
                .addResourceHandler("/memberImg/**")
                .addResourceLocations("file:///" + memberImgUploadFolder)
                //.setCachePeriod()
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }

}
