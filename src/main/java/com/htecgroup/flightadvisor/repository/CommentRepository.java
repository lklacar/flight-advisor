package com.htecgroup.flightadvisor.repository;

import com.htecgroup.flightadvisor.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndAuthorId(Long id, Long authorId);

}


