package com.securitydemo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("User")
@AllArgsConstructor
@NoArgsConstructor
public class DUser {
    private String username;
    private String password;
}
