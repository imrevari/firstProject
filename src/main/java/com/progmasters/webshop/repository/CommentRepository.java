package com.progmasters.webshop.repository;

import com.progmasters.webshop.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByOrderByCreatedAtDesc();

    List<Comment> findByProductId(Long productId);

    void deleteByProductId(Long id);
}
