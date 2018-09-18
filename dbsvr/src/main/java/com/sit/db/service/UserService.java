package com.sit.db.service;

import com.sit.entity.Role;
import com.sit.entity.User;
import com.sit.entity.UserExtend;
import com.sit.entity.UserRole;

import java.util.List;
import java.util.Map;

/**
 * Created by zhenglin on 2018/9/12.
 */
public interface UserService {

    List<User> findAllUser(int pageNum, int pageSize);
    Map<String, Object> getUserInfo(String account);
    List<UserRole> getUserRole(int uid);
    List<Role> getRole(List<Integer> idlist);
    List<UserExtend> getUserExtend(int uid);
}
