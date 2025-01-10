package site.yanbin.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//其实这边的id换成wordBookId会更好
@Mapper
public interface UserWordBookMapper {


    void insert(@Param("id") Long id, @Param("currentId") Long currentId, @Param("permission") String permission);

    void deleteByWordBookId(Long id);

    List<Long> getAllWordIdList(@Param("userId") Long userId);

    void deleteByWordBookIdAndUserId(@Param("id")Long id,@Param("userId") Long currentId);

    String getPermission(@Param("userId") Long currentId,@Param("wordBookId") Long defaultWordBookId);
}
