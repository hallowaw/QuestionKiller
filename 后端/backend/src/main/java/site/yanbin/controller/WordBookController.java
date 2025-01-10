package site.yanbin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.yanbin.common.dto.PublicWordBookPageQueryDTO;
import site.yanbin.common.dto.UpdateIsPublicDTO;
import site.yanbin.common.dto.WordBookAddDTO;
import site.yanbin.common.dto.WordBookUpdateDTO;
import site.yanbin.common.result.Result;
import site.yanbin.service.WordBookService;

@RestController()
@RequestMapping("/wordBook")
@Api(tags = "词书模块")
public class WordBookController {

    @Autowired
    private WordBookService wordBookService;
    //增删改查的逻辑一个一个的来

    //新增词书l,新增词书要传过来什么参数呢，首先要是词书名称，词书描述，词书是否公开
    @ApiOperation("新增词书")
    @PostMapping("/add")
    public Result add(@RequestBody WordBookAddDTO wordBookAddDTO){
        wordBookService.add(wordBookAddDTO);

        return Result.success();
    }

    //修改词书
    @ApiOperation("修改词书")
    @PutMapping("/update")
    public Result update(@RequestBody WordBookUpdateDTO wordBookUpdateDTO){
        wordBookService.update(wordBookUpdateDTO);

        return Result.success();
    }

    //删除词书接口，要注意两个地方，1.先检查是不是用户的默认词书，默认词书不可以被删除，2.删除词书和用户表的关系表的记录，
    @ApiOperation("删除词书")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id){

        //1.先检查是不是用户的默认词书，默认词书不可以被删除
        wordBookService.delete(id);
        return Result.success();
    }

    //修改用户的默认词书



    //根据用户id返回所属的词书列表
    @ApiOperation("根据用户id返回所属的词书列表")
    @GetMapping("/getWordBookByUserId")
    public Result getWordBookByUserId(){
        return Result.success(wordBookService.getWordBookListByUserId());
    }


    //导入词书，原理是每一个词书有一个唯一的shareCode,我们可以根据这个shareCode来获取词书，看一下这个词书是不是公开状态，如果是的话
    //我们可以在用户和词书的关系表中添加一条记录，这样子，我们就可以获取到这个词书的所有信息了，同时注意户和词书的关系表中的permission是
    //read,也就是不可以增删改,以保证词书的完整性，于此同时我们可以加一个词书导出txt文本的功能，这样子的话用户就可以通过这个功能进行2次修改了
    //然后的话其实目前词书量比较很少，所以其实我们可以做一个公开词书的列表的页面，这样子用户就可以看到所有的公开词书了

    //先写一个分页查询的功能
    @ApiOperation("所有其他人公开词书的分页查询")
    @GetMapping("/publicOthersWordBookPageQuery")
    public Result pageQuery(PublicWordBookPageQueryDTO publicWordBookPageQueryDTO){
        return Result.success(wordBookService.pageQuery(publicWordBookPageQueryDTO));
    }


    //根据shareCode在用户和词书的关系表中添加一条记录
    @ApiOperation("根据shareCode导入词书")
    @PostMapping("/addWordBookByShareCode/{shareCode}")
    public Result addWordBookByShareCode(@PathVariable String shareCode){

        return wordBookService.addWordBookByShareCode(shareCode);
    }





    //然后的话这里还缺一个改变用户公开状态的接口，如果想简单一点的话，就在单独再弄两个接口好了，一个是获取词书公开状态，一个是修改用户公开状态
    //这样子的话稍微有一点麻烦

    //根据词书id获取词书isPublic状态
//    @ApiOperation("根据词书id获取词书isPublic状态")
//    @GetMapping("/getIsPublicByWordBookId/{id}")
//    public Result getIsPublicByWordBookId(@PathVariable Long id){
//        return Result.success(wordBookService.getIsPublicByWordBookId(id));
//    }
//    //然后的话就是修改词书的公开状态，需要传id和isPublic状态
//    @ApiOperation("修改词书的公开状态")
//    @PutMapping("/updateIsPublic")
//    public Result updateIsPublic(@RequestBody UpdateIsPublicDTO updateIsPublicDTO){
//        wordBookService.updateIsPublic(updateIsPublicDTO);
//        return Result.success();
//    }



}
