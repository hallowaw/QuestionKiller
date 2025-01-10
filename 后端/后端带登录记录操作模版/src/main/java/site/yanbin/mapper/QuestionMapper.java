package site.yanbin.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import site.yanbin.allGlobal.annotation.AutoFill;
import site.yanbin.common.entity.Question;
import site.yanbin.common.entity.QuestionOption;
import site.yanbin.common.enumeration.OperationType;

import java.util.List;


@Mapper
public interface QuestionMapper {




    List<QuestionOption> getQuestionOptions(Long id);


    Question getRandomQuestion(long defaultWordBookId);

    int getCorrectAnswerCount(@Param("currentId") Long currentId, @Param("id") Long id);

    @AutoFill(OperationType.INSERT)
    void insert(Question question);


    Page<Question> pageQuery(@Param("wordBookId") Long wordBookId, @Param("title") String title);

    @AutoFill(OperationType.UPDATE)
    void updateById(Question question);

    void deleteById(Long id);

    Question getQuestionById(Long id);


    void updateNote(@Param("id") Long id, @Param("note") String note);

    int getTotalAnswerCount(@Param("currentId") Long currentId, @Param("id") Long id);

    List<Long> getAllQuestionIdList(Long wordBookId);
}
