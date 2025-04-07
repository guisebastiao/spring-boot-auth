package com.guisebastiao.springbootauth.controllers;

import com.guisebastiao.springbootauth.dtos.ResponseEntityDTO;
import com.guisebastiao.springbootauth.dtos.UserDTO;
import com.guisebastiao.springbootauth.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("{id}")
    public ResponseEntity<ResponseEntityDTO> findUser(@PathVariable String id) {
        ResponseEntityDTO responseDTO = userService.findUser(id);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseEntityDTO> updateUser(@PathVariable String id, @RequestBody @Valid UserDTO user) {
        ResponseEntityDTO responseDTO = userService.updateUser(id, user);
        return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
    }

}