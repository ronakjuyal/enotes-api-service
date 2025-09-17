package com.enotes.project.service;

import java.util.List;

import com.enotes.project.dto.TodoDto;

public interface TodoService {
    public Boolean saveTodo(TodoDto todo); 
    public TodoDto getTodoById(Integer id) throws Throwable;
    public List<TodoDto> getTodoByUser();
}
