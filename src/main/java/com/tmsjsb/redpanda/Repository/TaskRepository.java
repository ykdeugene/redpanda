package com.tmsjsb.redpanda.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tmsjsb.redpanda.Entity.TaskEntity;

public interface TaskRepository extends JpaRepository<TaskEntity, String>{
    
}
