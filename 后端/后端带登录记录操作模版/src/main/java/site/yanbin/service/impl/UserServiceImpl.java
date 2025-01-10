package site.yanbin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.yanbin.common.context.BaseContext;
import site.yanbin.common.dto.RegisterUserDTO;
import site.yanbin.common.dto.UpdatePasswordDTO;
import site.yanbin.common.entity.User;
import site.yanbin.common.entity.WordBook;
import site.yanbin.common.result.Result;
import site.yanbin.common.vo.DefaultWorkBookVO;
import site.yanbin.common.vo.ShowStatsVO;
import site.yanbin.common.vo.UserInfoVO;
import site.yanbin.common.vo.WordBookVO;
import site.yanbin.mapper.UserMapper;
import site.yanbin.mapper.UserQuestionStatsMapper;
import site.yanbin.mapper.UserWordBookMapper;
import site.yanbin.mapper.WordBookMapper;
import site.yanbin.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserWordBookMapper userWordBookMapper;
    @Autowired
    private WordBookMapper wordBookMapper;
    @Autowired
    private UserQuestionStatsMapper userQuestionStatsMapper;

    @Override
    public User getLoginUserByUser(User user) {
        User u = userMapper.getLoginUserByUser(user);
        return u;
    }



    @Override
    public void updatePassword(UpdatePasswordDTO updatePasswordDTO) {
        System.out.println("updatePasswordDTO = " + updatePasswordDTO);

        //直接调用mapper改就完事了
        userMapper.updatePassword(updatePasswordDTO);

    }

    @Transactional
    @Override
    public Result register(RegisterUserDTO registerUserDTO) {
        // 判断用户名是否重复
        if (userMapper.checkUsernameIsExist(registerUserDTO.getUsername()) != 0) {
            return Result.error("用户名已存在");
        }
        // 注册
        User user = new User();
        user.setUsername(registerUserDTO.getUsername());
        user.setPassword(registerUserDTO.getPassword());
        user.setEmail(registerUserDTO.getEmail());
        userMapper.register(user);
        //同时的话记得生成一个词书，并且把用这个用户的词书绑定到这个默认词书上面去
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
        wordBook.setIsPublic(0);
        wordBook.setName(registerUserDTO.getUsername()+"的默认词书");
        wordBook.setDescription("默认词书");
        //这里就要组合一个插入的对象了
        //TODO 问题出现在这里，就是这里的Insert会调用Autofill注解，注解里面会用currentId，但是这里currentId为null，所以会报错，所以这个方法不用注解就好了
        //改一下insert方法，同时注意其他地方也调用了这个方法，然后接口排除拦截起就好了
        //所以的话我们直接不用insert方法，用一个新的方法
        wordBook.setCreatedBy(user.getId());
        wordBook.setUpdatedBy(user.getId());
        wordBookMapper.insertAtRegister(wordBook);
        System.out.println("Generated wordBook ID: " + wordBook.getId());  // 确认ID是否已回填
        //这里应该词书和用户关系表还是要插入一个的，插入的时候，先要获取到用户id还有生成的词书的id
        String permission="WRITE";
        System.out.println("Current user ID: " + user.getId());
        userWordBookMapper.insert(wordBook.getId(), user.getId(),permission);
        //这里还要把用户的默认词书id存到数据库里面
        userMapper.updateDefaultWordBookId(user.getId(),wordBook.getId());
        return Result.success();
    }

    @Override
    public void updateAvatarUrl(String imageUrl) {
        userMapper.updateAvatarUrl(imageUrl,BaseContext.getCurrentId());
    }

    @Override
    public String getAvatarUrl() {
        return userMapper.getAvatarUrl(BaseContext.getCurrentId());
    }


    @Override
    public void updateNickname(String nickname) {

        userMapper.updateNickname(nickname,BaseContext.getCurrentId());
    }

    @Override
    public UserInfoVO getNicknameAndUsername() {
         return userMapper.getNicknameAndUsername(BaseContext.getCurrentId());
    }



    @Override
    public Long getDefaultWordBookIdByCurrentID(Long currentId) {
        //哈哈，原来在mapper层面是已经做了这个事情的
         return userMapper.getDefaultWordBookIdByCurrentID(currentId);
    }

    @Override
    public void updateDefaultWordBookId(Long currentId, Long wordBookId) {
         userMapper.updateDefaultWordBookId(currentId,wordBookId);
    }

    @Override
    public DefaultWorkBookVO getDefaultWordBookNameByCurrentID() {
        return userMapper.getDefaultWordBookNameByCurrentID(BaseContext.getCurrentId());
    }

    @Override
    public ShowStatsVO showStats() {
        //根据用户id查询出 user_question_stats表里面所有的  `total_answer_count`之和，`correct_answer_count`之和
        ShowStatsVO showStatsVO = new ShowStatsVO();
        showStatsVO.setAllTotalAnswerCount(userQuestionStatsMapper.getAllTotalAnswerCount(BaseContext.getCurrentId()));
        showStatsVO.setAllCorrectAnswerCount(userQuestionStatsMapper.getAllCorrectAnswerCount(BaseContext.getCurrentId()));
        showStatsVO.setAllLastAnswerTime(userQuestionStatsMapper.getAllLastAnswerTime(BaseContext.getCurrentId()));
        return showStatsVO;
    }

    @Override
    public String getPermission() {
        Long defaultWordBookId = getDefaultWordBookIdByCurrentID(BaseContext.getCurrentId());
        return userWordBookMapper.getPermission(BaseContext.getCurrentId(),defaultWordBookId);
    }


}
