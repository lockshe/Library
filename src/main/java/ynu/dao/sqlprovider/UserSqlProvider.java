package ynu.dao.sqlprovider;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;


public class UserSqlProvider {
	
	//更新用户表的权限
    public String updateAuthById() {
        return CommonSqlProvider.updateAuthById("user");
    }

    public String getUserBy(@Param("permission") final int permission, @Param("condition") final String condition) {
        String sql = new SQL() {{
            SELECT("*");
            FROM("user");
            if (permission == 3) {
                WHERE("permission<3");
            } else if (permission == 2) {
                WHERE("permission<2");
            } else {
                WHERE("permission<0");
            }
            if (condition!=null) {
                WHERE("username like '%" + condition + "%' or email like '%" + condition + "%' or real_name like '" +
                        condition + "'");
            }
            ORDER_BY("id");
        }}.toString();
        
       // 分页查询操作
       // int size = Application.settings.getIntegerUseEval(ConfigConsts.USER_PAGE_SIZE_OF_SETTINGS);
       // return sql + " limit " + (offset * size) + "," + size;
        return sql;
    }
}
