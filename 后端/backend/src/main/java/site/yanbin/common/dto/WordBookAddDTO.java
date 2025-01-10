package site.yanbin.common.dto;

import lombok.Data;

@Data
public class WordBookAddDTO {
    private String name;          // 词书名称
    private String description;   // 词书描述
    private Integer isPublic;     // 公开状态，0为不可公开，1为公开
}
