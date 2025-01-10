package site.yanbin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import site.yanbin.allGlobal.util.CosUtils;
import site.yanbin.allGlobal.util.JWTUtils;
import site.yanbin.common.context.BaseContext;
import site.yanbin.common.dto.RegisterUserDTO;
import site.yanbin.common.dto.UpdatePasswordDTO;
import site.yanbin.common.entity.User;
import site.yanbin.common.result.Result;
import site.yanbin.service.UserService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Api(tags = "用户模块")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private CosUtils cosUtils;
    //这几个接口都很简单的，确实算很简单的了

    //注册,同时的话记得生成一个词书，并且把用这个用户的词书绑定到这个默认词书上面去
    //这里要改逻辑，不登录的话拿不到id
    @ApiOperation(value = "注册")
    @PostMapping("/register")
    public Result register(@RequestBody RegisterUserDTO registerUserDTO){
        return userService.register(registerUserDTO);
    }


    //修改密码
    @ApiOperation(value = "修改密码")
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO){
        userService.updatePassword(updatePasswordDTO);
        return Result.success();
    }





    @ApiOperation("登录")
    @PostMapping("/login")
    public Result login(@RequestBody User user) throws Exception {
        User loginuser = userService.getLoginUserByUser(user);
        if(loginuser != null){
            // 登录成功了：生成JWT令牌给前端网页。
            Map<String, Object> claims = new HashMap();
            claims.put("id", loginuser.getId());
            claims.put("username", loginuser.getUsername());

            String jwtToken = jwtUtils.createJWT(claims);
            // 响应给前端浏览器
            return Result.success(jwtToken);
        }
        // 登录失败！
        return  Result.error("NOT_LOGIN");
    }


    // 上传头像
    @ApiOperation(value = "更新用户头像")
    @PostMapping("/upload")
    public Result upload(MultipartFile image) throws Exception {
        // 把图片存储到腾讯云 COS 服务器上面去
        String imageUrl = cosUtils.upload(image);
        //这里就直接调用serviceImpl方法updateAvatarUrl了
        userService.updateAvatarUrl(imageUrl);
        return Result.success();
    }

    //这里再加一个获取用户头像地址的接口
    @ApiOperation("获取用户头像地址")
    @GetMapping("/getAvatarUrl")
    public Result getAvatarUrl(){
        return Result.success(userService.getAvatarUrl());
    }

    //获取用户昵称

    @ApiOperation("获取用户昵称和用户名")
    @GetMapping("/getNicknameAndUsername")
    public Result getNicknameAndUsername(){
        return Result.success(userService.getNicknameAndUsername());
    }


    //修改用户昵称
    @ApiOperation("修改用户昵称")
    @PutMapping("/updateNickname")
    public Result updateNickname(@RequestParam String nickname) {
        userService.updateNickname(nickname);
        return Result.success();
    }

    //根据用户id获取用户默认词书
    @ApiOperation("根据用户id获取用户默认词书")
    @GetMapping("/getDefaultWordBookIdByCurrentID")
    public Result getDefaultWordBookIdByCurrentID(){
        return Result.success(userService.getDefaultWordBookIdByCurrentID(BaseContext.getCurrentId()));
    }

    //修改默认词书
    @ApiOperation("修改默认词书")
    @PutMapping("/updateDefaultWordBookId")
    public Result updateDefaultWordBookId(@RequestParam Long wordBookId){
        userService.updateDefaultWordBookId(BaseContext.getCurrentId(),wordBookId);
        return Result.success();
    }

    //根据用户id获取默认词书名字
    @ApiOperation("根据用户id获取默认词书名字")
    @GetMapping("/getDefaultWordBookIdAndNameByCurrentID")
    public Result getDefaultWordBookNameByCurrentID(){
        return Result.success(userService.getDefaultWordBookNameByCurrentID());
    }


    //返回一个展示数据的接口
    @ApiOperation("展示数据")
    @GetMapping("/showStats")
    public Result showStats(){
        return Result.success(userService.showStats());
    }

    //获取用户对其默认词书的权限是WRITE还是READ
    @ApiOperation("获取用户对当前默认词书的权限是WRITE还是READ")
    @GetMapping("/getPermission")
    public Result getPermission(){
        return Result.success(userService.getPermission());
    }

}
