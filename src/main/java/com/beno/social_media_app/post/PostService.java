package com.beno.social_media_app.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> fetch() {
        return postRepository.findAll();
    }

    public Page<Post> fetch(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Cacheable("Post")
    public Post fetch(Long id) {
        return postRepository.findById(id).orElseThrow();
    }

    public Post create(Post post) throws Exception {
        if (post.getUser() == null) {
            throw new Exception("User didn't found");
        }
        return postRepository.save(post);
    }

    public Post update(Long id, Post post) {
        return postRepository.findById(id).map(resource -> {
            resource.setPostText(post.getPostText());
            return postRepository.save(resource);
        }).orElseThrow();
    }

    public String delete(Long id) {
        Post post = postRepository.findById(id).orElse(null);
        if (post != null) {
            post.setDeleted(true);
            postRepository.save(post);
            return "Post Deleted";
        }
        return "Post didn't found";
    }

}
