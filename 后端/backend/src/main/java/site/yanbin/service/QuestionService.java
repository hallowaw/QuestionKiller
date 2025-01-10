package site.yanbin.service;

import org.springframework.stereotype.Service;
import site.yanbin.common.dto.QuestionAddDTO;
import site.yanbin.common.dto.QuestionPageQueryDTO;
import site.yanbin.common.dto.QuestionUpdateDTO;
import site.yanbin.common.result.PageResult;
import site.yanbin.common.vo.QuestionResponse;

@Service
public interface QuestionService {
    QuestionResponse getRandomQuestion();

    void add(QuestionAddDTO questionAddDTO);

    PageResult pageQuery(QuestionPageQueryDTO questionPageQueryDTO);

    void update(QuestionUpdateDTO questionUpdateDTO);

    void delete(Long id);

    Object getQuestionById(Long id);


    void updateUserQuestionStats(Long id, Integer correctAnswerCount, Integer totalAnswerCount);

    void updateNote(Long id, String note);

}
