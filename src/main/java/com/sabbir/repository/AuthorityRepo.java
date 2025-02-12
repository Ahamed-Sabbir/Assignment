package com.sabbir.repository;

import com.sabbir.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthorityRepo extends JpaRepository<Authority, UUID> {
    Authority findByAuthority(String authority);
}
