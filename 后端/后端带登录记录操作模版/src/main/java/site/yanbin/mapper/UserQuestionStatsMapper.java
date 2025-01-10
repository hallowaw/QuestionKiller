package site.yanbin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import site.yanbin.common.entity.UserQuestionStats;

import java.time.LocalDateTime;

@Mapper
public interface UserQuestionStatsMapper {
    void insert(UserQuestionStats userQuestionStats);

    void deleteByQuestionId(Long id);

    void updateUserQuestionStats(
            @Param("id") Long id,
            @Param("currentId") Long currentId,
            @Param("correctAnswerCount") Integer correctAnswerCount,
            @Param("totalAnswerCount") Integer totalAnswerCount);

    Long getAllTotalAnswerCount(Long currentId);

    Long getAllCorrectAnswerCount(Long currentId);

    LocalDateTime getAllLastAnswerTime(Long currentId);
}
