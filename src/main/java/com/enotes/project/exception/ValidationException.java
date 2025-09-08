package com.enotes.project.exception;

import java.util.Map;

public class ValidationException extends RuntimeException{

    Map<String,Object> error;
    
    public ValidationException(Map<String, Object> error) {
        super("validation error");
        this.error = error;
    }

    public Map<String, Object> getErrors() {
        return error;
    }
    
}
