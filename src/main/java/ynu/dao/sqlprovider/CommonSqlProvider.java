package ynu.dao.sqlprovider;

import org.apache.ibatis.jdbc.SQL;

public class CommonSqlProvider {

    private CommonSqlProvider() {}
    	
    /*
     * ±í¸üÐÂÓï¾ä
     * UPDATE [LOW_PRIORITY] [IGNORE] table_name 
	SET 
    column_name1 = expr1,
    column_name2 = expr2,
    ...
	WHERE
    condition;

     */
    public static String updateAuthById(final String table) {
        return new SQL() {{
            UPDATE(table);
            SET("is_downloadable=#{isDownloadable}");
            SET("is_uploadable=#{isUploadable}");
            SET("is_deletable=#{isDeletable}");
            SET("is_updatable=#{isUpdatable}");
            SET("is_visible=#{isVisible}");
            WHERE("id=#{id}");
        }}.toString();
    }
}
