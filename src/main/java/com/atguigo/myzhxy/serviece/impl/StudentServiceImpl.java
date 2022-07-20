package com.atguigo.myzhxy.serviece.impl;

import com.atguigo.myzhxy.mapper.StudentMapper;
import com.atguigo.myzhxy.pojo.LoginForm;
import com.atguigo.myzhxy.pojo.Student;
import com.atguigo.myzhxy.serviece.StudentService;
import com.atguigo.myzhxy.utils.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Override
    public Student getStudentById(Long userId) {
        QueryWrapper<Student> qw = new QueryWrapper<>();
        qw.eq("id",userId);
        return baseMapper.selectOne(qw);
    }

    @Override
    public Student login(LoginForm loginForm) {
        QueryWrapper<Student> qw = new QueryWrapper<>();
        qw.eq("name",loginForm.getUsername());
        qw.eq("password", MD5.encrypt(loginForm.getPassword()));
        return baseMapper.selectOne(qw);
    }

    @Override
    public IPage<Student> getStudentByOpr(Page<Student> page, Student student) {
        QueryWrapper<Student> qw = new QueryWrapper<>();
        if (StringUtils.hasText(student.getName())) {
            qw.eq("name", student.getName());
        }
        if (StringUtils.hasText(student.getClazzName())){
            qw.eq("clazz_name", student.getClazzName());
        }

        qw.orderByDesc("id");
        return baseMapper.selectPage(page, qw);
    }
}
