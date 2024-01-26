package com.shake.account.service;

import com.shake.account.exception.PasswordException;
import com.shake.account.exception.UsernameException;
import com.shake.account.feign.AuthFeignService;
import com.shake.account.mapper.RegisterMapper;
import com.shake.account.mapper.TokenMapper;
import com.shake.account.mapper.UserMapper;
import com.shake.account.model.entity.AuthEntity;
import com.shake.account.model.entity.UserEntity;
import com.shake.common.enums.UserRoleConstantEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author: AccountAuthService
 * @Description 用戶認證業務類
 * @Date 2023/10/18
 */
@Service
public class AccountAuthService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TokenMapper tokenMapper;

    @Autowired
    private RegisterMapper registerMapper;

    @Autowired
    private AuthFeignService authFeignService;

    @Bean
    private BCryptPasswordEncoder bCryptPasswordEncoderAuth(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 註冊帳號 : 註冊完成後直接登入
     * @param username
     * @param password
     * @param email
     * @param phone
     * @param gender
     * @param address
     * @param role
     * @return
     */
    public AuthEntity register(String username, String password, String gender, String phone, String email, String address, UserRoleConstantEnum role) {
        //檢查用戶名, 已存在則拋出異常
        UserEntity user = userMapper.selectUserByUsername(username);
        if(user != null){
            throw new UsernameException("Username Exists");
        }
        //生成token認證帳號: 沒有密碼的用戶對像
        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .gender(gender)
                .phone(phone)
                .email(email)
                .address(address)
                .role(role)
                .build();

        //密碼加密
        String encodePassword = bCryptPasswordEncoderAuth().encode(password);
        //保存數據到資料庫
        registerMapper.insertUser(userEntity);
        registerMapper.insertPassword(encodePassword,username);

        //登入步驟:生成token 遠端調用auth服務
        String token = authFeignService.generateToken(userEntity).getBody();
        //保存token 認證用
        storeToken(username,token);

        return new AuthEntity(userEntity,token);
    }


    /**
     * 用戶登入認證
     * @param username
     * @param password
     * @return
     */
    public AuthEntity authenticationLogin(String username , String password){

        //確認帳號
        UserEntity user = userMapper.selectUserPasswordByUsername(username);
        if(user == null){
            //用戶為null, 拋出異常
            throw new UsernameException("Username Not Found");
        }

        //確認帳號對應的密碼
        if(!new BCryptPasswordEncoder().matches(password,user.getPassword())){
            throw new PasswordException();
        }

        //生成token認證帳號: 沒有密碼的用戶對像
        UserEntity userEntity = UserEntity.builder()
                .accId(user.getAccId())
                .username(username)
                .email(user.getEmail())
                .gender(user.getGender())
                .address(user.getAddress())
                .role(user.getRole())
                .build();

        //登入步驟:生成token 遠端調用auth服務
        String token = authFeignService.generateToken(user).getBody();
        //保存token 認證用
        storeToken(username,token);

        return new AuthEntity(userEntity,token);
    }


    /**
     * 獲取token
     * @param username
     * @return
     */
    public String getToken(String username){
        String token = tokenMapper.selectTokenByUsername(username);
        return token;
    }

    /**
     * 儲存token
     * @param username
     * @param token
     * @return
     */
    public Integer storeToken(String username , String token){
        Integer result;
        if(tokenMapper.selectTokenByUsername(username) == null){
            result = tokenMapper.insertTokenByUsername(username,token);
        }else {
            result = tokenMapper.updateTokenByUsername(username,token);
        }

        return result;
    }

    /**
     * 刷新token
     * @param username
     * @return
     */
    public AuthEntity refreshToken(String username){
        //獲取用戶資料
        UserEntity user = userMapper.selectUserByUsername(username);
        //確認帳號
        if(user == null){
            //用戶為null, 拋出異常
            throw new UsernameException("Username Not Found");
        }

        //遠端調用Auth service, 生成token
        String token = authFeignService.generateToken(user).getBody();
        //更新資料庫token
        tokenMapper.updateTokenByUsername(username, token);
        return new AuthEntity(user,token);
    }

    /**
     * 刪除token
     * @param username
     */
    public Integer deleteToken(String username){
        Integer accId = userMapper.selectAccIdByUsername(username);
        //刪除
        Integer result = tokenMapper.deleteTokenByAccId(accId);
        return result;
    }


}
