package site.yanbin.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserQuestionStats {

    private Long id;                // 主键ID
    private Long userId;            // 用户ID，外键关联用户表
    private Long questionId;        // 题目ID，外键关联题目表
    private Integer totalAnswerCount;   // 该用户答这道题的总次数，这个字段是冗余字段目前还用不上它
    private Integer correctAnswerCount;  // 该用户答对这道题的次数
    private LocalDateTime lastAnswerTime; // 最后答题时间

}
