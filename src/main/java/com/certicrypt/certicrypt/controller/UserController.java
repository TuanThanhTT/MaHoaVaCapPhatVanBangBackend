package com.certicrypt.certicrypt.controller;


import com.certicrypt.certicrypt.models.User;
import com.certicrypt.certicrypt.repository.UserRepository;
import com.certicrypt.certicrypt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path="/all")
    public ResponseEntity<Page<User>> getAllUsers(Pageable pageable) {
        Page<User> users = userService.getAll(pageable);
        return ResponseEntity.ok(users);
    }

    @PostMapping(path="/add")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        return  ResponseEntity.ok(userService.addUser(user));
    }

}
