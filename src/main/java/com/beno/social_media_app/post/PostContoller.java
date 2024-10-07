package com.beno.social_media_app.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.*;

@RestController
@RequestMapping("/api/posts")
public class PostContoller {
    @Autowired
    private PostService postService;

    @GetMapping
    public Page<Post> fetch(Pageable pageable) {
        return postService.fetch(pageable);
    }

    @GetMapping("/{id}")
    public Post fetch(@PathVariable Long id) {
        return postService.fetch(id);
    }

    @PostMapping
    public Post create(@RequestBody Post post) throws Exception {
        return postService.create(post);
    }

    @PutMapping("/{id}")
    public Post update(@PathVariable Long id, @RequestBody Post post) {
        return postService.update(id, post);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        return postService.delete(id);
    }
}
