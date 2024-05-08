package com.pentahelix.kanemuraproject.repository;

import com.pentahelix.kanemuraproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

//    mencari token di database
    Optional<User> findFirstByToken(String token);
}
