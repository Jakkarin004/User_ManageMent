package com.example.usermanagement.Controller;

import com.example.usermanagement.DTO.Request.UserRequestDTO;
import com.example.usermanagement.DTO.Request.UserUpdateRequestDTO;
import com.example.usermanagement.DTO.Response.UserResponDTO;
import com.example.usermanagement.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/new-user")
    public String newUser(@Valid @RequestBody UserRequestDTO request){
        userService.addUser(request);
        return "Add New User Success";
    }

    @PostMapping("/update-user/{id}")
    public String updateUser(@PathVariable UUID id, @Valid @RequestBody UserUpdateRequestDTO request){
        userService.upDate(id,request);
        return "Update User Success";
    }

    @DeleteMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable UUID id){
        userService.deleteUser(id);
        return "Delete User Success";
    }

    @GetMapping("/user-all")
    public List<UserResponDTO> getAllUser(){
        return userService.getAllUser();
    }

    @GetMapping("/user-by-id/{id}")
    public UserResponDTO getUserById(@PathVariable UUID id){
        return userService.getUserById(id);
    }




}
