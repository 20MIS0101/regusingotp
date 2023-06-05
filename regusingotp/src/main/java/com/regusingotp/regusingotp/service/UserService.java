package com.regusingotp.regusingotp.service;
import com.regusingotp.regusingotp.CommonResponse;
import com.regusingotp.regusingotp.dto.LoginDto;
import com.regusingotp.regusingotp.dto.RegisterDto;
import com.regusingotp.regusingotp.entity.User;
import com.regusingotp.regusingotp.repository.UserRepository;
import com.regusingotp.regusingotp.util.EmailUtil;
import com.regusingotp.regusingotp.util.OtpUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class UserService {

    @Autowired
    private OtpUtil otpUtil;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private UserRepository userRepository;

    public CommonResponse register(RegisterDto registerDto) {
        String otp = otpUtil.generateOtp();
        CommonResponse reg = new CommonResponse();
        try {
            emailUtil.sendOtpEmail(registerDto.getEmail(), otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp Please try again");
        }
        User user = new User();
        user.setName(registerDto.getName());
        user.setEmail(registerDto.getEmail());
        user.setPassword(registerDto.getPassword());
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        userRepository.save(user);
        reg.setMessage("User registration successful");
        reg.setStatus(true);
        return reg;

    }

    public CommonResponse verifyAccount(String email, String otp) {
        CommonResponse ver = new CommonResponse();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        if (user.getOtp().equals(otp) && Duration.between(user.getOtpGeneratedTime(),
                LocalDateTime.now()).getSeconds() < (1 * 60)) {
            user.setActive(true);
            userRepository.save(user);
            ver.setMessage("OTP verified you can login");
            ver.setStatus(true);
            return ver;
        }
        ver.setStatus(true);
        ver.setMessage("Please regenerate otp and try again");
        return ver;
    }

    public CommonResponse regenerateOtp(String email) {
        CommonResponse reg = new CommonResponse();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(email, otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }
        user.setOtp(otp);
        user.setOtpGeneratedTime(LocalDateTime.now());
        userRepository.save(user);
        reg.setMessage("Email sent... please verify account within 3 minute");
        reg.setStatus(true);
        return reg;
    }

    public CommonResponse login(LoginDto loginDto) {
        CommonResponse logi = new CommonResponse();
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(
                        () -> new RuntimeException("User not found with this email: " + loginDto.getEmail()));
        if (!loginDto.getPassword().equals(user.getPassword())) {
            logi.setMessage("Password is incorrect");
            return logi;
        } else if (!user.isActive()) {
            logi.setMessage("your account is not verified");
            return logi ;
        }
        logi.setMessage("Login successful");
        logi.setStatus(true);
        return logi;
    }
}