package hello.hellospring.exception;

public class PointLackException extends RuntimeException{

    public PointLackException(String message) {
        super(message);
    }
}
