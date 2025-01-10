package site.yanbin.service;

import org.springframework.stereotype.Service;
import site.yanbin.common.dto.PublicWordBookPageQueryDTO;
import site.yanbin.common.dto.UpdateIsPublicDTO;
import site.yanbin.common.dto.WordBookAddDTO;
import site.yanbin.common.dto.WordBookUpdateDTO;
import site.yanbin.common.result.PageResult;
import site.yanbin.common.result.Result;
import site.yanbin.common.vo.WordBookVO;

import java.util.List;

@Service
public interface WordBookService {
    void add(WordBookAddDTO wordBookAddDTO);

    void update(WordBookUpdateDTO wordBookUpdateDTO);

    void delete(Long id);

    List<WordBookVO> getWordBookListByUserId();

    Integer getIsPublicByWordBookId(Long id);

    void updateIsPublic(UpdateIsPublicDTO updateIsPublicDTO);

    PageResult pageQuery(PublicWordBookPageQueryDTO publicWordBookPageQueryDTO);

    Result addWordBookByShareCode(String shareCode);
}
