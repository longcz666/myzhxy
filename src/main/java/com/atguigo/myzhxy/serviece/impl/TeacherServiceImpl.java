package com.atguigo.myzhxy.serviece.impl;

import com.atguigo.myzhxy.mapper.TeacherMapper;
import com.atguigo.myzhxy.pojo.LoginForm;
import com.atguigo.myzhxy.pojo.Teacher;
import com.atguigo.myzhxy.serviece.TeacherService;
import com.atguigo.myzhxy.utils.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {
    @Override
    public Teacher login(LoginForm loginForm) {
        QueryWrapper<Teacher> qw = new QueryWrapper<>();
        qw.eq("name",loginForm.getUsername());
        qw.eq("password", MD5.encrypt(loginForm.getPassword()));
        return baseMapper.selectOne(qw);
    }

    @Override
    public Teacher getTeacherById(Long userId) {
        QueryWrapper<Teacher> qw = new QueryWrapper<>();
        qw.eq("id",userId);
        return baseMapper.selectOne(qw);
    }

    @Override
    public IPage<Teacher> getTeacherByOpr(Page<Teacher> page, Teacher teacher) {
        QueryWrapper<Teacher> qw = new QueryWrapper<>();
        if (StringUtils.hasText(teacher.getName())){
            qw.like("name",teacher.getName());
        }
        if (StringUtils.hasText(teacher.getClazzName())){
            qw.eq("clazz_name",teacher.getClazzName());
        }
        qw.orderByDesc("id");

        return baseMapper.selectPage(page,qw);
    }
}
