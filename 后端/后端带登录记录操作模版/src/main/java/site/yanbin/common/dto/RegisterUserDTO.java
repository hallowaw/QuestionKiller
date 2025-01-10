package site.yanbin.common.dto;

import lombok.Data;

@Data
public class RegisterUserDTO {
    private String username;
    private String password;
    private String email;
}
