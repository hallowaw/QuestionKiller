package site.yanbin.common.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class WordBookPageVO {
    private Long id;
    private String name;
    private String description;
    private String shareCode;     // 分享码
    private Long createdBy;       // 创建者（可以关联用户表）


}
