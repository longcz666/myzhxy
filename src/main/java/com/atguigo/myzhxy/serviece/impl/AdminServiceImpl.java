package com.atguigo.myzhxy.serviece.impl;

import com.atguigo.myzhxy.mapper.AdminMapper;
import com.atguigo.myzhxy.pojo.Admin;
import com.atguigo.myzhxy.pojo.LoginForm;
import com.atguigo.myzhxy.serviece.AdminService;
import com.atguigo.myzhxy.utils.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Override
    public Admin login(LoginForm loginForm) {
        QueryWrapper<Admin> qw = new QueryWrapper<>();
        qw.eq("name",loginForm.getUsername());
        qw.eq("password", MD5.encrypt(loginForm.getPassword()));
        return baseMapper.selectOne(qw);
    }

    @Override
    public Admin getAdminById(Long userId) {
        QueryWrapper<Admin> qw = new QueryWrapper<>();
        qw.eq("id",userId);
        return baseMapper.selectOne(qw);
    }

    @Override
    public IPage<Admin> getAdminByOpr(Page<Admin> page, String adminName) {
        QueryWrapper<Admin> qw = new QueryWrapper<>();
        if (StringUtils.hasText(adminName)){
            qw.like("name",adminName);
        }
        qw.orderByDesc("id");
        Page<Admin> page1 = baseMapper.selectPage(page, qw);

        return page1;
    }


}
