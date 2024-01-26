package com.shake.account.service;

import com.shake.account.exception.PasswordException;
import com.shake.account.exception.UsernameException;
import com.shake.account.mapper.UserMapper;
import com.shake.account.model.dto.PasswordRequest;
import com.shake.account.model.dto.UpdateRequest;
import com.shake.account.model.entity.RegisterEntity;
import com.shake.account.model.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: AccountService
 * @Description 用戶業務類
 * @Date 2023/10/16
 */
@Service
public class AccountService {

    @Autowired
    private UserMapper userMapper;

    @Bean
    private BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 獲取用戶信息
     * @param username
     * @return UserEntity
     */
    public UserEntity getUserPasswordByUsername(String username) {
        UserEntity userEntity = userMapper.selectUserPasswordByUsername(username);
        return userEntity;
    }

    /**
     * 以用戶名獲取用戶信息 不含密碼
     * @param username
     * @return
     */
    public UserEntity getUserByUsername(String username){
        UserEntity user = userMapper.selectUserByUsername(username);
        return user;
    }

    /**
     * 以用戶名撐獲取用戶id
     * @param username
     * @return
     */
    public Integer getAccIdByUsername(String username){
        Integer accId = userMapper.selectAccIdByUsername(username);
        return accId;
    }

    /**
     * 以用戶名獲取角色
     * @param username
     * @return
     */
    public List<String> getRoleByUsername(String username){
        List<String> list = userMapper.listRoleByUsername(username);
        return list;
    }

    /**
     * 更新用戶信息
     * @param request
     * @return
     */
    public UserEntity updateUser(UpdateRequest request){

        UserEntity user = userMapper.selectUserByUsername(request.getUsername());
        if(user == null){
            throw new UsernameException("Username Not Found");
        }
        //更新
        userMapper.updateUserByRequest(request);
        return userMapper.selectUserByUsername(request.getUsername());
    }

    /**
     * 更新密碼
     * @param user
     * @return
     */
    public UserEntity updatePassword(PasswordRequest user){
        UserEntity userEntity = userMapper.selectUserPasswordByUsername(user.getUsername());
        //確認帳號原有密碼
        if(!bCryptPasswordEncoder().matches(user.getOldPassword(),userEntity.getPassword())){
            throw new PasswordException();
        }
        //對新密碼加密
        String newPassword = bCryptPasswordEncoder().encode(user.getNewPassword());
        //更新密碼
        userMapper.updatePasswordByAccId(newPassword, userEntity.getAccId());
        userEntity.setPassword(newPassword);

        return userEntity;
    }

    public UserEntity updatePasswordAdmin(RegisterEntity registerEntity){
        UserEntity userEntity = userMapper.selectUserPasswordByUsername(registerEntity.getUsername());

        String password = registerEntity.getPassword();
        String newPassword = bCryptPasswordEncoder().encode(password);

        userMapper.updatePasswordByAccId(newPassword, userEntity.getAccId());
        userEntity.setPassword(newPassword);
        return userEntity;
    }

    /**
     * 用戶端刪除用戶
     */
    public void deleteUser(String username){
        userMapper.deleteUserByUsername(username);
    }




}
