package site.yanbin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.yanbin.common.context.BaseContext;
import site.yanbin.common.dto.QuestionAddDTO;
import site.yanbin.common.dto.QuestionPageQueryDTO;
import site.yanbin.common.dto.QuestionUpdateDTO;
import site.yanbin.common.entity.Question;
import site.yanbin.common.entity.QuestionOption;
import site.yanbin.common.entity.UserQuestionStats;
import site.yanbin.common.result.PageResult;
import site.yanbin.mapper.QuestionOptionMapper;
import site.yanbin.mapper.UserMapper;
import site.yanbin.common.vo.QuestionResponse;
import site.yanbin.mapper.QuestionMapper;
import site.yanbin.mapper.UserQuestionStatsMapper;
import site.yanbin.service.QuestionService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionOptionMapper questionOptionMapper;
    @Autowired
    private UserQuestionStatsMapper userQuestionStatsMapper;

    @Override
    public QuestionResponse getRandomQuestion() {
        // 获取用户的默认词书 ID
        long defaultWordBookId = userMapper.getDefaultWordBookIdByCurrentID(BaseContext.getCurrentId());

        // 随机获取题目信息
        Question question = questionMapper.getRandomQuestion(defaultWordBookId);

        // 查询答对次数
        int correctAnswerCount = questionMapper.getCorrectAnswerCount(BaseContext.getCurrentId(), question.getId());
        int totalAnswerCount = questionMapper.getTotalAnswerCount(BaseContext.getCurrentId(), question.getId());
        // 获取题目选项
        List<QuestionOption> options = questionMapper.getQuestionOptions(question.getId());

        // 封装成 QuestionResponse 对象并返回
        return new QuestionResponse(
                question.getId(),
                question.getTitle(),
                question.getQuestionType(),
                question.getCategory(),
                question.getCorrectChoice(),
                question.getNote(),
                question.getWordBookId(),
                options,
                correctAnswerCount,
                totalAnswerCount,
                question.getStatus()
        );
    }

    //这里要怎么加呢，简单的就是先插入两次，先把题目信息插入题目表，然后把选项信息插入选项表
    @Override
    public void add(QuestionAddDTO questionAddDTO) {
        // 1. 将 DTO 数据拆分，创建并保存 Question 实体对象
        Question question = new Question();
        BeanUtils.copyProperties(questionAddDTO, question);
        questionMapper.insert(question);

        // 2. 根据用户 ID 列表生成 UserQuestionStats 并批量插入
        List<UserQuestionStats> statsList = userMapper.getAllIdList().stream().map(userId -> {
            UserQuestionStats stats = new UserQuestionStats();
            stats.setUserId(userId);
            stats.setQuestionId(question.getId());
            stats.setTotalAnswerCount(0);
            stats.setCorrectAnswerCount(0);
            stats.setLastAnswerTime(null);
            return stats;
        }).collect(Collectors.toList());
        statsList.forEach(userQuestionStatsMapper::insert);

        // 3. 设置选项的外键 Question ID 并批量插入
        if (questionAddDTO.getOptions() != null && !questionAddDTO.getOptions().isEmpty()) {
            questionAddDTO.getOptions().forEach(option -> {
                option.setQuestionId(question.getId());
                questionOptionMapper.insert(option);
            });
        }
    }

    @Override
    public PageResult pageQuery(QuestionPageQueryDTO questionPageQueryDTO) {
        //这里主要做的就是根据词书id，先假设前端能返回给后端id，理论上来说确实是可以的
        //根据词书id(必给),标题（模糊查询）， 页码和每页显示记录数返回结果
        PageHelper.startPage(questionPageQueryDTO.getPage(),questionPageQueryDTO.getPageSize());
        //然后我们要做的就是根据名字模糊查询，我们再次使用PageHelper提供的方法,用Page对象来接受查询的接口
        //Page对象里面就会自动包含所有分页的信息，包括分页的数据和总数
        Page<Question> page=questionMapper.pageQuery(questionPageQueryDTO.getWordBookId(),questionPageQueryDTO.getTitle());
        //现在我们把两个数据拿出来total和List封装到PageResult对象中，然后返回
        return new PageResult(page.getTotal(),page.getResult());
    }

    //直接对整个方法进行加锁了
    @Override
    @Transactional
    public void update(QuestionUpdateDTO questionUpdateDTO) {
        // 创建一个Question对象，并将DTO数据复制到该对象
        Question question = new Question();
        BeanUtils.copyProperties(questionUpdateDTO, question);

        // 更新题目
        questionMapper.updateById(question);

        // 先根据题目ID删除相关的选项
        questionOptionMapper.deleteByQuestionId(questionUpdateDTO.getId());

        // 遍历并插入新的选项
        if (questionUpdateDTO.getOptions() != null && !questionUpdateDTO.getOptions().isEmpty()) {
            for (QuestionOption option : questionUpdateDTO.getOptions()) {
                // 设置选项所属题目ID
                option.setQuestionId(questionUpdateDTO.getId());

                // 插入选项到数据库
                questionOptionMapper.insert(option);
            }
        }
    }

    //所以这里肯定用事务了，synchronized不能保证一致性
    @Override
    @Transactional
    public  void delete(Long id) {
        //根据id删除题目
        //要去题目表，选项表，用户题目统计表里面删除
        //直接方法上加同步锁来处理了
        questionOptionMapper.deleteByQuestionId(id);
        userQuestionStatsMapper.deleteByQuestionId(id);
        questionMapper.deleteById(id);
    }

    @Override
    public Object getQuestionById(Long id) {
        Question question = questionMapper.getQuestionById(id);
        int correctAnswerCount = questionMapper.getCorrectAnswerCount(BaseContext.getCurrentId(), question.getId());
        int totalAnswerCount = questionMapper.getTotalAnswerCount(BaseContext.getCurrentId(), question.getId());
        List<QuestionOption> options = questionMapper.getQuestionOptions(question.getId());
        return new QuestionResponse(
                question.getId(),
                question.getTitle(),
                question.getQuestionType(),
                question.getCategory(),
                question.getCorrectChoice(),
                question.getNote(),
                question.getWordBookId(),
                options,
                correctAnswerCount,
                totalAnswerCount,
                question.getStatus()
        );
    }

    @Override
    public void updateUserQuestionStats(Long id, Integer correctAnswerCount, Integer totalAnswerCount) {
        userQuestionStatsMapper.updateUserQuestionStats(id, BaseContext.getCurrentId(), correctAnswerCount,totalAnswerCount);
    }

    @Override
    public void updateNote(Long id, String note) {
        questionMapper.updateNote(id,note);
    }


}
