package com.atguigo.myzhxy.serviece.impl;

import com.atguigo.myzhxy.mapper.ClazzMapper;
import com.atguigo.myzhxy.pojo.Clazz;
import com.atguigo.myzhxy.serviece.ClazzService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements ClazzService {
    @Override
    public IPage<Clazz> getClazzsByOpr(Page<Clazz> page, Clazz clazz) {
        QueryWrapper<Clazz> qw = new QueryWrapper<>();
        String gradeName = clazz.getGradeName();
        if (StringUtils.hasText(gradeName)){
            qw.like("grade_name",gradeName);
        }
        String name = clazz.getName();
        if (StringUtils.hasText(name)){
            qw.like("name",name);
        }
        qw.orderByDesc("id");
        return baseMapper.selectPage(page, qw);
    }
}
