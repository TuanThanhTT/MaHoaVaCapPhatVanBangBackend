package com.certicrypt.certicrypt.service.impl;

import com.certicrypt.certicrypt.models.User;
import com.certicrypt.certicrypt.repository.UserRepository;
import com.certicrypt.certicrypt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl  implements UserService {
    @Autowired
    private UserRepository userRepository;


    @Override
    public Page<User> getAll(Pageable pageable) {
       return userRepository.findByIsLockedFalse(pageable);
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }
}
