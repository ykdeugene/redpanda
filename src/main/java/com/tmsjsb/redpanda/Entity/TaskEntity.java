package com.tmsjsb.redpanda.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "task")
public class TaskEntity {
    @Id
    @JsonProperty("Task_id")
    @Column(name = "task_id")
    private String Task_id;

    @JsonProperty("Task_name")
    @Column(name = "task_name")
    private String Task_name;

    @JsonProperty("Task_description")
    @Column(name = "task_description")
    private String Task_description;

    @JsonProperty("Task_notes")
    @Column(name = "task_notes")
    private String Task_notes;

    @JsonProperty("Task_plan")
    @Column(name = "task_plan")
    private String Task_plan;

    @JsonProperty("Task_app_Acronym")
    @Column(name = "task_app_acronym")
    private String Task_app_Acronym;

    @JsonProperty("Task_state")
    @Column(name = "task_state")
    private String Task_state;

    @JsonProperty("Task_creator")
    @Column(name = "task_creator")
    private String Task_creator;

    @JsonProperty("Task_owner")
    @Column(name = "task_owner")
    private String Task_owner;
    
    @JsonProperty("Task_createDate")
    @Column(name = "task_createdate")
    private String Task_createDate;
}
