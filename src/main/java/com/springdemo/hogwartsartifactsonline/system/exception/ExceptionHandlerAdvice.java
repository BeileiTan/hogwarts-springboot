package com.springdemo.hogwartsartifactsonline.system.exception;

import com.springdemo.hogwartsartifactsonline.system.Result;
import com.springdemo.hogwartsartifactsonline.system.StatusCode;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Result handleObjectNotFoundException(ObjectNotFoundException ex){
        return new Result(false, StatusCode.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Result handleValidationException(MethodArgumentNotValidException ex){
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        Map<String, String> map = new HashMap<>(errors.size());
        errors.forEach((error) ->{
            String key = ((FieldError) error).getField();
            String val = error.getDefaultMessage();
            map.put(key, val);
        });
        return new Result(false, StatusCode.INVALID_ARGUMENT, "Provide arguments here", map);
    }

    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Result handleAuthenticationException(Exception ex){
        return new Result(false, StatusCode.UNAUTHORIZED, "username or password is incorrect", ex.getMessage());
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Result handleInsufficientAuthenticationException(InsufficientAuthenticationException ex) {
        return new Result(false, StatusCode.UNAUTHORIZED, "Login credentials are missing.", ex.getMessage());
    }

    @ExceptionHandler(AccountStatusException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Result handleAccountStatusException(AccountStatusException ex){
        return new Result(false, StatusCode.UNAUTHORIZED, "user account is abnormal", ex.getMessage());
    }

    @ExceptionHandler(InvalidBearerTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Result handleInvalidBearerTokenException(InvalidBearerTokenException ex){
        return new Result(false, StatusCode.UNAUTHORIZED, "access token provided is expire, revoked, malformed or invalid for other reasons", ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    Result handleAccessDeniedException(AccessDeniedException ex){
        return new Result(false, StatusCode.FORBIDDEN, "no permission", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    Result handleOtherException(Exception ex){
        return new Result(false, StatusCode.INTERNAL_SERVER_ERROR, "A server internal error occurs", ex.getMessage());
    }

}
