package com.todo.todotask.model;

import lombok.*;

import java.sql.Date;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TaskRequest extends Task{

  @Builder
  public TaskRequest(@NonNull String task) {
    super(task, new Date(new java.util.Date().getTime()));
  }
}
