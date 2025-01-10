package site.yanbin.common.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionPageQueryDTO implements Serializable {

    //标题
    private String title;

    //页码
    private int page;

    //每页显示记录数
    private int pageSize;

    private Long wordBookId;

}
