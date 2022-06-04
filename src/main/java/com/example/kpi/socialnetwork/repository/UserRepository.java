package com.example.kpi.socialnetwork.repository;

import com.example.kpi.socialnetwork.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.posts where u.email =:email")
    User findByEmail(String email);

    Optional<User> findById(Long id);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.likes where u.id =:id")
    Optional<User> findByIdFetchLikes(Long id);
}
