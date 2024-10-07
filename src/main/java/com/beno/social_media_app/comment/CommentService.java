package com.beno.social_media_app.comment;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beno.social_media_app.post.Post;
import com.beno.social_media_app.post.PostRepository;

import org.springframework.data.domain.*;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;

    List<Comment> fetch() {
        return commentRepository.findAll();
    }

    public Page<Comment> fetch(Pageable pageable) {
        return commentRepository.findAll(pageable);
    }

    @Cacheable("Comment")
    public Comment fetch(Long id) {
        return commentRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Comment create(Comment comment) throws Exception {
        if (comment.getPost() == null) {
            throw new Exception("Post didn't found");
        }
        if (comment.getUser() == null) {
            throw new Exception("User didn't found");
        }
        Post post = postRepository.findById(comment.getPost().getId()).get();
        post.setCommentsCnt(post.getCommentsCnt() + 1);
        postRepository.save(post);
        return commentRepository.save(comment);
    }

    public Comment update(Long id, Comment comment) {
        return commentRepository.findById(id).map(resource -> {
            resource.setCommentText(comment.getCommentText());
            return commentRepository.save(resource);
        }).orElseThrow();
    }

    @Transactional
    public String delete(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow();
        Post post = postRepository.findById(comment.getPost().getId()).get();
        post.setCommentsCnt(post.getCommentsCnt() - 1);
        postRepository.save(post);
        commentRepository.deleteById(id);
        return "Comment Deleted";
    }
}
