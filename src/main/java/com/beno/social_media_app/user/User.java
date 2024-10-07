package com.beno.social_media_app.user;

import com.beno.social_media_app.comment.Comment;
import com.beno.social_media_app.post.Post;
import com.beno.social_media_app.reaction.Reaction;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.experimental.Accessors;
import java.time.Instant;
import java.util.*;
import lombok.*;

@Entity
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "First Name is empty")
    @Column(nullable = false)
    private String firstname;

    @NotEmpty(message = "Last Name is empty")
    @Column(nullable = false)
    private String lastname;

    @NotEmpty(message = "Email is empty")
    @Column(nullable = false, unique = true)
    private String email;

    @NotEmpty(message = "Password is empty")
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "reactions", "comments" })
    private List<Post> posts;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Reaction> reactions;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Comment> comments;

    @CreationTimestamp
    Instant createdAt;
}
