package site.yanbin.mapper;

import org.apache.ibatis.annotations.*;
import site.yanbin.common.dto.RegisterUserDTO;
import site.yanbin.common.dto.UpdatePasswordDTO;
import site.yanbin.common.entity.User;
import site.yanbin.common.vo.DefaultWorkBookVO;
import site.yanbin.common.vo.UserInfoVO;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from user where username = #{username} and password = #{password}")
    User getLoginUserByUser(User user);

    @Select("SELECT default_word_book_id FROM user WHERE id = #{currentId}")
    long getDefaultWordBookIdByCurrentID(Long currentId);

    @Select("SELECT id FROM user")
    List<Long> getAllIdList();


    @Update("UPDATE user SET password = #{newPassword} WHERE username = #{username} and password = #{oldPassword}")
    void updatePassword(UpdatePasswordDTO updatePasswordDTO);

    @Select("SELECT COUNT(*) FROM user WHERE username = #{username}")
    int checkUsernameIsExist(String username);



    @Insert("INSERT INTO user (username, password, email,role) VALUES (#{username}, #{password}, #{email},'USER')")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void register(User user);

    @Update("UPDATE user SET default_word_book_id = #{wordBookId} WHERE id = #{userId}")
    void updateDefaultWordBookId(@Param("userId") Long userId, @Param("wordBookId") Long wordBookId);

    @Update("UPDATE user SET avatar_url = #{imageUrl} WHERE id = #{currentId}")
    void updateAvatarUrl(@Param("imageUrl") String imageUrl, @Param("currentId") Long currentId);

    @Select("SELECT avatar_url FROM user WHERE id = #{currentId}")
    String getAvatarUrl(Long currentId);

    @Update("UPDATE user SET nickname = #{nickname} WHERE id = #{currentId}")
    void updateNickname(@Param("nickname") String nickname,@Param("currentId") Long currentId);

    @Select("SELECT nickname,username FROM user WHERE id = #{currentId}")
    UserInfoVO getNicknameAndUsername(Long currentId);

    @Select("SELECT id,name FROM word_book WHERE id = #{currentId}")
    DefaultWorkBookVO getDefaultWordBookNameByCurrentID(Long currentId);
}
