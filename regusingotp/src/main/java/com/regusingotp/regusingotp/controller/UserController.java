package com.regusingotp.regusingotp.controller;

import com.regusingotp.regusingotp.CommonResponse;
import com.regusingotp.regusingotp.dto.LoginDto;
import com.regusingotp.regusingotp.dto.RegisterDto;
import com.regusingotp.regusingotp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
@Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<CommonResponse> register(@RequestBody RegisterDto registerDto){
        return new ResponseEntity<>(userService.register(registerDto), HttpStatus.OK);

        }
    @PutMapping("/verify-account")
    public ResponseEntity<CommonResponse> verifyAccount(@RequestParam String email,
                                                @RequestParam String otp) {
        return new ResponseEntity<>(userService.verifyAccount(email, otp), HttpStatus.OK);
    }
    @PutMapping("/regenerate-otp")
    public ResponseEntity<CommonResponse> regenerateOtp(@RequestParam String email) {
        return new ResponseEntity<>(userService.regenerateOtp(email), HttpStatus.OK);
    }
    @PutMapping("/login")
    public ResponseEntity<CommonResponse> login(@RequestBody LoginDto loginDto) {
        return new ResponseEntity<>(userService.login(loginDto), HttpStatus.OK);
    }
}


