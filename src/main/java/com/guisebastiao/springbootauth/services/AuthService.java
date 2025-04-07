package com.guisebastiao.springbootauth.services;

import com.guisebastiao.springbootauth.dtos.LoginDTO;
import com.guisebastiao.springbootauth.dtos.ResponseEntityDTO;
import com.guisebastiao.springbootauth.dtos.UserDTO;
import com.guisebastiao.springbootauth.enuns.RoleEnum;
import com.guisebastiao.springbootauth.exceptions.DuplicateEntityException;
import com.guisebastiao.springbootauth.exceptions.EntityNotFoundException;
import com.guisebastiao.springbootauth.exceptions.RequiredAuthenticationException;
import com.guisebastiao.springbootauth.models.User;
import com.guisebastiao.springbootauth.repositories.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${session.expiration.time}")
    private String durationToken;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public ResponseEntityDTO login(HttpServletResponse response, LoginDTO loginDTO) {
        Optional<User> user = this.userRepository.findByEmail(loginDTO.getEmail());

        if (user.isEmpty()) {
            throw new EntityNotFoundException("Account not already exists");
        }

        if (!passwordEncoder.matches(loginDTO.getPassword(), user.get().getPassword())) {
            throw new RequiredAuthenticationException("Invalid credentials");
        }

        String token = this.tokenService.generateToken(user.get());

        this.generateCookie(response, token);

        ResponseEntityDTO responseDTO = new ResponseEntityDTO();
        responseDTO.setStatus(HttpStatus.OK.value());
        responseDTO.setMessage("Login Successful");
        responseDTO.setSuccess(Boolean.TRUE);

        return responseDTO;
    }

    public ResponseEntityDTO register(HttpServletResponse response, UserDTO userDTO) {
        Optional<User> existUser = this.userRepository.findByEmail(userDTO.getEmail());

        if (existUser.isPresent()) {
            throw new DuplicateEntityException("Account already exists");
        }

        User user = new User();

        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(RoleEnum.getRole(userDTO.getRole()));

        this.userRepository.save(user);

        String token = this.tokenService.generateToken(user);

        this.generateCookie(response, token);

        ResponseEntityDTO responseDTO = new ResponseEntityDTO();

        responseDTO.setStatus(HttpStatus.CREATED.value());
        responseDTO.setMessage("Registration Successful");
        responseDTO.setSuccess(Boolean.TRUE);

        return responseDTO;
    }

    private void generateCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("Authenticate", token);

        cookie.setPath("/");
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setMaxAge(60 * 60 * Integer.parseInt(durationToken));

        response.addCookie(cookie);
    }
}