package site.yanbin.common.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShowStatsVO {
    private Long AllTotalAnswerCount;   // 该用户答这道题的总次数，这个字段是冗余字段目前还用不上它
    private Long AllCorrectAnswerCount;  // 该用户答对这道题的次数
    private LocalDateTime AllLastAnswerTime;
}
