package site.yanbin.common.dto;

import lombok.Data;
import site.yanbin.common.entity.QuestionOption;

import java.util.List;

@Data
public class QuestionAddDTO {
    private String title;
    private String questionType;  // 单选题、多选题等
    private String category;      // 分类（如Java、数据库等）
    private String correctChoice; // 正确答案
    private String note;
    private Long wordBookId;      // 外键关联词书表
    private Integer status;
    private List<QuestionOption> options;  // 用来存放选项
}
