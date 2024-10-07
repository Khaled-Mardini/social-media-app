package com.beno.social_media_app.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> fetch() {
        return userRepository.findAll();
    }

    public Page<User> fetch(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Cacheable("User")
    public User fetch(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public User update(Long id, User user) {
        return userRepository.findById(id).map(resource -> {
            if (user.getFirstname() != null)
                resource.setFirstname(user.getFirstname());
            if (user.getLastname() != null)
                resource.setLastname(user.getLastname());
            return userRepository.save(resource);
        }).orElseThrow();
    }

    public String delete(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return "User Deleted";
        }
        return "User didn't found";
    }
}
