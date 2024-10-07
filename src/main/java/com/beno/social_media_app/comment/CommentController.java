package com.beno.social_media_app.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    public Page<Comment> fetch(Pageable pageable) {
        return commentService.fetch(pageable);
    }

    @GetMapping("/post/{id}")
    public Page<Comment> fetch(@PathVariable Long id, Pageable pageable) {
        return commentService.fetchByPostId(id, pageable);
    }

    @GetMapping("/{id}")
    public Comment fetch(@PathVariable Long id) {
        return commentService.fetch(id);
    }

    @PostMapping
    public Comment create(@RequestBody Comment comment) throws Exception {
        return commentService.create(comment);
    }

    @PutMapping("/{id}")
    public Comment update(@PathVariable Long id, @RequestBody Comment comment) {
        return commentService.update(id, comment);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        return commentService.delete(id);
    }
}
