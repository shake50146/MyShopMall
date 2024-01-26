package com.shake.authentication.controller;

import com.shake.authentication.model.entity.UserEntity;
import com.shake.authentication.utility.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author: TokenController
 * @Description
 * @Date 2023/11/10
 */
@RestController
@RequestMapping("/security")
public class TokenController {

    @PostMapping("/auth/token")
    public ResponseEntity<String> generateToken(@RequestBody UserEntity userEntity){

        String token = JwtUtil.generate(userEntity);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/get/token")
    public ResponseEntity<String> parseToken(@RequestHeader("Authorization") String token){

        Claims claims = JwtUtil.parse(token);
        String username = claims.getSubject();
        return ResponseEntity.ok(username);
    }
}
