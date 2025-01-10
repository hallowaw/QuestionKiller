package site.yanbin;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// @SpringBootTest
public class JWTTest {
    // 加密：得到一个令牌。
    @Test
    public void jwtTest1() throws Exception{

        // 生成一个适用于 HS256 算法的安全密钥
        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        // 获取秘钥的字节数组形式
        byte[] keyBytes = secretKey.getEncoded();
        // 编码成Base64的秘钥
        String encodedKey = Base64.getEncoder().encodeToString(keyBytes);
        System.out.println(encodedKey); // IBVFKa3LlJrqd5pP2pOrDDGRZYpIbb1mWwIyUgicv4w=


        // 指定中间部分的有效数据
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", "dlei999");
        claims.put("id", "003197");

        // 加密成令牌（重点）
        String token = Jwts.builder().setClaims(claims) // 令牌的第二部分数据：负载的有效数据
                .signWith(SignatureAlgorithm.HS256, "IBVFKa3LlJrqd5pP2pOrDDGRZYpIbb1mWwIyUgicv4w") // 令牌的第三部分数据：签名，加密算法，密钥
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 60 * 1000)).compact(); // 令牌的存储时间
        System.out.println(token);
    }


    // 写一个测试方法，完成对JWT令牌的解密操作（校验）
    @Test
    public void jwtTest2() throws Exception{
        // 1、获取JWT令牌 token
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6IjAwMzE5NyIsInVzZXJuYW1lIjoiZGxlaTk5OSIsImV4cCI6MTcyNTUyMTYzMH0.--4lUxUtvFu2dvU9EyN6kiv5PuQ-I5k3GXcY9SUbFT0";
        // 2、解密
        Map<String, Object> result = Jwts.parser().setSigningKey("IBVFKa3LlJrqd5pP2pOrDDGRZYpIbb1mWwIyUgicv4w")
                .parseClaimsJws(token).getBody();
        System.out.println(result);
    }
}
