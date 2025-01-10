package site.yanbin.mapper;

import org.apache.ibatis.annotations.Mapper;
import site.yanbin.common.entity.QuestionOption;

@Mapper
public interface QuestionOptionMapper {

    //@Data
    //public class QuestionOption {
    //
    //    private Long questionId;      // 外键关联题目表
    //    private String optionLabel;   // 选项标签（如 A、B、C、D）
    //    private String optionText;    // 选项内容
    //}

    void insert(QuestionOption option);

    void deleteByQuestionId(Long id);
}
