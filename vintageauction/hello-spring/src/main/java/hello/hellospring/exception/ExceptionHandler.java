package hello.hellospring.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@ControllerAdvice
@RestController
public class ExceptionHandler {
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @org.springframework.web.bind.annotation.ExceptionHandler(value = UnauthorizedException.class)
    public String handleBaseException(UnauthorizedException e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @org.springframework.web.bind.annotation.ExceptionHandler(NoSuchElementException.class)
    public String NoSuchElementExHandler(NoSuchElementException e){
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(value = PointLackException.class)
    public String PointLackExHandler(PointLackException e) {
        return e.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(value = BadRequestException.class)
    public String BadRequestExHandler(BadRequestException e){
        return e.getMessage();
    }
}
