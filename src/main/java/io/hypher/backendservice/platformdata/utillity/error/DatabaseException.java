package io.hypher.backendservice.platformdata.utillity.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
public class DatabaseException extends Exception {
    private static final long serialVersionUID = 1L;

    public DatabaseException(String message){
        super(message);
    }

    
}
