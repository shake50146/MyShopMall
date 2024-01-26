package com.shake.account.controller;

import com.shake.account.exception.AdminException;
import com.shake.account.model.dto.UpdateRequest;
import com.shake.account.model.entity.RegisterEntity;
import com.shake.account.model.entity.UserEntity;
import com.shake.account.service.AccountService;
import com.shake.common.enums.UserRoleConstantEnum;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: AdminController
 * @Description 管理員控制器
 * @Date 2023/10/25
 */
@RestController
@RequestMapping("/admin")
@Slf4j
@CircuitBreaker(name = "account-breaker-2" , fallbackMethod = "fallback")
public class AdminController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/account/{username}")
    public ResponseEntity<UserEntity> getUser(@PathVariable String username , HttpServletRequest request){
        //獲取Admin 用戶名
        String admin = request.getHeader("Username");
        //獲取Admin 的role
        List<String> role = accountService.getRoleByUsername(admin);
        //判斷該請求的用戶role是否為Admin
        if(!role.contains(UserRoleConstantEnum.ROLE_ADMIN.toString())){
            throw new AdminException();
        }

        //到此步驟代表該請求是Admin用戶發送的 調用獲取特定用戶
        UserEntity user = accountService.getUserPasswordByUsername(username);
        log.info("Admin get User");
        return ResponseEntity.ok(user);
    }

    @PutMapping("/account")
    public ResponseEntity<UserEntity> updateUser(@RequestBody RegisterEntity registerEntity , HttpServletRequest request){
        //獲取Admin 用戶名
        String admin = request.getHeader("Username");
        //獲取Admin 的role
        List<String> role = accountService.getRoleByUsername(admin);
        //判斷該請求的用戶role是否為Admin
        if(!role.contains(UserRoleConstantEnum.ROLE_ADMIN.toString())){
            throw new AdminException();
        }

        //到此步驟代表該請求是Admin用戶發送的 調用更新特定用戶
        UpdateRequest updateRequest = UpdateRequest.builder()
                .username(registerEntity.getUsername())
                .gender(registerEntity.getGender())
                .phone(registerEntity.getPhone())
                .email(registerEntity.getEmail())
                .address(registerEntity.getAddress())
                .build();
        accountService.updateUser(updateRequest);

        UserEntity userEntity = accountService.updatePasswordAdmin(registerEntity);
        log.info("Admin update User");
        return ResponseEntity.ok(userEntity);
    }

    @DeleteMapping("/account/{username}")
    public ResponseEntity<UserEntity> deleteUser(@PathVariable String username , HttpServletRequest request){
        //獲取Admin 用戶名
        String admin = request.getHeader("Username");
        //獲取Admin 的role
        List<String> role = accountService.getRoleByUsername(admin);
        //判斷該請求的用戶role是否為Admin
        if(!role.contains(UserRoleConstantEnum.ROLE_ADMIN.toString())){
            throw new AdminException();
        }

        //到此步驟代表該請求是Admin用戶發送的 調用刪除特定用戶
        UserEntity user = accountService.getUserPasswordByUsername(username);
        accountService.deleteUser(username);
        log.info("Admin delete User");
        return ResponseEntity.ok(user);
    }


    /**
     * 熔斷機制需要的fallback方法 自定義返回調用其他服務錯誤信息
     * @param throwable
     * @return
     */
    public ResponseEntity<Object> fallback(Throwable throwable) {
        //默認錯誤信息, 表示資料庫出錯
        String message = "MySQL Service Error";
        if(throwable != null){
            //獲取Service端的錯誤信息, 如果有則使用該信息
            message = throwable.getMessage();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).header("Content-Type", "application/json").body(message);
    }

}
