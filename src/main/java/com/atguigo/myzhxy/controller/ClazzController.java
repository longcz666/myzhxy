package com.atguigo.myzhxy.controller;

import com.atguigo.myzhxy.pojo.Clazz;
import com.atguigo.myzhxy.serviece.ClazzService;
import com.atguigo.myzhxy.utils.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/clazzController")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

   //sms/clazzController/getClazzs
    //查询所有班级
    @GetMapping("/getClazzs")
    public Result getClazzs(){
        List<Clazz> list = clazzService.list();
        return Result.ok(list);
    }

    //分页查询
    @GetMapping("/getClazzsByOpr/{pageNo}/{pageSize}")
    public Result getClazzByOpr(@PathVariable Integer pageNo, @PathVariable Integer pageSize, Clazz clazz){

        Page<Clazz> page = new Page<>(pageNo,pageSize);
        IPage<Clazz> iPage = clazzService.getClazzsByOpr(page,clazz);
        return Result.ok(iPage);
    }

    //新增或修改
    @PostMapping("/saveOrUpdateClazz")
    public Result saveOrUpdateClazz(@RequestBody Clazz clazz){
        clazzService.saveOrUpdate(clazz);
        return Result.ok("添加或修改成功");
    }

    //删除
    	//sms/clazzController/deleteClazz
    @DeleteMapping("/deleteClazz")
    public Result deleteClazz(@RequestBody List<Integer> ids){
        clazzService.removeByIds(ids);
        return Result.ok("删除成功");
    }
}
