package com.shake.gateway.controller;

import com.shake.authentication.entity.UserEntity;
import com.shake.authentication.utility.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;

/**
 * @author: TokenController
 * @Description
 * @Date 2023/11/10
 */
@RestController
@RequestMapping("/security")
public class TokenController {

    @PostMapping("/token")
    @Operation(security = {@SecurityRequirement(name = "Authorization" ) })
    public ResponseEntity<String> generateToken(@RequestBody UserEntity userEntity){

        String token = JwtUtil.generate(userEntity);

        return ResponseEntity.ok(token);
    }


}
