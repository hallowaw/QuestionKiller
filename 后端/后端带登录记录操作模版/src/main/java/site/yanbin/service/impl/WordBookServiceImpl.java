package site.yanbin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.yanbin.common.context.BaseContext;
import site.yanbin.common.dto.PublicWordBookPageQueryDTO;
import site.yanbin.common.dto.UpdateIsPublicDTO;
import site.yanbin.common.dto.WordBookAddDTO;
import site.yanbin.common.dto.WordBookUpdateDTO;
import site.yanbin.common.entity.Question;
import site.yanbin.common.entity.UserQuestionStats;
import site.yanbin.common.entity.WordBook;
import site.yanbin.common.result.PageResult;
import site.yanbin.common.result.Result;
import site.yanbin.common.vo.WordBookPageVO;
import site.yanbin.common.vo.WordBookVO;
import site.yanbin.mapper.QuestionMapper;
import site.yanbin.mapper.UserQuestionStatsMapper;
import site.yanbin.mapper.UserWordBookMapper;
import site.yanbin.mapper.WordBookMapper;
import site.yanbin.service.UserService;
import site.yanbin.service.WordBookService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
public class WordBookServiceImpl implements WordBookService {

    @Autowired
    private WordBookMapper wordBookMapper;
    @Autowired
    private UserWordBookMapper userWordBookMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserQuestionStatsMapper userQuestionStatsMapper;

    @Transactional
    @Override
    public void add(WordBookAddDTO wordBookAddDTO) {
        //    private String name;          // 词书名称
        //    private String description;   // 词书描述
        //    private Integer isPublic;     // 公开状态，0为不可公开，1为公开

        //首先我们要做的是先随机生成一个4个大写字母的分享码
        //然后的话调用mapper，去查一下是否有这个分享码，没有的话在随机生成一个
        // ，但是可能很费时间，那有没有更好的办法呢
        //TODO 这里的话是可以用缓存来进行优化的，但是我们现在的话就先直接简单的来处理吧

        Random random = new Random();

        String shareCode;
        // 假设这是一个查询方法，检查分享码是否已存在
        while (true) {
            int number=0;
            // 生成4位大写字母的分享码
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 4; i++) {
                char randomChar = (char) ('A' + random.nextInt(26));  // 随机生成A-Z之间的字母
                sb.append(randomChar);
            }
            shareCode = sb.toString();
            // 检查分享码是否已经存在（这里假设isExist是通过查询数据库得到的结果）
            number= wordBookMapper.checkShareCodeIsExist(shareCode);
            // 这里检查个数
            if(number==0){
                //跳出循环

                break;
            }
        }

