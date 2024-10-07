package com.beno.social_media_app.comment;

import org.hibernate.annotations.CreationTimestamp;

import com.beno.social_media_app.post.Post;
import com.beno.social_media_app.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.experimental.Accessors;
import java.time.Instant;
import lombok.*;

@Entity
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Comment is empty")
    @Column(nullable = false)
    private String commentText;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @JsonIgnoreProperties(value = { "reactions", "comments" })
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties(value = "posts")
    private User user;

    @CreationTimestamp
    Instant createdAt;
}
