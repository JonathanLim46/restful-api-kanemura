package com.pentahelix.kanemuraproject.repository;

import com.pentahelix.kanemuraproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FindUserRepository extends JpaRepository<User, String> {

    Optional<User> findFirstByUsername(String username);

    Page<User> findAll(Specification<User> specification, Pageable pageable);
}
