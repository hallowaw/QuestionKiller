package site.yanbin.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WordBook {

    private Long id;
    private String name;          // 词书名称
    private String description;   // 词书描述
    private Long createdBy;       // 创建者（可以关联用户表）
    private LocalDateTime createdAt;
    private Long updatedBy;       // 更新者（可以关联用户表）
    private LocalDateTime updatedAt;
    private String shareCode;     // 分享码
    private Integer isPublic;     // 公开状态，0为不可公开，1为公开

}
