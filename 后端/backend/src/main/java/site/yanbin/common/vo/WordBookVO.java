package site.yanbin.common.vo;

import lombok.Data;

@Data
public class WordBookVO {
    private Long id;
    private String name;
    private String description;
    private Integer isPublic;
}
