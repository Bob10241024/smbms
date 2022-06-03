package com.xiao.dao.role;

import com.xiao.dao.BaseDao;
import com.xiao.pojo.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao{
    /**
     * 获取角色列表
     * @param connection
     * @return
     * @throws SQLException
     */
    @Override
    public List<Role> getRoleList(Connection connection) throws SQLException {

        PreparedStatement pstm = null;
        ResultSet resultSet = null;
        ArrayList<Role> roleList = new ArrayList<>();

        if (connection!=null){
            String sql = "select * from smbms_role";
            Object[] params = {};
            resultSet = BaseDao.execute(connection,pstm,resultSet,sql,params);

            //到时候mybatis就帮我们封装了这些，所以不必担心
            while(resultSet.next()){
                Role _role = new Role();
                _role.setId(resultSet.getInt("id"));
                _role.setRoleCode(resultSet.getString("roleCode"));
                _role.setRoleName(resultSet.getString("roleName"));

                roleList.add(_role);//将数据存在链表里面
            }
            BaseDao.closeResource(null,pstm,resultSet);
        }
        return roleList;
    }
}
