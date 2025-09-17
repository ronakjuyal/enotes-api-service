package com.enotes.project.util;


import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.enotes.project.dto.CategoryDto;
import com.enotes.project.dto.TodoDto;
import com.enotes.project.dto.TodoDto.StatusDto;
import com.enotes.project.enums.TodoStatus;
import com.enotes.project.exception.ValidationException;

@Component
public class Validation {

    public void categoryValidation(CategoryDto categoryDto){
        Map<String,Object> error=new LinkedHashMap<>();
        if(ObjectUtils.isEmpty(categoryDto)){
            throw new IllegalArgumentException("category Object/JSON cannot be null or empty");
        }
        else{
            if(ObjectUtils.isEmpty(categoryDto.getName())){
                error.put("name", "name field is empty or null");
            }
            else{
                if(categoryDto.getName().length()<10 || categoryDto.getName().length()>=100){
                    error.put("name", "name length min 10 and max 100");
                }
            }
            if(ObjectUtils.isEmpty(categoryDto.getDescription())){
                error.put("description", "description field is null or empty");
            }
            if(ObjectUtils.isEmpty(categoryDto.getIsActive())){
                error.put("isActive", "isActive field is null or empty");
            }
        }
        if(!error.isEmpty()) throw new ValidationException(error);
    }

    public static void todoValidation(TodoDto todo) {
        StatusDto status = todo.getStatus();
        Boolean isStatusValid = false;
        for(TodoStatus s : TodoStatus.values()){
            if(s.getId().equals(status.getId())){
                isStatusValid = true;
                break;
            }
        }
        if(!isStatusValid){
            throw new ValidationException(Map.of("status", "Invalid status value "+todo.getStatus().getId()));
        }
    }
}
