package com.poseidon.fridge.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class FridgeNotFoundAdvice {
    
    @ResponseBody
    @ExceptionHandler(FridgeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String fridgeNotFoundHandler(FridgeNotFoundException ex) {
        return ex.getMessage();
    }

}