        WordBook wordBook = new WordBook();
        wordBook.setShareCode(shareCode);
        wordBook.setIsPublic(wordBookAddDTO.getIsPublic());
        wordBook.setName(wordBookAddDTO.getName());
        wordBook.setDescription(wordBookAddDTO.getDescription());
        //这里就要组合一个插入的对象了
        wordBookMapper.insert(wordBook);
        System.out.println("Generated wordBook ID: " + wordBook.getId());  // 确认ID是否已回填
        //这里应该词书和用户关系表还是要插入一个的，插入的时候，先要获取到用户id还有生成的词书的id
        String permission="WRITE";
        System.out.println("Current user ID: " + BaseContext.getCurrentId());
        userWordBookMapper.insert(wordBook.getId(), BaseContext.getCurrentId(),permission);
    }

    @Override
    public void update(WordBookUpdateDTO wordBookUpdateDTO) {
        WordBook wordBook = new WordBook();
// 使用 BeanUtils 批量复制属性
        BeanUtils.copyProperties(wordBookUpdateDTO, wordBook);
        wordBookMapper.update(wordBook);
    }

    //   //删除词书接口，要注意两个地方，1.先检查是不是用户的默认词书，默认词书不可以被删除，2.删除词书和用户表的关系表的记录，
    @Override
    public void delete(Long id) {
//1.先检查是不是用户的默认词书，默认词书不可以被删除
        Long defaultWordBookId = userService.getDefaultWordBookIdByCurrentID(BaseContext.getCurrentId());
        if (defaultWordBookId.equals(id)) {
            throw new RuntimeException("默认词书不可以被删除");
        }
        //这里后续加一个提示信息吧，如果找不到要删除的词书提示一下

        userWordBookMapper.deleteByWordBookIdAndUserId( id, BaseContext.getCurrentId());

    }

    @Override
    public List<WordBookVO> getWordBookListByUserId() {
        List<Long> wordIdList = userWordBookMapper.getAllWordIdList(BaseContext.getCurrentId());

        // 如果没有词书ID，返回空列表
        if (wordIdList == null ||  wordIdList.size() == 0) {
            return Collections.emptyList(); // 返回空List
        }

        // 循环查询词书信息
        List<WordBookVO> wordBookVOList = new ArrayList<>();
        for (Long wordId : wordIdList) {
            WordBookVO wordBook = wordBookMapper.getWordBookById(wordId);
            if (wordBook != null) {
                wordBookVOList.add(wordBook);
            }
        }

        return wordBookVOList;
    }

    @Override
    public Integer getIsPublicByWordBookId(Long id) {
        return wordBookMapper.getIsPublicByWordBookId(id);
    }

    @Override
    public void updateIsPublic(UpdateIsPublicDTO updateIsPublicDTO) {
        wordBookMapper.newUpdateIsPublic(updateIsPublicDTO.getId(),updateIsPublicDTO.getIsPublic());
    }

    @Override
    public PageResult pageQuery(PublicWordBookPageQueryDTO publicWordBookPageQueryDTO) {
        PageHelper.startPage(publicWordBookPageQueryDTO.getPage(),publicWordBookPageQueryDTO.getPageSize());
        //然后我们要做的就是根据名字模糊查询，我们再次使用PageHelper提供的方法,用Page对象来接受查询的接口
        //Page对象里面就会自动包含所有分页的信息，包括分页的数据和总数
        Page<WordBookPageVO> page=wordBookMapper.pageQuery(publicWordBookPageQueryDTO.getName());
        //现在我们把两个数据拿出来total和List封装到PageResult对象中，然后返回
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public Result addWordBookByShareCode(String shareCode) {
        //首先先根据shareCode获取到词书的id
        Long wordBookId = wordBookMapper.getWordBookIdByShareCode(shareCode);
        //TODO先可以检查一下词书是否已在用户里面存在
        if(userWordBookMapper.getAllWordIdList(BaseContext.getCurrentId()).contains(wordBookId)){
            //如果存在就抛出一个异常，能不能给返回一个 result 结果的msg信息呢
            return Result.error("该词书已被收入到本用户的词书库中");
        }
        //然后的话检查是不是公开状态
        if(wordBookMapper.getIsPublicByWordBookId(wordBookId)==1){
            //如果是的话，就直接插入一个用户和词书的关系表
            userWordBookMapper.insert(wordBookId,BaseContext.getCurrentId(),"READ");
            //这里还要把词书里面所有题目和和用户在UserQuestionStats表中插入一个记录
            //目前的想法是先获取出词书里面所有题目的id，做成一个list，然后遍历，然后插入一个记录
            List<Long> questionIdList = questionMapper.getAllQuestionIdList(wordBookId);
            for (Long questionId : questionIdList) {
                //插入记录
                UserQuestionStats userQuestionStats = new UserQuestionStats();
                userQuestionStats.setUserId(BaseContext.getCurrentId());
                userQuestionStats.setQuestionId(questionId);
                userQuestionStats.setTotalAnswerCount(0);
                userQuestionStats.setCorrectAnswerCount(0);
                userQuestionStats.setLastAnswerTime(null);
                userQuestionStatsMapper.insert(userQuestionStats);
            }
            return Result.success();
        }else{
            //如果不是的话，就抛出一个异常
           return Result.error("该词书为私有状态，无法添加到词书库中");
        }
    }
}
