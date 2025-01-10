package site.yanbin.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String username;
    private String password;
    private String role;
    private String email;
    private String nickname;
    private String avatarUrl;
    // 新增默认词书ID字段
    private Long defaultWordBookId;
}
