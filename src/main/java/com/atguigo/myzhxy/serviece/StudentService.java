package com.atguigo.myzhxy.serviece;

import com.atguigo.myzhxy.pojo.LoginForm;
import com.atguigo.myzhxy.pojo.Student;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

public interface StudentService extends IService<Student> {
    Student getStudentById(Long userId);

    Student login(LoginForm loginForm);

    IPage<Student> getStudentByOpr(Page<Student> page, Student student);
}
