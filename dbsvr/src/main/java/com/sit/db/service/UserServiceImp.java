package com.sit.db.service;

import com.github.pagehelper.PageHelper;
import com.sit.entity.*;
import mapper.RoleMapper;
import mapper.UserExtendMapper;
import mapper.UserMapper;
import mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by zhenglin on 2018/9/12.
 */
@Service(value = "UserService")
public class UserServiceImp implements UserService{
    @Autowired
    private UserMapper userMapper;//这里会报错，但是并不会影响
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private UserExtendMapper userExtendMapper;
    /*
    * 这个方法中用到了我们开头配置依赖的分页插件pagehelper
    * 很简单，只需要在service层传入参数，然后将参数传递给一个插件的一个静态方法即可；
    * pageNum 开始页数
    * pageSize 每页显示的数据条数
    * */
    @Override
    public List<User> findAllUser(int pageNum, int pageSize) {
        //将参数传给这个方法就可以实现物理分页了，非常简单。
        PageHelper.startPage(pageNum, pageSize);
        return userMapper.selectByExample(null);
    }

    @Override
    public Map<String, Object> getUserInfo(String account) {
        Map<String, Object> retmap = new HashMap<String, Object>();
        UserExample ex = new UserExample();
        UserExample.Criteria crt = ex.createCriteria();
        crt.andAccountEqualTo(account);
        List<com.sit.entity.User> list = userMapper.selectByExample(ex);
        if(list != null && list.size() > 0)
        {
            com.sit.entity.User u = list.get(0);
            retmap.put("id", u.getId());
            retmap.put("account", u.getAccount());
            retmap.put("password", u.getPassword());
            retmap.put("username", u.getUsername());
            List<UserRole> urlist = getUserRole(u.getId());
            List<Integer> idlist = new ArrayList<>();
            for(UserRole ur:urlist)
            {
                idlist.add(ur.getRoleId());
            }

            List<Role> rlist = getRole(idlist);
            List<String> roleNameList = new ArrayList<String>();
            for(Role r:rlist)
            {
                roleNameList.add(r.getName());
            }

            retmap.put("roles", roleNameList);
            List<UserExtend> uelist = getUserExtend(u.getId());
            if(uelist != null && uelist.size() > 0)
            {
                retmap.put("tel", uelist.get(0).getMobile());
            }
        }

        return retmap;
    }

    @Override
    public List<UserRole> getUserRole(int uid)
    {
        UserRoleExample ex = new UserRoleExample();
        UserRoleExample.Criteria crt = ex.createCriteria();
        crt.andUserIdEqualTo(uid);
        List<UserRole> list = userRoleMapper.selectByExample(ex);
        return list;
    }

    @Override
    public List<Role> getRole(List<Integer> idlist)
    {
        RoleExample ex = new RoleExample();
        RoleExample.Criteria crt = ex.createCriteria();
        crt.andIdIn(idlist);
        List<Role> list = roleMapper.selectByExample(ex);
        return list;
    }

    @Override
    public List<UserExtend> getUserExtend(int uid)
    {
        UserExtendExample ex = new UserExtendExample();
        UserExtendExample.Criteria crt = ex.createCriteria();
        crt.andUseridEqualTo(uid);
        List<UserExtend> list = userExtendMapper.selectByExample(ex);
        return list;
    }
}
