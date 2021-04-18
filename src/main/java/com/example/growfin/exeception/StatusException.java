package com.example.growfin.exeception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class StatusException extends RuntimeException{
    public StatusException() {
        super("Status is not a valid one");
    }


}
