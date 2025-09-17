package com.enotes.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enotes.project.dto.TodoDto;
import com.enotes.project.service.TodoService;

@RestController
@RequestMapping("/api/v1/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @PostMapping("/save")
    public ResponseEntity<?> saveTodo(@RequestBody TodoDto todo){
        todoService.saveTodo(todo);
        return ResponseEntity.ok("todo saved");
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getTodoById(@PathVariable Integer id) throws Throwable{
        TodoDto todo = todoService.getTodoById(id);
        return ResponseEntity.ok(todo);
    }
    @GetMapping("/user")
    public ResponseEntity<?> getTodoByUser(){
        return ResponseEntity.ok(todoService.getTodoByUser());
    }
}
