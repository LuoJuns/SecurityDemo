package com.securitydemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.securitydemo.entity.DUser;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper extends BaseMapper<DUser> {
}
