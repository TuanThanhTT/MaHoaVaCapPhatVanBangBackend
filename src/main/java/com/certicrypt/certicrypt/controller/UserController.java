package com.certicrypt.certicrypt.controller;


import com.certicrypt.certicrypt.DTO.request.ResetPassDTO;
import com.certicrypt.certicrypt.DTO.request.UserRequest;
import com.certicrypt.certicrypt.DTO.response.MessageChangePass;
import com.certicrypt.certicrypt.DTO.response.UserResponse;
import com.certicrypt.certicrypt.models.User;

import com.certicrypt.certicrypt.service.UserService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/user")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path="/all")
    public ResponseEntity<Page<UserResponse>> getAllUsers(Pageable pageable) {
        Page<UserResponse> users = userService.getAll(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping(path="/search/role/{id}")
    public ResponseEntity<Page<UserResponse>> getUserByRole(@PathVariable Integer id, Pageable pageable) {
        return ResponseEntity.ok(userService.getUserByRole(id, pageable));
    }

    @GetMapping(path="/search/name/{username}")
    public ResponseEntity<Page<UserResponse>> getUsersByUsername(@PathVariable String username, Pageable pageable) {
        return ResponseEntity.ok(userService.getUserbyUsername(username, pageable));
    }

    @PostMapping(path="/add")
    public ResponseEntity<User> addUser(@RequestBody UserRequest user) {
        return  ResponseEntity.ok(userService.addUser(user));
    }

    @PostMapping(path = "/addUserAdmin")
    public ResponseEntity<User> addUserAdmin(@RequestBody UserRequest user) {
        User result = userService.addUserAdmin(user);
        if (result != null) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PutMapping(path="/update")
    public ResponseEntity<User> updateUser(UserRequest user) {
        return  ResponseEntity.ok(userService.updateUser(user));
    }

    @DeleteMapping(path="/delete/{userId}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Integer userId) {
        Boolean result =  userService.deleteUser(userId);
        if(result) {
            return ResponseEntity.ok(true);
        }else {
            return ResponseEntity.ok(false);
        }
    }

    @PutMapping(path="/unlock/{userId}")
    public ResponseEntity<Boolean> unClockUser(@PathVariable Integer userId) {
        return userService.unclockUser(userId)?ResponseEntity.ok(true):ResponseEntity.ok(false);
    }

    @PostMapping(path="/account/reset")
    public ResponseEntity<Boolean> resetPassword(@RequestBody String userName) {
        System.out.println("Có chạy đặt lại mật khẩu");
        Boolean result = userService.resetPassword(userName);
        System.out.println("Kết quả là: "+ result);
        return result?ResponseEntity.ok(true):ResponseEntity.ok(false);
    }

    @PutMapping(path="/change")
    public ResponseEntity<MessageChangePass> changePassword(@RequestBody ResetPassDTO resetPassDTO) {
        MessageChangePass messageChangePass = new MessageChangePass();
        String message = userService.changePassword(resetPassDTO.getUsername(), resetPassDTO.getNewPassword(), resetPassDTO.getOldPassword());
        if(message != "ok") {
            messageChangePass.setSuccess(false);
        }else{
            messageChangePass.setSuccess(true);
        }
        messageChangePass.setMessage(message);
        return ResponseEntity.ok(messageChangePass);
    }


}