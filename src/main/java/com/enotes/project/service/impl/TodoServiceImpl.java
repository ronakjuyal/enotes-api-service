package com.enotes.project.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.enotes.project.dto.TodoDto;
import com.enotes.project.entity.Todo;
import com.enotes.project.enums.TodoStatus;
import com.enotes.project.exception.ResourceNotFoundException;
import com.enotes.project.repository.TodoRepository;
import com.enotes.project.service.TodoService;
import com.enotes.project.util.Validation;

@Service
public class TodoServiceImpl implements TodoService{
    @Autowired
    private TodoRepository todoRepository;


    @Override
    public Boolean saveTodo(TodoDto todo) {
        
        Validation.todoValidation(todo);
        int statusId = todo.getStatus().getId();
        TodoStatus status=statusId==1?TodoStatus.NOT_STARTED:
                        statusId==2?TodoStatus.IN_PROGRESS:TodoStatus.COMPLETED;
        Todo saveTodo = Todo.builder()
            .title(todo.getTitle())
            .status(status)
            .build();
        if(ObjectUtils.isEmpty(todoRepository.save(saveTodo))){
            return false;
        }
        return true;
    }

    @Override
    public TodoDto getTodoById(Integer id){
        Todo todo = todoRepository.findById(id)
            .orElseThrow(()->new ResourceNotFoundException("invalid todo id"));
        TodoDto todoDto =TodoDto.builder()
            .id(todo.getId())
            .title(todo.getTitle())
            .status(new TodoDto.StatusDto(todo.getStatus().getId(), todo.getStatus().name()))
            .createdBy(todo.getCreatedBy())
            .createdOn(todo.getCreatedOn())
            .updatedBy(todo.getUpdatedBy())
            .updatedOn(todo.getUpdatedOn())
            .build();
        return todoDto;
    }

    @Override
    public List<TodoDto> getTodoByUser() {
        List<Todo> todos = todoRepository.findByCreatedBy(1);
        List<TodoDto> todoDtos = todos.stream().map(todo->{
            TodoDto todoDto = TodoDto.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .createdBy(todo.getCreatedBy())
                .createdOn(todo.getCreatedOn())
                .updatedBy(todo.getUpdatedBy())
                .updatedOn(todo.getUpdatedOn())
                .build();
            todoDto.setStatus(new TodoDto.StatusDto(todo.getStatus().getId(), todo.getStatus().name()));
            return todoDto;
        }).toList();
        return todoDtos;
    }

}
