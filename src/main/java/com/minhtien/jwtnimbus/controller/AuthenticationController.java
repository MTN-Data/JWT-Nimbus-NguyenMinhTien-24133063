package com.minhtien.jwtnimbus.controller;

import com.minhtien.jwtnimbus.entity.User;
import com.minhtien.jwtnimbus.model.LoginResponse;
import com.minhtien.jwtnimbus.model.LoginUserModel;
import com.minhtien.jwtnimbus.model.RegisterUserModel;
import com.minhtien.jwtnimbus.service.AuthenticationService;
import com.minhtien.jwtnimbus.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService,
                                    AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody RegisterUserModel registerUser) {
        try {
            return ResponseEntity.ok(authenticationService.signup(registerUser));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserModel loginUser) {
        User authenticatedUser = authenticationService.authenticate(loginUser);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        return ResponseEntity.ok(new LoginResponse(jwtToken, jwtService.getExpirationTime()));
    }
}
