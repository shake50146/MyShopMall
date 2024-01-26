package com.shake.account.controller;

import com.shake.account.model.dto.PasswordRequest;
import com.shake.account.model.dto.UpdateRequest;
import com.shake.account.model.entity.UserEntity;
import com.shake.account.service.AccountService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 * @author: UserController
 * @Description 用戶控制器對象
 * @Date 2023/10/22
 */
@RestController
@RequestMapping("/user")
@Slf4j
@CircuitBreaker(name = "account-breaker-2" , fallbackMethod = "fallback")
public class UserController {

    @Autowired
    private AccountService accountService;

    /**
     * 查看用戶
     * @param request
     * @return
     */
    @GetMapping("/account")
    public ResponseEntity<UserEntity> getUser(HttpServletRequest request){
        //獲取請求頭中的用戶名
        String username = request.getHeader("Username");
        //調用獲取用戶與密碼
        UserEntity user = accountService.getUserPasswordByUsername(username);
        log.info("get User");
        return ResponseEntity.ok(user);
    }


    /**
     * 更新用戶
     * @param updateRequest
     * @param request
     * @return
     */
    @PutMapping("/account")
    public ResponseEntity<UserEntity> updateUser(@RequestBody UpdateRequest updateRequest , HttpServletRequest request){
        //獲取請求頭中的用戶名
        String username = request.getHeader("Username");
        //設置用戶名
        updateRequest.setUsername(username);
        //調用更新用戶資料, 不包含密碼
        UserEntity user = accountService.updateUser(updateRequest);
        log.info("update User");
        return ResponseEntity.ok(user);
    }

    /**
     * 更新密碼
     * @param passwordRequest
     * @param request
     * @return
     */
    @PutMapping("/password")
    public ResponseEntity<UserEntity> updatePassword(@RequestBody PasswordRequest passwordRequest , HttpServletRequest request){
        //獲取請求頭中的用戶名
        String username = request.getHeader("Username");
        //設置用戶名
        passwordRequest.setUsername(username);
        //調用更新用戶密碼
        UserEntity userEntity = accountService.updatePassword(passwordRequest);
        log.info("update User password");
        return ResponseEntity.ok(userEntity);
    }

    /**
     * 刪除帳戶
     * @param request
     * @return
     */
    @DeleteMapping("/account")
    public ResponseEntity<String> deleteUser(HttpServletRequest request){
        //獲取請求頭中的用戶名
        String username = request.getHeader("Username");
        //調用刪除用戶
        accountService.deleteUser(username);
        log.info("delete User");
        return ResponseEntity.ok().body("Delete User Success");
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
