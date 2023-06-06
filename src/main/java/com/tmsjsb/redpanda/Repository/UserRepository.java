package com.tmsjsb.redpanda.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tmsjsb.redpanda.Entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String> {

}
