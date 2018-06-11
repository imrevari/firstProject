package com.progmasters.webshop.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private MessageSource messageSource;

    @Autowired
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    protected ResponseEntity<ValidationErrorDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        return new ResponseEntity<>(processFieldErrors(fieldErrors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    protected ResponseEntity<ValidationErrorDTO> handleBindException(BindException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        return new ResponseEntity<>(processFieldErrors(fieldErrors), HttpStatus.BAD_REQUEST);
    }

    private ValidationErrorDTO processFieldErrors(List<FieldError> fieldErrors) {
        ValidationErrorDTO dto = new ValidationErrorDTO();

        for (FieldError fieldError: fieldErrors) {
            dto.addFieldError(fieldError.getField(), messageSource.getMessage(fieldError, null));
        }

        return dto;
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseBody
    protected ResponseEntity<InvalidInputErrorDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        InvalidInputErrorDTO invalidInputErrorDTO = new InvalidInputErrorDTO(ex.getMessage());

        return new ResponseEntity<>(invalidInputErrorDTO, HttpStatus.BAD_REQUEST);
    }
}
