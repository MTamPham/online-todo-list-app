package com.tampm.todolist.model;

import lombok.Data;

import javax.persistence.Entity;

@Data
public class TodoStatistic {
    private int all;
    private int complete;
    private int incomplete;
}
