package com.hovispace.javacommons.facebooksdk.repository;

import com.hovispace.javacommons.facebooksdk.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
