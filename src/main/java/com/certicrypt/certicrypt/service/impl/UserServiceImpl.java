package com.certicrypt.certicrypt.service.impl;

import com.certicrypt.certicrypt.DTO.request.UserRequest;
import com.certicrypt.certicrypt.DTO.response.UserResponse;
import com.certicrypt.certicrypt.mapper.UserMapper;
import com.certicrypt.certicrypt.models.Role;
import com.certicrypt.certicrypt.models.User;
import com.certicrypt.certicrypt.repository.RoleRepository;
import com.certicrypt.certicrypt.repository.UserRepository;
import com.certicrypt.certicrypt.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class UserServiceImpl  implements UserService {


    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Page<UserResponse> getAll(Pageable pageable) {

        Page<User> users =  userRepository.findAll(pageable);
        return users.map(UserMapper::toDTO);
    }

    @Override
    public Page<UserResponse> getUserByRole(Integer roleId, Pageable pageable) {
        try{
            Role role = roleRepository.findById(roleId).orElse(null);
            if(role == null){
                return null;
            }
            Page<User> users = userRepository.findByRole_RoleIdAndIsLockedFalse(roleId, pageable);
            return users.map(UserMapper::toDTO);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Page<UserResponse> getUserbyUsername(String username, Pageable pageable) {
       try{
            if(username == null || username.isEmpty()){
                throw  new IllegalArgumentException("Username is null or empty");
            }

            Page<User> users = userRepository.searchActiveUsersByUsernameIgnoreCaseAndAccent(username, pageable);

            return users.map(UserMapper::toDTO);
       }catch (Exception e){
           logger.error(e.getMessage());
       }
       return null;
    }

    @Override
    public User updateUser(UserRequest userRequest) {
        try{
            if(userRequest == null || userRequest.getUserId() == null ||
            userRequest.getUserName() == null || userRequest.getUserName().isEmpty()
            || userRequest.getPassWord() == null || userRequest.getPassWord().isEmpty()){
                throw  new IllegalArgumentException("Username or Password is null or empty");
            }
            User updateUser = userRepository.findById(userRequest.getUserId()).orElse(null);
            if(updateUser == null){
                throw  new IllegalArgumentException("User not found");
            }
            updateUser.setUserName(userRequest.getUserName());
            updateUser.setPassWord(userRequest.getPassWord());
            return userRepository.save(updateUser);

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public Boolean deleteUser(Integer userId) {
       try{
           if(userId == null){
               throw  new IllegalArgumentException("Userid is null");
           }
           User deleteUser = userRepository.findById(userId).orElse(null);
           if(deleteUser == null){
               return false;
           }
           deleteUser.setIsLocked(true);
           userRepository.save(deleteUser);
           return true;

       }catch (Exception e){
           logger.error(e.getMessage());
       }
       return false;
    }

    @Override
    public Boolean unclockUser(Integer userId) {
        try{

            if(userId == null) {
                throw new IllegalArgumentException("Userid is null");
            }
            User unclockUser = userRepository.findById(userId).orElse(null);
            if(unclockUser == null){
                return false;
            }

            unclockUser.setIsLocked(false);
            userRepository.save(unclockUser);
            return true;

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return false;
    }

    @Override
    public User addUserAdmin(UserRequest userRequest) {
        try{
            if(userRequest == null || userRequest.getEmail() == null || userRequest.getEmail().isEmpty()||
            userRequest.getRole() == null){
                throw  new IllegalArgumentException("Username or Password is null or empty");
            }
            User newUser = new User();
            newUser.setUserName(userRequest.getEmail());
            //newUser.setPassWord(userRequest.getEmail());
            newUser.setPassWord(passwordEncoder.encode(userRequest.getEmail()));
            newUser.setEmail(userRequest.getEmail());
            Role role = roleRepository.findById(userRequest.getRole()).orElse(null);
            if(role == null){
                return null;
            }
            newUser.setRole(role);
            newUser.setIsLocked(false);
            newUser.setLastLogin(LocalDateTime.now());
            return userRepository.save(newUser);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        try{
            if(username == null || username.isEmpty()){
                logger.warn("Username is null or empty");
                return null;
            }
            return userRepository.findByEmail(username);
        }catch (Exception e){
            logger.error(e.getMessage());
            throw  new IllegalArgumentException("Username is null");
        }

    }

    @Override
    public Boolean resetPassword(String username) {
        try{

            if(username == null || username.isEmpty()){
                throw new IOException("username rỗng");
            }
            User user = userRepository.findByUserName(username.trim());
            if(user == null){
                System.out.println("User null username la: "+ username);
                return false;
            }
            user.setPassWord(passwordEncoder.encode(user.getUserName()));
            userRepository.save(user);
            return true;

        }catch (Exception ex){
            throw new RuntimeException("Có lỗi xảy ra!");
        }
    }

    @Override
    public String changePassword(String userName, String password, String oldPassword) {
        try{

            if(userName == null || userName.isEmpty() || password == null || password.isEmpty()
            || oldPassword == null || oldPassword.isEmpty()){
                throw  new IOException("Thông tin rỗng");
            }

            User user = userRepository.findByUserName(userName);
            if(user == null){

                return "Không tim thấy tài khoản!";
            }
            if(!passwordEncoder.matches(oldPassword, user.getPassWord())){
                return "Mật khẩu không chính xác!";
            }

            user.setPassWord(passwordEncoder.encode(password));
            userRepository.save(user);
            return "ok";
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return "Lỗi thực hiện!";
    }

    @Override
    public User addUser(UserRequest user) {
        try {
            if (user == null || user.getUserName() == null || user.getUserName().isEmpty()
                    || user.getEmail() == null || user.getEmail().isEmpty()
                    || user.getPassWord() == null || user.getPassWord().isEmpty()) {
                return null;
            }

            User newUser = new User();
            newUser.setUserName(user.getUserName());
            newUser.setEmail(user.getEmail());
            newUser.setPassWord(passwordEncoder.encode(user.getPassWord()));
            newUser.setIsLocked(false);


            Role role = roleRepository.findById(user.getRole())
                    .orElseThrow(() -> new RuntimeException("Role không tồn tại"));
            newUser.setRole(role);

            newUser.setLastLogin(user.getLastLogin());

            return userRepository.save(newUser);
        } catch (Exception e) {
            logger.error("Lỗi khi thêm người dùng: {}", e.getMessage(), e);
            return null;
        }

    }

}
