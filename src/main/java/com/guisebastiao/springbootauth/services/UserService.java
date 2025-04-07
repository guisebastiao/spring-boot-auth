package com.guisebastiao.springbootauth.services;

import com.guisebastiao.springbootauth.dtos.ResponseEntityDTO;
import com.guisebastiao.springbootauth.dtos.UserDTO;
import com.guisebastiao.springbootauth.dtos.UserResponseDTO;
import com.guisebastiao.springbootauth.enuns.RoleEnum;
import com.guisebastiao.springbootauth.models.User;
import com.guisebastiao.springbootauth.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public ResponseEntityDTO findUser(String id) {
        UUID userId = UUID.fromString(id);
        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty()) {
            ResponseEntityDTO response = new ResponseEntityDTO();
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setMessage("User not found");
            response.setSuccess(Boolean.FALSE);
            return response;
        }

        User userPresent = user.get();

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(userPresent.getId());
        userResponseDTO.setUsername(userPresent.getName());
        userResponseDTO.setEmail(userPresent.getEmail());

        ResponseEntityDTO response = new ResponseEntityDTO();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("User found");
        response.setData(userResponseDTO);
        response.setSuccess(Boolean.TRUE);
        return response;
    }

    public ResponseEntityDTO updateUser(String id, UserDTO userDTO) {
        UUID userId = UUID.fromString(id);
        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty()) {
            ResponseEntityDTO response = new ResponseEntityDTO();
            response.setStatus(HttpStatus.NOT_FOUND.value());
            response.setMessage("User not found");
            response.setSuccess(Boolean.FALSE);
            return response;
        }

        User userUpdate = new User();
        userUpdate.setId(userId);
        userUpdate.setEmail(userDTO.getEmail());
        userUpdate.setName(userDTO.getName());
        userUpdate.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userUpdate.setRole(RoleEnum.getRole(userDTO.getRole()));
        userRepository.save(userUpdate);

        ResponseEntityDTO response = new ResponseEntityDTO();
        response.setStatus(HttpStatus.OK.value());
        response.setMessage("User updated");
        response.setSuccess(Boolean.TRUE);

        return response;
    }
}
