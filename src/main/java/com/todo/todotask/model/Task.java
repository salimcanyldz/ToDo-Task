package com.todo.todotask.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@Data
@RequiredArgsConstructor
@Entity
@Table(name="task")
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NonNull
    private String task;

    @Column(name = "insert_date")
    @NonNull
    private Date addedDate;
}
