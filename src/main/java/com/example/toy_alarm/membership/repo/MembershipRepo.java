package com.example.toy_alarm.membership.repo;

import com.example.toy_alarm.membership.entity.Membership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface MembershipRepo extends JpaRepository<Membership, Long> {

    void findByPlanCode(String planCode);

    List<Membership> findAllByAppUserId(Long appUserId);

    Optional<Membership> findByPlanId(Long planId);

    @Query("select m from Membership m join fetch m.appUser where m.plan.id = :planId")
    List<Membership> findAllByPlanId(Long planId);
}
