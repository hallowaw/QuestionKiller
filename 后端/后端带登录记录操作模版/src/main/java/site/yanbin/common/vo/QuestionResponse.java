package site.yanbin.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.yanbin.common.entity.QuestionOption;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {
    private Long id;
    private String title;
    private String questionType;
    private String category;
    private String correctChoice;
    private String note;
    private Long wordBookId;
    private List<QuestionOption> options;  // 用来存放选项
    private Integer correctAnswerCount;
    private Integer totalAnswerCount;
    private Integer status;
}