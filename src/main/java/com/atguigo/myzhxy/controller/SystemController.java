package com.atguigo.myzhxy.controller;

import com.atguigo.myzhxy.pojo.Admin;
import com.atguigo.myzhxy.pojo.LoginForm;
import com.atguigo.myzhxy.pojo.Student;
import com.atguigo.myzhxy.pojo.Teacher;
import com.atguigo.myzhxy.serviece.AdminService;
import com.atguigo.myzhxy.serviece.StudentService;
import com.atguigo.myzhxy.serviece.TeacherService;
import com.atguigo.myzhxy.utils.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/sms/system")
public class SystemController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    //POST
    //	http://localhost:8080/sms/system/updatePwd/admin/123456
    //修改密码
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(
            @ApiParam("token口令") @RequestHeader("token") String token,
            @ApiParam("旧密码") @PathVariable("oldPwd") String oldPwd,
            @ApiParam("新密码") @PathVariable("newPwd") String newPwd
    ){
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            // token过期
            return Result.fail().message("token失效,请重新登录后修改密码");
        }
        // 获取用户ID和用类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        oldPwd= MD5.encrypt(oldPwd);
        newPwd= MD5.encrypt(newPwd);

        switch (userType) {
            case 1:
                QueryWrapper<Admin> queryWrapper1=new QueryWrapper<>();
                queryWrapper1.eq("id",userId.intValue());
                queryWrapper1.eq("password",oldPwd);
                Admin admin =adminService.getOne(queryWrapper1);
                if (admin != null){
                    // 修改
                    admin.setPassword(newPwd);
                    adminService.saveOrUpdate(admin);
                }else{
                    return Result.fail().message("原密码有误!");
                }
                break;

            case 2:
                QueryWrapper<Student> queryWrapper2=new QueryWrapper<>();
                queryWrapper2.eq("id",userId.intValue());
                queryWrapper2.eq("password",oldPwd);
                Student student =studentService.getOne(queryWrapper2);
                if (student != null){
                    // 修改
                    student.setPassword(newPwd);
                    studentService.saveOrUpdate(student);
                }else{
                    return Result.fail().message("原密码有误!");
                }
                break;
            case 3:
                QueryWrapper<Teacher> queryWrapper3=new QueryWrapper<>();
                queryWrapper3.eq("id",userId.intValue());
                queryWrapper3.eq("password",oldPwd);
                Teacher teacher =teacherService.getOne(queryWrapper3);
                if (teacher != null){
                    // 修改
                    teacher.setPassword(newPwd);
                    teacherService.saveOrUpdate(teacher);
                }else{
                    return Result.fail().message("原密码有误!");
                }
                break;

        }
        return Result.ok();
    }

    //图片的上传和下载
//    POST
//    http://localhost:8080/sms/system/headerImgUpload

    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(@RequestPart("multipartFile") MultipartFile multipartFile){
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String originalFilename = multipartFile.getOriginalFilename();
        int i = originalFilename.lastIndexOf(".");
        String newFileName =uuid.concat(originalFilename.substring(i));

        // 保存文件 将文件发送到第三方/独立的图片服务器上,
        String portraitPath="D:/Codes/myzhxy/target/classes/public/upload/".concat(newFileName);
        try {
            multipartFile.transferTo(new File(portraitPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 响应图片的路径
        String path="upload/".concat(newFileName);
        return Result.ok(path);

    }

    //返回对于登录用户的信息
    @GetMapping("/getInfo")
    public Result getInfo(@RequestHeader("token") String token ) {
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        Map<String, Object> map = new LinkedHashMap<>();
        switch (userType) {
            case 1:
                Admin admin = adminService.getAdminById(userId);
                map.put("userType",1);
                map.put("user",admin);
                break;
            case 2:
               Student student = studentService.getStudentById(userId);
                map.put("userType",2);
                map.put("user",student);
                break;
            case 3:
                Teacher teacher = teacherService.getTeacherById(userId);
                map.put("userType",3);
                map.put("user",teacher);
                break;
        }

        return Result.ok(map);
    }


    //登录
    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm, HttpServletRequest request){
        // 验证码校验
        HttpSession session = request.getSession();
        String sessionVerifiCode = (String)session.getAttribute("verifiCode");
        String loginVerifiCode = loginForm.getVerifiCode();
        if("".equals(sessionVerifiCode) || null == sessionVerifiCode){
            return Result.fail().message("验证码失效,请刷新后重试");
        }
        if (!sessionVerifiCode.equalsIgnoreCase(loginVerifiCode)){
            return Result.fail().message("验证码有误,请仔细输入后重试");
        }
        // 从session域中移除现有验证码
        session.removeAttribute("verifiCode");
        // 分用户类型进行校验


        // 准备一个map用户存放响应的数据
        Map<String,Object> map=new LinkedHashMap<>();
        switch (loginForm.getUserType()){
            case 1:
                try {
                    Admin admin=adminService.login(loginForm);
                    if (null != admin) {
                        // 用户的类型和用户id转换成一个密文,以token的名称向客户端反馈
                        map.put("token",JwtHelper.createToken(admin.getId(), 1));
                    }else{
                        throw new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 2:
                try {
                    Student student =studentService.login(loginForm);
                    if (null != student) {
                        // 用户的类型和用户id转换成一个密文,以token的名称向客户端反馈
                        map.put("token",JwtHelper.createToken(student.getId(), 2));
                    }else{
                        throw new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 3:
                try {
                    Teacher teacher =teacherService.login(loginForm);
                    if (null != teacher) {
                        // 用户的类型和用户id转换成一个密文,以token的名称向客户端反馈
                        map.put("token",JwtHelper.createToken(teacher.getId(), 3));
                    }else{
                        throw new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
        }
        return Result.fail().message("该用户不存在");

    }


    //验证码
    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response){
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
        String verifiCode = new String(CreateVerifiCodeImage.getVerifiCode());
        HttpSession session = request.getSession();
        session.setAttribute("verifiCode",verifiCode);
        try {
            ImageIO.write(verifiCodeImage,"JPEG",response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
