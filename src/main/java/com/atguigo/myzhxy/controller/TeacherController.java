package com.atguigo.myzhxy.controller;

import com.atguigo.myzhxy.pojo.Teacher;
import com.atguigo.myzhxy.serviece.TeacherService;
import com.atguigo.myzhxy.utils.MD5;
import com.atguigo.myzhxy.utils.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;
    //GET
    //http://localhost:8080/sms/teacherController/getTeachers/1/3
    @GetMapping("/getTeachers/{pageNo}/{pageSize}")
    public Result getTeachers(@PathVariable Integer pageNo, @PathVariable Integer pageSize, Teacher teacher){
        Page<Teacher> page = new Page<>(pageNo,pageSize);
        IPage<Teacher> iPage = teacherService.getTeacherByOpr(page,teacher);
        return Result.ok(iPage);
    }

    //POST
    //http://localhost:8080/sms/teacherController/saveOrUpdateTeacher
    @PostMapping("/saveOrUpdateTeacher")
    public Result saveOrUpdateTeacher(@RequestBody Teacher teacher){
        Integer id = Math.toIntExact(teacher.getId());
        if(id == null || id == 0){
           teacher.setPassword(MD5.encrypt(teacher.getPassword()));
        }
        teacherService.saveOrUpdate(teacher);
        return Result.ok();
    }

     /*DELETE
    http://localhost:8080/sms/adminController/deleteTeacher*/
     @DeleteMapping("/deleteTeacher")
     public Result deleteTeacher(@RequestBody List<Integer> ids){
         teacherService.removeByIds(ids);
         return Result.ok();
     }
}
