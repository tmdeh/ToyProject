package com.fc.toyproeject2.domain.comment.repository;

import com.fc.toyproeject2.domain.comment.model.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTripId(Long tripId);
}

