package com.beno.social_media_app.reaction;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beno.social_media_app.post.PostRepository;
import com.beno.social_media_app.post.Post;

import org.springframework.data.domain.*;

@Service
public class ReactionService {
    @Autowired
    private ReactionRepository reactionRepository;
    @Autowired
    private PostRepository postRepository;

    public List<Reaction> fetch() {
        return reactionRepository.findAll();
    }

    public Page<Reaction> fetch(Pageable pageable) {
        return reactionRepository.findAll(pageable);
    }

    @Cacheable("Reaction")
    public Reaction fetch(Long id) {
        return reactionRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Reaction create(Reaction reaction) throws Exception {
        if (reaction.getPost() == null) {
            throw new Exception("Post didn't found");
        }
        if (reaction.getUser() == null) {
            throw new Exception("User didn't found");
        }
        if (reactionRepository.existsByUserIdAndPostId(reaction.getPost().getId(), reaction.getUser().getId())) {
            throw new Exception("Reaction Already Exists");
        }
        Post post = postRepository.findById(reaction.getPost().getId()).get();
        post.setReactionsCnt(post.getReactionsCnt() + 1);
        postRepository.save(post);
        return reactionRepository.save(reaction);
    }

    public Reaction update(Long id, Reaction reaction) {
        return reactionRepository.findById(id).map(resource -> {
            resource.setType(reaction.getType());
            return reactionRepository.save(resource);
        }).orElseThrow();
    }

    @Transactional
    public String delete(Long id) {
        Reaction reaction = reactionRepository.findById(id).orElseThrow();
        Post post = postRepository.findById(reaction.getPost().getId()).get();
        post.setReactionsCnt(post.getReactionsCnt() - 1);
        postRepository.save(post);
        reactionRepository.deleteById(id);
        return "Reaction Deleted";
    }
}
