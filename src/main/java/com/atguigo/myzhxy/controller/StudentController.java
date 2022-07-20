package com.atguigo.myzhxy.controller;

import com.atguigo.myzhxy.pojo.Student;
import com.atguigo.myzhxy.serviece.StudentService;
import com.atguigo.myzhxy.utils.MD5;
import com.atguigo.myzhxy.utils.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/studentController")
public class StudentController {
    @Autowired
    private StudentService studentService;

    //删除学生信息
    /*DELETE
    http://localhost:8080/sms/studentController/delStudentById*/
    @DeleteMapping("/delStudentById")
    public Result delStudentById(@RequestBody List<Integer> ids){
        studentService.removeByIds(ids);
        return Result.ok();
    }

    //添加或修改
   /* POST
    http://localhost:8080/sms/studentController/addOrUpdateStudent*/
    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(@RequestBody Student student){

        Integer id = Math.toIntExact(student.getId());
        if (id == null || id == 0){
            student.setPassword(MD5.encrypt(student.getPassword()));
        }
        studentService.saveOrUpdate(student);

        return Result.ok();
    }

    //分页查询
    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result getStudentByOpr(@PathVariable Integer pageNo, @PathVariable Integer pageSize, Student student){
        Page<Student> page = new Page<>(pageNo,pageSize);
        IPage<Student> iPage = studentService.getStudentByOpr(page,student);
        return Result.ok(iPage);
    }

}
