package com.beno.social_media_app.reaction;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    boolean existsByUserIdAndPostId(Long userId, Long postId);

    Page<Reaction> findByPost_Id(Long postId, Pageable pageable);
}
