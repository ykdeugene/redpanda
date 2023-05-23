package com.tmsjsb.redpanda.Repository;

import org.springframework.data.repository.CrudRepository;

import com.tmsjsb.redpanda.Entity.SampleEntity;


public interface SampleRepository extends CrudRepository<SampleEntity, Integer>{
  
}
