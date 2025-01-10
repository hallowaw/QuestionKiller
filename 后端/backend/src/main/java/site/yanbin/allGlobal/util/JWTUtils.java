package site.yanbin.allGlobal.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JWTUtils {

    private String secret;

    public String createJWT(Map<String, Object> claims) throws Exception{
        // 加密成令牌（重点）
        String token = Jwts.builder().setClaims(claims) // 令牌的第二部分数据：负载的有效数据
                .signWith(SignatureAlgorithm.HS256, secret) // 令牌的第三部分数据：签名，加密算法，密钥
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000)).compact(); // 令牌的存储时间
        return token;
    }


    public Claims parseJWT(String token) throws Exception{
        // 1、解密
        Claims result = Jwts.parser().setSigningKey(secret)
                .parseClaimsJws(token).getBody();
        return result;
    }
}
