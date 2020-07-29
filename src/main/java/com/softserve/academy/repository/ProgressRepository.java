package com.softserve.academy.repository;

import com.softserve.academy.model.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {

    @Query("SELECT p FROM Progress p WHERE p.user.id = ?1 AND p.task.sprint.marathon.id = ?2")
    List<Progress> findAllByUserIdAndMarathonId(Long userId, Long marathonId);

    @Query("SELECT p FROM Progress p WHERE p.user.id = ?1 AND p.task.sprint.id = ?2")
    List<Progress> findAllProgressByUserIdAndSprintId(Long userId, Long sprintId);
}
