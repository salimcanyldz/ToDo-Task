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

  @GetMapping("task/{id}")
  public ResponseEntity<String> getTest(@PathVariable int id) throws JsonProcessingException {
      Task t = taskRepository.findTaskByID(id);
      if (t == null){
          return new ResponseEntity<>("null", HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(new ObjectMapper().writeValueAsString(t), HttpStatus.OK);
  }

  @PostMapping("/task")
  @Transactional
  public ResponseEntity<String> addNewTask(@RequestBody TaskRequest taskInfo)
      throws JsonProcessingException {
    Task t = new Task(taskInfo.getTask(), new Date(new java.util.Date().getTime()));
    taskRepository.save(t);
    return new ResponseEntity<>(new ObjectMapper().writeValueAsString(t), HttpStatus.OK);
  }

  @PutMapping("/test/{id}")
  @Transactional
  public ResponseEntity<String> testPut(@PathVariable int id,
                                        @RequestBody TaskRequest update)
          throws JsonProcessingException {

      Task t = taskRepository.findTaskByID(id);
      if (t == null){
          return new ResponseEntity<>("null", HttpStatus.NOT_FOUND);
      }
      else{
          t.setTask(update.getTask());
          t.setAddedDate(new Date(new java.util.Date().getTime()));
          taskRepository.save(t);
      }
      return new ResponseEntity<>(new ObjectMapper().writeValueAsString(t), HttpStatus.OK);
  }

  @DeleteMapping("/task/{id}")
  public ResponseEntity<String> deleteTask(@PathVariable int id) {
    taskRepository.deleteById(id);
    return new ResponseEntity<>("", HttpStatus.OK);
  }
}
