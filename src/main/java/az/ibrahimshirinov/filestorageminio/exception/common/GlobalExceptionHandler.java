package az.ibrahimshirinov.filestorageminio.exception.common;


import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.validation.ConstraintViolation;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static az.ibrahimshirinov.filestorageminio.constants.HttpResponseConstants.*;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends DefaultErrorAttributes {

    private static final String ARGUMENT_VALIDATION_FAILED = "Argument validation failed";
    private static final String MAXIMUM_UPLOAD_SIZE_EXCEEDED = "Maximum upload size exceeded.";


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String,Object>> handle(NotFoundException ex,
                                                     WebRequest request) {
    log.trace("Resource not found {}",ex.getMessage());
    return ofType(request,HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String,Object>> handle(MaxUploadSizeExceededException ex,
                                                     WebRequest request){
        log.trace("Resource not found {}", ex.getMessage());
        return ofType(request,HttpStatus.PAYLOAD_TOO_LARGE,MAXIMUM_UPLOAD_SIZE_EXCEEDED);
    }

    @ExceptionHandler(MismatchedInputException.class)
    public ResponseEntity<Map<String,Object>> handle(MismatchedInputException ex,
                                                     WebRequest request){
        log.trace("Mismatched input {}",ex.getMessage());
        return ofType(request,HttpStatus.BAD_REQUEST,ex.getMessage());
    }


    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<Map<String,Object>> handle(ServiceUnavailableException ex,
                                                     WebRequest request){
        log.trace("Service unavailable {}",ex.getMessage());
        return ofType(request,HttpStatus.SERVICE_UNAVAILABLE,ex.getMessage());
    }

    @ExceptionHandler(InvalidStateException.class)
    public final ResponseEntity<Map<String, Object>> handle(InvalidStateException ex,
                                                            WebRequest request) {
        log.trace("Request is invalid state {}", ex.getMessage());
        return ofType(request, HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(
            {ConstraintViolationException.class, DataIntegrityViolationException.class}
    )
    public final ResponseEntity<Map<String, Object>> handle(ConstraintViolationException ex,
                                                            WebRequest request) {
        log.trace("Constraints violated {}", ex.getMessage());
        return ofType(request, HttpStatus.BAD_REQUEST, getConstraintViolationExceptionMessage(ex));
    }

    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    public final ResponseEntity<Map<String,Object>> handle(javax.validation.ConstraintViolationException ex,
                                                           WebRequest request){
        log.trace("Constraints violated {}", ex.getMessage());
        return ofType(request,HttpStatus.BAD_REQUEST,getConstraintViolationExceptionMessage(ex));
    }

    @ExceptionHandler(CannotDeleteForeignKeyConstraintException.class)
    public final ResponseEntity<Map<String,Object>> handle(CannotDeleteForeignKeyConstraintException ex,
                                                           WebRequest request){
        log.trace("Request to dependent object {}",ex.getMessage());
        return ofType(request,HttpStatus.BAD_REQUEST,ex.getMessage());
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public final ResponseEntity<Map<String,Object>> handle(PropertyReferenceException ex,
                                                           WebRequest request){
        log.trace("Wrong input: {}",ex.getMessage());
        return ofType(request,HttpStatus.BAD_REQUEST,ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handle(MethodArgumentNotValidException ex,
                                                     WebRequest request){
        List<ConstraintsViolationError>  validationErrors= ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ConstraintsViolationError(error.getField(),error.getDefaultMessage()))
                .collect(Collectors.toList());

        return ofType(request,HttpStatus.BAD_REQUEST,ARGUMENT_VALIDATION_FAILED,validationErrors);
    }

    protected ResponseEntity<Map<String, Object>> ofType(WebRequest request, HttpStatus status, String message) {
        return ofType(request, status, message, Collections.EMPTY_LIST);
    }

    private ResponseEntity<Map<String, Object>> ofType(WebRequest request, HttpStatus status, String message,
                                                       List validationErrors) {
        Map<String, Object> attributes = getErrorAttributes(request, ErrorAttributeOptions.defaults());
        attributes.put(STATUS, status.value());
        attributes.put(ERROR, status.getReasonPhrase());
        attributes.put(MESSAGE, message);
        attributes.put(ERRORS, validationErrors);
        attributes.put(PATH, ((ServletWebRequest) request).getRequest().getRequestURI());
        return new ResponseEntity<>(attributes, status);
    }

    private String getConstraintViolationExceptionMessage(javax.validation.ConstraintViolationException ex) {
        return ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList()).get(0);
    }

    private String getConstraintViolationExceptionMessage(ConstraintViolationException ex){
        return ex.getSQLException().getMessage();
    }
}
