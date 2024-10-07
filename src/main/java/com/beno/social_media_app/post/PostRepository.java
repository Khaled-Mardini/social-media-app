package com.beno.social_media_app.post;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByIsDeletedFalse(Pageable pageable);

    Optional<Post> findByIdAndIsDeletedFalse(Long id);
}
