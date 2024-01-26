package com.shake.authentication.utility;

import com.shake.authentication.model.entity.UserEntity;
import com.shake.common.enums.UserRoleConstantEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.time.Duration;
import java.util.Date;

/**
 * @author: JwtUtil
 * @Description 認證令牌工具類
 * @Date 2023/10/17
 */
@Component
public class JwtUtil {

    /**
     *  定義密鑰 不能洩漏 : 也可以設置在yml中
      */
//    @Value("${application.security.jwt.secret-key}")
//    private String secretKey;
    private final static Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    /**
     * 設定過期時間 : 也可以設置在yml中
     */
//    @Value("${application.security.jwt.expiration}")
//    private long jwtExpiration;
    private final static Duration expiration = Duration.ofHours(2);

    /**
     *  生成JWT
     * @param user
     * @return String
     */
    public static String generate(UserEntity user){

        //過期時間
        Date expiryDate = new Date(System.currentTimeMillis() + expiration.toMillis());

        //生成JWT
        return Jwts.builder()
                //設置JWT的主題為用戶的用戶名
                .setSubject(user.getUsername())
                //設置JWT發行的時間為當下
                .setIssuedAt(new Date())
                //設置過期時間2小
                .setExpiration(expiryDate)
                //使用HS512算法和密鑰對JWT進行簽名
                .signWith(SignatureAlgorithm.HS512,secretKey)
                //添加自訂義聲明
                .claim("accId",user.getAccId())
                .claim("role", user.getRole())
                .claim("gender",user.getGender())
                .claim("email",user.getEmail())
                //將JWT生成為字符串
                .compact();
    }

    /**
     * 解析JWT 獲取聲明
     * @param token
     * @return Claims
     */
    public static Claims parse(String token){

        //判斷token是否為空 為空直接返回空的claims對象
        if (StringUtils.isEmpty(token)) throw new JwtException("Invalid JWT token");

        Claims claims = null;
        // try-catch 如果 密鑰過期或錯誤都會導致失敗
        try{
            claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (JwtException e){
            throw new JwtException("Parse : Invalid or Expired JWT token");
        }

        //三元運算 判斷解析完後的claims是否為空, 避免空指針異常
        return claims!=null ? claims:Jwts.claims();
    }

    /**
     * 驗證過期時間
     * @param token
     * @return true : token expired , false : token efficient
     */
    public static boolean validation(String token){
        try{
            //獲取過期時間確認token的過期時間是否在當下之前 如果是代表已過期
            boolean before = !Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build().parseClaimsJws(token)
                    .getBody().getExpiration().before(new Date());
            return before;
        }catch (JwtException e){
            throw new JwtException("Validation : Invalid or Expired JWT token");
        }
    }

    /**
     * 在token有效期內, 根據token解碼的信息, 生成用戶信息, 並獲取認證信息
     * @param token
     * @return
     */
    public static Authentication getAuthentication(String token){
        //從token解析獲取聲明字串
        Claims claims = parse(token);

        //創建用戶
        UserEntity user = UserEntity.builder()
                .username(claims.getSubject())
                .accId(claims.get("accId", Integer.class))
                .email(claims.get("email", String.class))
                .gender(claims.get("gender",String.class))
                .role(UserRoleConstantEnum.valueOf(claims.get("role", String.class)))
                .build();

        return new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
    }
}
