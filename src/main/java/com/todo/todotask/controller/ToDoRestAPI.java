package com.todo.todotask.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.todotask.db.TaskRepository;
import com.todo.todotask.model.Task;
import com.todo.todotask.model.TaskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
public class ToDoRestAPI {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/")
    public ResponseEntity<List<Task>> getTasks(){
        return new ResponseEntity<>(taskRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping("/new-task")
    public ResponseEntity<String> addNewTask(@RequestBody TaskRequest taskInfo) throws JsonProcessingException {
        Task t = new Task(taskInfo.getTask(), new Date(new java.util.Date().getTime()));
        taskRepository.save(t);
        taskRepository.flush();
        return new ResponseEntity<>(new ObjectMapper().writeValueAsString(t),HttpStatus.OK);
    }

    @PutMapping("/update-task/{id}")
    public ResponseEntity<String> updateTask(@PathVariable int id, @RequestBody TaskRequest updatedTask){
        return taskRepository.findById(id).map(task -> {
            task.setTask(updatedTask.getTask());
            task.setAddedDate(new Date(new java.util.Date().getTime()));
            taskRepository.save(task);
            taskRepository.flush();
            try {
                return new ResponseEntity<>(new ObjectMapper().writeValueAsString(task),HttpStatus.OK);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return new ResponseEntity<>("Internal Server Error.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }).orElseGet(() -> {
            return new ResponseEntity<>("Task Not Found.", HttpStatus.NOT_FOUND);
        });
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable int id){
        taskRepository.deleteById(id);
        return new ResponseEntity<>("{\"task\":" + id + ", \"status\": \"deleted\"}", HttpStatus.OK);
    }
}
