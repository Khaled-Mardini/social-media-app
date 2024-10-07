package com.beno.social_media_app.reaction;

import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.*;
import lombok.experimental.Accessors;
import java.time.Instant;
import lombok.*;
import com.beno.social_media_app.post.Post;
import com.beno.social_media_app.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private int type = 0;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties(value = "posts")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    @JsonIgnoreProperties(value = { "reactions", "comments" })
    private Post post;

    @CreationTimestamp
    Instant createdAt;
}
