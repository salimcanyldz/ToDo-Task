package com.todo.todotask.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.todo.todotask.db.TaskRepository;
import com.todo.todotask.model.Task;
import com.todo.todotask.model.TaskRequest;
import java.sql.Date;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ToDoRestAPI {

  @Autowired private TaskRepository taskRepository;

  @GetMapping("/task")
  public ResponseEntity<List<Task>> getTasks() {
    return new ResponseEntity<>(taskRepository.findAll(), HttpStatus.OK);
  }

  @GetMapping("/task/{id}")
  public ResponseEntity<String> getSpecificTask(@PathVariable int id) {
    return taskRepository
        .findById(id)
        .map(
            task -> {
              try {
                return new ResponseEntity<>(
                    new ObjectMapper().writeValueAsString(task), HttpStatus.OK);
              } catch (Exception e) {
                return new ResponseEntity<>(
                    "{\"status\": \"Internal Server Error\"}", HttpStatus.INTERNAL_SERVER_ERROR);
              }
            })
        .orElseGet(
            () -> {
              return new ResponseEntity<>("{\"status\": \"Not Found\"}", HttpStatus.NOT_FOUND);
            });
  }

  @PostMapping("/task")
  @Transactional
  public ResponseEntity<String> addNewTask(@RequestBody TaskRequest taskInfo)
      throws JsonProcessingException {
    Task t = new Task(taskInfo.getTask(), new Date(new java.util.Date().getTime()));
    taskRepository.save(t);
    return new ResponseEntity<>(new ObjectMapper().writeValueAsString(t), HttpStatus.OK);
  }

  @PutMapping("/task/{id}")
  @Transactional
  public ResponseEntity<String> updateTask(
      @PathVariable int id, @RequestBody TaskRequest updatedTask) {
    return taskRepository
        .findById(id)
        .map(
            task -> {
              task.setTask(updatedTask.getTask());
              task.setAddedDate(new Date(new java.util.Date().getTime()));
              taskRepository.save(task);
              try {
                return new ResponseEntity<>(
                    new ObjectMapper().writeValueAsString(task), HttpStatus.OK);
              } catch (JsonProcessingException e) {
                e.printStackTrace();
                return new ResponseEntity<>(
                    "{\"status\": \"Internal Server Error\"}", HttpStatus.INTERNAL_SERVER_ERROR);
              }
            })
        .orElseGet(
            () -> {
              return new ResponseEntity<>("{\"status\": \"Not Found\"}", HttpStatus.NOT_FOUND);
            });
  }

  @DeleteMapping("/task/{id}")
  public ResponseEntity<String> deleteTask(@PathVariable int id) {
    taskRepository.deleteById(id);
    return new ResponseEntity<>("{\"task\":" + id + ", \"status\": \"deleted\"}", HttpStatus.OK);
  }
}
