package site.yanbin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import site.yanbin.common.dto.*;
import site.yanbin.common.entity.Question;
import site.yanbin.common.result.PageResult;
import site.yanbin.common.result.Result;
import site.yanbin.service.QuestionService;

@RestController
@Api(tags = "问题模块")
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @ApiOperation("随机获取一个问题")
    @GetMapping("/random")
    public Result getRandomQuestion()

    {
        return  Result.success(questionService.getRandomQuestion());
    }

    @ApiOperation("添加问题")
    @PostMapping("/add")
    public Result add(@RequestBody QuestionAddDTO questionAddDTO){

        questionService.add(questionAddDTO);
        return Result.success();
    }

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public Result<PageResult> pageQuery(QuestionPageQueryDTO questionPageQueryDTO)//这是前端传过来的不是json数据，所以不需要加requestbody注解
    {
        PageResult pageresult= questionService.pageQuery(questionPageQueryDTO);
        return Result.success(pageresult);
    }

    @ApiOperation("更新问题")
    @PutMapping("/update")
    public Result update(@RequestBody QuestionUpdateDTO questionUpdateDTO)
    {
        questionService.update(questionUpdateDTO);
        return Result.success();
    }

    @ApiOperation("删除问题")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id)
    {
        questionService.delete(id);
        return Result.success();
    }


    //还有三个接口
    //根据id查询问题，但是好像不用啊，前端不是自带吗？，但是还是这样子写比较好，比较规范一点
//    @GetMapping("/{id}")
//    public Result<ChoiceQuestion> getById(@PathVariable Long id)
//    {
//        ChoiceQuestion choiceQuestion=choiceQuestionService.getById(id);
//        return Result.success(choiceQuestion);
//    }
//
//    @PutMapping("/updateNote")
//    public Result updateNote(@RequestBody NoteDTO noteDTO){
//        choiceQuestionService.updateNote(noteDTO.getId(),noteDTO.getNote());
//        return Result.success();
//    }
//
//    @PutMapping("/updateCorrectPersonalCount")
//    public Result updateCorrectPersonalCount(@RequestBody CorrectPersonalCountDTO correctPersonalCountDTO){
//        choiceQuestionService.updateCorrectPersonalCount(correctPersonalCountDTO.getId(),correctPersonalCountDTO.getCorrectPersonalCount());
//        return Result.success();
//    }

    @ApiOperation("根据给定题目id查询问题")
    @GetMapping("/getQuestion/{id}")
    public Result getQuestionById(@PathVariable Long id)
    {
        return Result.success(questionService.getQuestionById(id));
    }

    //如果这两个接口合二为一的话就是一个接口了，那么就是前端提交进来的dto是什么，我们后端就按照去改就好了
//修改答对次数（同时也是可以只修改总次数的）
    @ApiOperation("修改用户问题统计")
@PutMapping("/updateUserQuestionStats")
public Result updateUserQuestionStats(@RequestBody UserQuestionStatsDTO userQuestionStatsDTO){
    questionService.updateUserQuestionStats(userQuestionStatsDTO.getId(),userQuestionStatsDTO.getCorrectAnswerCount(),userQuestionStatsDTO.getTotalAnswerCount());
    return Result.success();
}

    //修改笔记的接口
    @ApiOperation("修改笔记")
    @PutMapping("/updateNote")
    public Result updateNote(@RequestBody NoteDTO noteDTO){
        questionService.updateNote(noteDTO.getId(),noteDTO.getNote());
        return Result.success();
    }


    //再做两个功能，批量导入问题呵批量导出词书问题至txt文本

//    //1//TODO批量导入问题
//    @ApiOperation("批量导入问题")
//    @PostMapping("/batchImport")
//    public Result batchImport(@RequestBody BatchImportDTO batchImportDTO){
//        questionService.batchImport(batchImportDTO);
//        return Result.success();
//    }
//
//    //2//批量导出问题
//    @ApiOperation("批量导出问题")
//    @PostMapping("/batchExport")
//    public Result batchExport(@PathVariable Long wordBookId){
//        questionService.batchExport(wordBookId);
//        return Result.success();
//    }
}
