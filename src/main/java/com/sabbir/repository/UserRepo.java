package com.sabbir.repository;

import com.sabbir.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
    Optional<User> findByUsernameIgnoreCase(String username);

    Page<User> findByCreatedBy(User createdBy, Pageable pageable);

    Page<User> findAll(Pageable pageable);
}
