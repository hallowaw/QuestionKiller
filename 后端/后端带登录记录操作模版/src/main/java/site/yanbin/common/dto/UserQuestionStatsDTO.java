package site.yanbin.common.dto;

import lombok.Data;

@Data
public class UserQuestionStatsDTO {
    private Long id;
    private Integer correctAnswerCount;
    private Integer totalAnswerCount;
}