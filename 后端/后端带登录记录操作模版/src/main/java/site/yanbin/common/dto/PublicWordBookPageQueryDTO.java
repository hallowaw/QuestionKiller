package site.yanbin.common.dto;

import lombok.Data;

@Data
public class PublicWordBookPageQueryDTO {
    private String name;
    private int page;
    private int pageSize;
}
