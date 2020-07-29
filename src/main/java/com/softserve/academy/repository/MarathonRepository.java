package com.softserve.academy.repository;

import com.softserve.academy.model.Marathon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarathonRepository extends JpaRepository<Marathon, Long> {
}
