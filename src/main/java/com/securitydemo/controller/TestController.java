package com.securitydemo.controller;

import com.securitydemo.entity.DUser;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/hello")
    public String hello() {
        return "hello security!";
    }

    @RequestMapping("/index")
    public String index() {
        return "hello index!";
    }

    @RequestMapping("/update")
    @Secured({"ROLE_boss"})//注解的角色权限控制
    public String update() {
        return "hello update!";
    }

    @RequestMapping("/delete")
    @PreAuthorize("hasAnyAuthority('ROLE_employee')")//@PostAuthorize用的很少，在执行方法之后再做角色判断
    public String delete() {
        return "hello delete!";
    }

    @RequestMapping("/preFilter")
    @PreAuthorize("hasAnyAuthority('ROLE_employee','ROLE_boss')")
    @PreFilter(value = "filterObject.username == 'admin1'")//方法执行前过滤参数内容
    @ResponseBody
    public List<DUser> preFilter(@RequestBody List<DUser> users) {
        for (DUser user : users) {
            System.out.println(user.getUsername()+" "+user.getPassword());
        }
        return users;
    }

    @RequestMapping("/postFilter")
    @PreAuthorize("hasAnyAuthority('ROLE_employee','ROLE_boss')")
    @PostFilter("filterObject.username == 'admin1'")//方法执行后过滤结果内容
    public List<DUser> postFilter() {
        ArrayList<DUser> users = new ArrayList<>();
        users.add(new DUser("admin1","123456"));
        users.add(new DUser("admin2","123456"));
        System.out.println(users);
        return users;
    }
}
