package site.yanbin.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionOption {

    private Long id;
    private Long questionId;      // 外键关联题目表
    private String optionLabel;   // 选项标签（如 A、B、C、D）
    private String optionText;    // 选项内容
}
