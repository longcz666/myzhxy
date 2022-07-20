package com.atguigo.myzhxy.controller;

import com.atguigo.myzhxy.pojo.Admin;
import com.atguigo.myzhxy.serviece.AdminService;
import com.atguigo.myzhxy.utils.MD5;
import com.atguigo.myzhxy.utils.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/adminController")
public class AdminController {
    @Autowired
    private AdminService adminService;
    //GET
    //http://localhost:8080/sms/adminController/getAllAdmin/1/3
    @GetMapping("/getAllAdmin/{pageNo}/{pageSize}")
    public Result getAllAdmin(@PathVariable Integer pageNo, @PathVariable Integer pageSize, String adminName){
        Page<Admin> page = new Page<>(pageNo,pageSize);
        IPage<Admin> iPage = adminService.getAdminByOpr(page,adminName);
        return Result.ok(iPage);
    }

    //POST
    //http://localhost:8080/sms/adminController/saveOrUpdateAdmin
    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(@RequestBody Admin admin){
        Integer id = Math.toIntExact(admin.getId());
        if(id == null || id == 0){
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }
        adminService.saveOrUpdate(admin);
        return Result.ok();
    }

     /*DELETE
    http://localhost:8080/sms/adminController/deleteAdmin*/
    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(@RequestBody List<Integer> ids){
        adminService.removeByIds(ids);
        return Result.ok();
    }
}
