package com.atguigo.myzhxy.controller;

import com.atguigo.myzhxy.pojo.Grade;
import com.atguigo.myzhxy.serviece.GradeService;
import com.atguigo.myzhxy.utils.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "年级控制器")
@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @ApiOperation("查询全部年级")
    @GetMapping("/getGrades")
    public Result getGrades(){
        List<Grade> list = gradeService.list();
          return Result.ok(list);
    }

    //新增或修改年级
    @ApiOperation("新增或修改年级，有id是修改")
    @PostMapping("/saveOrUpdateGrade")
    public Result saveOrUpdateGrade(@ApiParam("年级信息的json对象") @RequestBody Grade grade){

        gradeService.saveOrUpdate(grade);
        return Result.ok("添加成功");
    }

    //删除年级
    @ApiOperation("删除年级")
    @DeleteMapping("/deleteGrade")
    public Result deleteGrade(@ApiParam("删除年级的id的json集合") @RequestBody List<Integer> ids){
        gradeService.removeByIds(ids);
        return Result.ok("删除成功");
    }

    //分页查询
    @ApiOperation("分页查询")
    @GetMapping("/getGrades/{pageNO}/{pageSize}")
    public Result getGrades(@PathVariable("pageNO") Integer pageNO,@PathVariable("pageSize") Integer pageSize, String gradeName){
        Page<Grade> page = new Page<>(pageNO, pageSize);
        IPage<Grade> iPage = gradeService.getGradeByOpr(page, gradeName);
        return Result.ok(iPage);
    }
}
