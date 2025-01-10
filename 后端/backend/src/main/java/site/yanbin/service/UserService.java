package site.yanbin.service;

import org.springframework.stereotype.Service;
import site.yanbin.common.dto.RegisterUserDTO;
import site.yanbin.common.dto.UpdatePasswordDTO;
import site.yanbin.common.entity.User;
import site.yanbin.common.result.Result;
import site.yanbin.common.vo.DefaultWorkBookVO;
import site.yanbin.common.vo.ShowStatsVO;
import site.yanbin.common.vo.UserInfoVO;
import site.yanbin.common.vo.WordBookVO;

import java.util.List;

@Service
public interface UserService {
    User getLoginUserByUser(User user);



    void updatePassword(UpdatePasswordDTO updatePasswordDTO);

    Result register(RegisterUserDTO registerUserDTO);

    void updateAvatarUrl(String imageUrl);

    String getAvatarUrl();



    void updateNickname(String nickname);

    UserInfoVO getNicknameAndUsername();

    Long getDefaultWordBookIdByCurrentID(Long currentId);

    void updateDefaultWordBookId(Long currentId, Long wordBookId);

    DefaultWorkBookVO getDefaultWordBookNameByCurrentID();

    ShowStatsVO showStats();

    String getPermission();
}
