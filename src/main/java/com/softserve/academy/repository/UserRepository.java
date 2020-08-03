package com.softserve.academy.repository;

import com.softserve.academy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByRoleOrderByFirstNameAscLastNameAsc(User.Role role);
}
