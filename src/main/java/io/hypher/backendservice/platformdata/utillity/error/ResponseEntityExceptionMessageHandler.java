package io.hypher.backendservice.platformdata.utillity.error;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Define the HTTP Status for Exceptions thrown
 */
@ControllerAdvice
public class ResponseEntityExceptionMessageHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> responseEntityExceptionMessageHandler(Exception e, WebRequest req){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), e.getMessage(), req.getDescription(false));
        
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }        
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException e, WebRequest req){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), e.getMessage(), req.getDescription(false));
        
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

        
    @ExceptionHandler(WrongBodyException.class)
    public ResponseEntity<?> wrongBodyException(WrongBodyException e, WebRequest req){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), e.getMessage(), req.getDescription(false));
        
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    
}
