package com.tmsjsb.redpanda.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tmsjsb.redpanda.Entity.TaskEntity;

public interface TaskRepository extends JpaRepository<TaskEntity, String>{
    List<TaskEntity> findByTaskappAcronym(String TaskappAcronym);
}
