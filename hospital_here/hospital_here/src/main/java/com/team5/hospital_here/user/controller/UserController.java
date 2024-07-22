package com.team5.hospital_here.user.controller;

import com.team5.hospital_here.common.exception.CustomException;
import com.team5.hospital_here.common.exception.ErrorCode;
import com.team5.hospital_here.common.jwt.JwtUtil;
import com.team5.hospital_here.user.entity.Role;
import com.team5.hospital_here.user.entity.User;
import com.team5.hospital_here.user.entity.UserDTO;
import com.team5.hospital_here.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;


    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    /*@GetMapping("/")
    public ResponseEntity<List<UserDTO>> getAllUsers(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.getUsernameFromToken(token.substring(7));
        UserDTO user = userService.findUser(email);
        if (user.getRole() != Role.ADMIN)
        {
            throw new CustomException(ErrorCode.NO_PERMISSION);
        }
        List<UserDTO> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }




    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {

        if(!userDTO.getProvider().isEmpty())
        {
            userDTO.setProvider(userDTO.getProvider());
            userDTO.setProviderKey(userDTO.getProviderKey());
        }
        userDTO.setUserId(null);
        userDTO.setRole(Role.USER);
        try {
            userService.createUser(userDTO);
            return ResponseEntity.ok(userDTO);
        }catch (CustomException e) {
            throw e;
        }
    }
    */

    //모든 유저 가져오기
    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }
    //특정 유저 가져오기(이메일로)
    @GetMapping("/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findByUserEmail(email));
    }
    //유저 추가
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO userDTO) {
        return ResponseEntity.ok(userService.save(userDTO));
    }
    //유저 업데이트 (전체)
    @PutMapping("/{email}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String email, @RequestBody @Valid UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateUser(email, userDTO));
    }
    //유저 업데이트 (이메일만)
    @PutMapping("/{email}/email")
    public ResponseEntity<String> updateUserEmail(@PathVariable String email, @RequestBody @Valid UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateEmail(email, userDTO));
    }
    //유저 업데이트 (전화번호만)
    @PutMapping("/{email}/phone")
    public ResponseEntity<String> updatePhoneNumber(@PathVariable String email, @RequestBody @Valid UserDTO userDTO) {
        return ResponseEntity.ok(userService.updatePhone(email, userDTO));
    }
    //유저 업데이트 (주소만)
    @PutMapping("/{email}/address")
    public ResponseEntity<String> updateAddress(@PathVariable String email, @RequestBody @Valid UserDTO userDTO) {
        return ResponseEntity.ok(userService.updateAddress(email, userDTO));
    }
    //유저 업데이트 (비밀번호만)
    @PutMapping("/{email}/password")
    public ResponseEntity<String> updatePassword(@PathVariable String email, @RequestBody @Valid UserDTO userDTO) {
        return ResponseEntity.ok(userService.updatePassword(email, userDTO));
    }
    //유저 삭제
    @DeleteMapping("/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email) {
        return ResponseEntity.ok("유저 삭제 완료");
    }

}
