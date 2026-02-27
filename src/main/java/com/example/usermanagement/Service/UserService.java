package com.example.usermanagement.Service;

import com.example.usermanagement.DTO.Request.UserRequestDTO;
import com.example.usermanagement.DTO.Request.UserUpdateRequestDTO;
import com.example.usermanagement.DTO.Response.UserResponDTO;
import com.example.usermanagement.Entity.UserEntity;
import com.example.usermanagement.Repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void addUser(UserRequestDTO request){
        try {
            UserEntity user = new UserEntity();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPhone(request.getPhone());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(request.getRole());
            user.setCreatedAt(new Date());

            userRepository.save(user);

        }catch (Exception e){
            throw new RuntimeException("server error",e);
        }
    }

    public void upDate(UUID id, UserUpdateRequestDTO request){
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("user not found"));
        try{
                user.setUsername(request.getUsername());
                user.setEmail(request.getEmail());
                user.setPhone(request.getPhone());
                user.setRole(request.getRole());
                user.setUpdatedAt(new Date());
                userRepository.save(user);

        }catch (Exception e){
            throw new RuntimeException("server error",e);
        }
    }

    public void deleteUser(UUID id){
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("user not found"));
        try{
            userRepository.delete(user);
        }catch (Exception e){
            throw new RuntimeException("server error",e);
        }
    }

    public List<UserResponDTO> getAllUser(){
        List<UserEntity> user = userRepository.findAll();
        List<UserResponDTO> list = new ArrayList<>();

        for(UserEntity userEntity : user){
            UserResponDTO userResponDTO = new UserResponDTO();
            userResponDTO.setUsername(userEntity.getUsername());
            userResponDTO.setEmail(userEntity.getEmail());
            userResponDTO.setPhone(userEntity.getPhone());
            userResponDTO.setRole(userEntity.getRole());
            list.add(userResponDTO);
        }
        return list;
    }
}
