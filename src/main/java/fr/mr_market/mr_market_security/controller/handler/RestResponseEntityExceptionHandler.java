package fr.mr_market.mr_market_security.controller.handler;

import fr.mr_market.mr_market_security.exception.BadRequestException;
import fr.mr_market.mr_market_security.exception.UnauthorizedException;
import fr.mr_market.mr_market_security.exception.ResourceNotFoundException;
import fr.mr_market.mr_market_security.swagger.model.authUser.Error;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String THIS_SHOULD_BE_APPLICATION_SPECIFIC = "Bad request arguments";
    public static final String THE_BODY_CANNOT_BE_CAST = "The body cannot be cast";

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    @ResponseStatus(value = HttpStatus.CONFLICT)
    protected Error handleConflict(RuntimeException ex) {
        log.error(THIS_SHOULD_BE_APPLICATION_SPECIFIC, ex);
        Error error = new Error();
        error.setCode(HttpStatus.CONFLICT.value());
        error.setMessage(ex.getMessage());
        error.setDescription(THIS_SHOULD_BE_APPLICATION_SPECIFIC);
        return error;
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected Error handleConstraintViolation(RuntimeException ex) {
        log.error(THE_BODY_CANNOT_BE_CAST, ex);
        Error error = new Error();
        error.setCode(HttpStatus.BAD_REQUEST.value());
        error.setMessage(ex.getMessage());
        error.setDescription(THE_BODY_CANNOT_BE_CAST);
        return error;
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class, BadRequestException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected Error handleUserNotFoundException(RuntimeException ex) {
        log.error(ex.getMessage());
        Error error = new Error();
        error.setCode(HttpStatus.BAD_REQUEST.value());
        error.setMessage(ex.getMessage());
        return error;
    }

    @ExceptionHandler(value = {UnauthorizedException.class, AccessDeniedException.class})
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    protected Error handleUnauthorizedException(RuntimeException ex) {
        log.error(ex.getMessage());
        Error error = new Error();
        error.setCode(HttpStatus.UNAUTHORIZED.value());
        error.setMessage(ex.getMessage());
        return error;
    }
}
