package io.hypher.backendservice.platformdata.utillity.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class WrongBodyException extends Exception{
    private static final long serialVersionUID = 1L;

    public WrongBodyException(String message){
        super(message);
    }
    
}
