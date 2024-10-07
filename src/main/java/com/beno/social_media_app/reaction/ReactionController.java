package com.beno.social_media_app.reaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.*;

@RestController
@RequestMapping("/api/reactions")
public class ReactionController {
    @Autowired
    private ReactionService reactionService;

    @GetMapping
    public Page<Reaction> fetch(Pageable pageable) {
        return reactionService.fetch(pageable);
    }

    @GetMapping("/{id}")
    public Reaction fetch(@PathVariable Long id) {
        return reactionService.fetch(id);
    }

    @PostMapping
    public Reaction create(@RequestBody Reaction reaction) throws Exception {
        return reactionService.create(reaction);
    }

    @PutMapping("/{id}")
    public Reaction update(@PathVariable Long id, @RequestBody Reaction reaction) {
        return reactionService.update(id, reaction);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        return reactionService.delete(id);
    }
}
