package com.hovispace.javacommons.springsocial.repository;

import com.hovispace.javacommons.springsocial.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
