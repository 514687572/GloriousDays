package com.stip.net.user.dao;

import com.stip.mybatis.generator.plugin.GenericMapper;
import com.stip.net.common.entity.Users;
import com.stip.net.example.UsersExample;

 /**
 * 可添加自定义查询语句，方便后续扩展
 **/
public interface UsersDao extends GenericMapper<Users, UsersExample, Integer> {
}