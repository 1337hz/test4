package pl.hubert.geometry.exception;

import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationExceptionDto handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        ValidationExceptionDto validationException = new ValidationExceptionDto();
        exception.getFieldErrors().forEach(fieldError ->
                validationException.addViolation(fieldError.getField(), fieldError.getDefaultMessage()));
        return validationException;
    }

    @ExceptionHandler(PropertyRequiredException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handlePropertyRequiredException(PropertyRequiredException exception) {
        return new ExceptionDto(exception.getMessage());
    }

    @ExceptionHandler(TypeNotRecognizedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleTypeNotRecognizedException(TypeNotRecognizedException exception) {
        return new ExceptionDto(exception.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleMissingRequestBodyException(HttpMessageNotReadableException ex) {
        return new ExceptionDto("Required request body is missing.");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionDto handleNoResourceFoundException(NoResourceFoundException exception) {
        return new ExceptionDto(exception.getMessage());
    }

    @ExceptionHandler(MappingMismatchException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionDto handleNoResourceFoundException(MappingMismatchException ex) {
        return new ExceptionDto("Something went bad!");
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionDto handleInvalidDataAccess(InvalidDataAccessApiUsageException exception) {
        return new ExceptionDto("Invalid parameter provided");
    }
}
