package com.example.toy_alarm.plan.repo;

import com.example.toy_alarm.plan.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanRepo extends JpaRepository<Plan, Long> {
    boolean existsByPlanCode(String planCode);

    Optional<Plan> findByPlanCode(String planCode);
}
