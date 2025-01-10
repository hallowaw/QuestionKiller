package site.yanbin.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import site.yanbin.allGlobal.annotation.AutoFill;
import site.yanbin.common.dto.WordBookUpdateDTO;
import site.yanbin.common.entity.Question;
import site.yanbin.common.entity.WordBook;
import site.yanbin.common.enumeration.OperationType;
import site.yanbin.common.vo.WordBookPageVO;
import site.yanbin.common.vo.WordBookVO;

@Mapper
public interface WordBookMapper {
    Integer checkShareCodeIsExist(String shareCode);


    @AutoFill(OperationType.INSERT)
    void insert(WordBook wordBook);

    @AutoFill(OperationType.UPDATE)
    void update(WordBook wordBook);


    void updateIsPublic(Long id);

    WordBookVO getWordBookById(Long wordId);

    void insertAtRegister(WordBook wordBook);

    Integer getIsPublicByWordBookId(Long id);

    void newUpdateIsPublic(@Param("id") Long id, @Param("isPublic") Integer isPublic);

    Long getWordBookIdByShareCode(String shareCode);

    Page<WordBookPageVO> pageQuery(@Param("name") String name);
}
