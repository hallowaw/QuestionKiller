package site.yanbin.common.dto;

import lombok.Data;
import site.yanbin.common.entity.QuestionOption;

import java.util.List;
@Data
public class QuestionUpdateDTO {
    private Long id;
    private String title;
    private String questionType;
    private String category;
    private String correctChoice;
    private String note;
    private Long wordBookId;
    private List<QuestionOption> options;  // 用来存放选项
    private Integer status;
}