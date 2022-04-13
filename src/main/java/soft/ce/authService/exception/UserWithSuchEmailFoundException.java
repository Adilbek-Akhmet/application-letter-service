package soft.ce.authService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserWithSuchEmailFoundException extends RuntimeException{

    public UserWithSuchEmailFoundException(String message)
    {
        super(message);
    }
}