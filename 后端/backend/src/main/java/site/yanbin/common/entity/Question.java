package site.yanbin.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    private Long id;
    private String title;
    private String questionType;  // 单选题、多选题等
    private String category;      // 分类（如Java、数据库等）
    private String correctChoice; // 正确答案
    private LocalDateTime createdAt;
    private Long createdBy;
    private LocalDateTime updatedAt;
    private Long updatedBy;
    private String note;
    private Long wordBookId;      // 外键关联词书表
    private Integer status;
}
