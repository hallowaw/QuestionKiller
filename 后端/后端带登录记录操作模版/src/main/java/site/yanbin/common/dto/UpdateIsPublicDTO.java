package site.yanbin.common.dto;

import lombok.Data;

@Data
public class UpdateIsPublicDTO {
    private Long id;         // 词书ID
    private Integer isPublic; // 公开状态 (0 或 1)
}