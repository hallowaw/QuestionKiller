package site.yanbin.common.dto;

import lombok.Data;

@Data
public class WordBookUpdateDTO {
    private Long id;
    private String name;
    private String description;
    private Integer isPublic;
}
