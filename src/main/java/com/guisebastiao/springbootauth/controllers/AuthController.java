package com.guisebastiao.springbootauth.controllers;

import com.guisebastiao.springbootauth.dtos.LoginDTO;
import com.guisebastiao.springbootauth.dtos.ResponseEntityDTO;
import com.guisebastiao.springbootauth.dtos.UserDTO;
import com.guisebastiao.springbootauth.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ResponseEntityDTO> login(HttpServletResponse response, @RequestBody @Valid LoginDTO loginDTO) {
        ResponseEntityDTO responseDTO = this.authService.login(response, loginDTO);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseEntityDTO> register(HttpServletResponse response, @RequestBody @Valid UserDTO userDTO) {
        ResponseEntityDTO responseDTO = this.authService.register(response, userDTO);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }
}