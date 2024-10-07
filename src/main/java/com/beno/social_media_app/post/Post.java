package com.beno.social_media_app.post;

import org.hibernate.annotations.CreationTimestamp;

import com.beno.social_media_app.comment.Comment;
import com.beno.social_media_app.reaction.Reaction;
import com.beno.social_media_app.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.experimental.Accessors;
import java.time.Instant;
import java.util.List;
import lombok.*;

@Entity
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private int reactionsCnt = 0;

    @Builder.Default
    private int commentsCnt = 0;

    @NotEmpty(message = "Post is empty")
    @Column(nullable = false)
    private String postText;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties(value = "posts")
    private User user;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = "post")
    private List<Reaction> reactions;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = "post")
    private List<Comment> comments;

    @CreationTimestamp
    Instant createdAt;
}
