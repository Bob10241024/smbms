package com.xiao.dao.role;

import com.xiao.pojo.Role;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * 为了我们职责统一，我们把角色的操作单独放在一个包中，和pojo类对应
 */
public interface RoleDao {
    //获取角色列表
    public List<Role> getRoleList(Connection connection) throws SQLException;
}
