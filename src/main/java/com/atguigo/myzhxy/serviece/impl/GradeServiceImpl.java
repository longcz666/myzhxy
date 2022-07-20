package com.atguigo.myzhxy.serviece.impl;

import com.atguigo.myzhxy.mapper.GradeMapper;
import com.atguigo.myzhxy.pojo.Grade;
import com.atguigo.myzhxy.serviece.GradeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements GradeService {
    @Override
    public IPage<Grade> getGradeByOpr(Page<Grade> page, String gradeName) {
        QueryWrapper<Grade> qw = new QueryWrapper<>();
        if (StringUtils.hasText(gradeName)) {
            qw.like("name",gradeName);
        }
        qw.orderByDesc("id");
        return baseMapper.selectPage(page, qw);
    }
}
