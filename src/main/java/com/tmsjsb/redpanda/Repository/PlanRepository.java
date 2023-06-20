package com.tmsjsb.redpanda.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tmsjsb.redpanda.Entity.PlanEntity;
import com.tmsjsb.redpanda.Entity.PlanKeyEntity;

public interface PlanRepository extends JpaRepository<PlanEntity, PlanKeyEntity> {

  List<PlanEntity> findByPlanappAcronym(String PlanappAcronym);

  List<PlanEntity> findByPlanappAcronymAndPlanMVPname(String planappAcronym, String planMVPname);

}
