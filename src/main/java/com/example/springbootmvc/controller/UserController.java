package com.example.springbootmvc.controller;

import com.example.springbootmvc.dto.UserDto;
import com.example.springbootmvc.model.User;
import com.example.springbootmvc.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        User user = userService.createUser(UserDto.toUser(userDto));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(UserDto.toUserDto(user));
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @Valid @RequestBody UserDto userDto) {
        User user = userService.updateUser(userId, UserDto.toUser(userDto));

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(UserDto.toUserDto(user));
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long userId) {
        User user = userService.getUser(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(UserDto.toUserDto(user));
    }
}
