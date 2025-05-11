package com.certicrypt.certicrypt.controller;

import com.certicrypt.certicrypt.DTO.request.LoginRequest;
import com.certicrypt.certicrypt.DTO.response.JwtResponse;
import com.certicrypt.certicrypt.models.User;
import com.certicrypt.certicrypt.repository.UserRepository;
import com.certicrypt.certicrypt.util.JwtUtil;

import jakarta.mail.internet.MimeMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/auth")
public class AuthController {

    public final UserRepository userRepository;
    private final   AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    public AuthController(AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, JavaMailSender mailSender, JwtUtil jwtUtil, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;

    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Xác thực người dùng
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            // Tạo token JWT
            String jwt = jwtUtil.generateToken(authentication.getName(), authentication.getAuthorities());

            // Trả về token
            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch (BadCredentialsException e) {
            // Trả về lỗi 401 nếu thông tin đăng nhập sai
            return ResponseEntity.status(401).body("Tên đăng nhập hoặc mật khẩu không đúng");
        } catch (DisabledException e) {
            // Trả về lỗi 401 nếu tài khoản bị vô hiệu hóa
            return ResponseEntity.status(401).body("Tài khoản đã bị vô hiệu hóa");
        } catch (Exception e) {
            // Trả về lỗi 401 cho các lỗi xác thực khác
            return ResponseEntity.status(401).body("Đăng nhập thất bại: " + e.getMessage());
        }
    }

    // Lưu token tạm thời trong bộ nhớ
    private final ConcurrentHashMap<String, TokenInfo> tokenStore = new ConcurrentHashMap<>();

    // Class để lưu thông tin token
    private static class TokenInfo {
        String email;
        LocalDateTime expiry;

        TokenInfo(String email, LocalDateTime expiry) {
            this.email = email;
            this.expiry = expiry;
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required");
        }

        // Kiểm tra định dạng email
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return ResponseEntity.badRequest().body("Invalid email format");
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body("Email not found");
        }

        // Tạo token khôi phục
        String resetToken = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusHours(1); // Hết hạn sau 1 giờ
        tokenStore.put(resetToken, new TokenInfo(email, expiry));

        // Gửi email với liên kết khôi phục
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject("Khôi phục mật khẩu CertiCrypt");
            helper.setText(
                    "<div style='font-family: Arial, sans-serif; padding: 20px; background-color: #f4f4f4;'>"
                            + "<h2 style='color: #1a73e8;'>Khôi phục mật khẩu CertiCrypt</h2>"
                            + "<p>Nhấn vào nút dưới đây để đặt lại mật khẩu:</p>"
                            + "<a href='http://localhost:5173/reset-password?token=" + resetToken + "' "
                            + "style='display: inline-block; padding: 10px 20px; color: white; background-color: #1a73e8; text-decoration: none; border-radius: 5px;'>"
                            + "Đặt lại mật khẩu</a>"
                            + "<p style='color: #555;'>Liên kết này có hiệu lực trong 1 giờ.</p>"
                            + "</div>",
                    true
            );
            mailSender.send(message);
            return ResponseEntity.ok("Password reset email sent");
        } catch (Exception e) {
            tokenStore.remove(resetToken); // Xóa token nếu gửi email thất bại
            return ResponseEntity.status(500).body("Failed to send email: " + e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        if (token == null || newPassword == null) {
            return ResponseEntity.badRequest().body("Token and new password are required");
        }

        // Kiểm tra độ mạnh mật khẩu
        if (newPassword.length() < 8 || !newPassword.matches(".*[A-Z].*") || !newPassword.matches(".*[0-9].*")) {
            return ResponseEntity.badRequest().body("Password must be at least 8 characters, include an uppercase letter and a number");
        }

        // Kiểm tra token
        TokenInfo tokenInfo = tokenStore.get(token);
        if (tokenInfo == null || tokenInfo.expiry.isBefore(LocalDateTime.now())) {
            tokenStore.remove(token); // Xóa token nếu hết hạn
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }

        // Tìm user theo email
        User user = userRepository.findByEmail(tokenInfo.email);
        if (user == null) {
            tokenStore.remove(token);
            return ResponseEntity.status(404).body("User not found");
        }

        // Cập nhật mật khẩu
        user.setPassWord(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Xóa token sau khi sử dụng
        tokenStore.remove(token);

        return ResponseEntity.ok("Password reset successful");
    }

}
