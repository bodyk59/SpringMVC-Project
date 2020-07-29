package com.softserve.academy.repository;

import com.softserve.academy.model.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Long> {
    List<Sprint> findAllByMarathonId(Long id);
}
