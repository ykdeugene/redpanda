package com.tmsjsb.redpanda.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tmsjsb.redpanda.Entity.AppEntity;

public interface AppRepository extends JpaRepository<AppEntity, String> {
  
}
