package com.example.kpi.socialnetwork.repository;

import com.example.kpi.socialnetwork.model.Post;
import com.example.kpi.socialnetwork.model.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByIdDesc();

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.likes where p.id =:id")
    Optional<Post> findById(Long id);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.comments where p.id =:id")
    Optional<Post> findByIdFetchComments(Long id);
}
