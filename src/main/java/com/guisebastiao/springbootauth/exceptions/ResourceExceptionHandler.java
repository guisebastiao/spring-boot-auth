package com.guisebastiao.springbootauth.exceptions;

import com.guisebastiao.springbootauth.dtos.FieldErrorDTO;
import com.guisebastiao.springbootauth.dtos.ResponseEntityDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ResponseEntityDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getFieldErrors();
        List<FieldErrorDTO> fieldErrorDTOs = fieldErrors.stream().map(err -> {
            FieldErrorDTO fieldError = new FieldErrorDTO();
            fieldError.setField(err.getField());
            fieldError.setError(err.getDefaultMessage());
            return fieldError;
        }).collect(Collectors.toList());

        ResponseEntityDTO response = new ResponseEntityDTO();
        response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        response.setMessage("validation error");
        response.setFieldErrors(fieldErrorDTOs);
        response.setSuccess(Boolean.FALSE);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(DuplicateEntityException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ResponseEntityDTO> handleDuplicateEntityException(DuplicateEntityException e) {
        ResponseEntityDTO response = new ResponseEntityDTO();
        response.setStatus(HttpStatus.CONFLICT.value());
        response.setMessage(e.getMessage());
        response.setSuccess(Boolean.FALSE);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseEntityDTO> handleEntityNotFoundException(EntityNotFoundException e) {
        ResponseEntityDTO response = new ResponseEntityDTO();
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage(e.getMessage());
        response.setSuccess(Boolean.FALSE);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(RequiredAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ResponseEntityDTO> handleRequiredAuthenticationException(RequiredAuthenticationException e) {
        ResponseEntityDTO response = new ResponseEntityDTO();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setMessage(e.getMessage());
        response.setSuccess(Boolean.FALSE);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ResponseEntityDTO> handleRuntimeException(RuntimeException e) {
        ResponseEntityDTO response = new ResponseEntityDTO();
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage("An unexpected error occurred");
        response.setSuccess(Boolean.FALSE);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
