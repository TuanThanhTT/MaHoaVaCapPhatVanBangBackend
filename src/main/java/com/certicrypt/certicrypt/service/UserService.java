package com.certicrypt.certicrypt.service;

import com.certicrypt.certicrypt.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<User> getAll(Pageable pageable);;
    User addUser(User user);

}
