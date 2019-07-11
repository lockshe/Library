package ynu.dao.sqlprovider;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;

@Component
public class FileSqlProvider {
	
	//�����ļ����Ȩ��
    public String updateAuthById() {
        return CommonSqlProvider.updateAuthById("file");
    }

    public String getBasicBy() {
        return getBaseSql(false);
    }
    
    //�û���ȡ�����ļ�
    public String getViewAll(@Param("search") final String search) {
    	return new SQL() {{
       	 SELECT("distinct f.id,f.user_id,u.username,u.avatar,f.name file_name,f.size,f.create_time,c.name " +
                 "category_name,f"
                 + ".description,f.tag,f.check_times,f.download_times,f.visit_url,f.is_uploadable,f.is_deletable,"
                 + "f.is_updatable,f.is_downloadable,f.is_visible");
         FROM("file f");
         JOIN("user u on u.id=f.user_id");//�ļ�����û�������
         JOIN("category c on c.id=f.category_id");//�ļ���ͷ��������
         WHERE("f.is_visible=1");
         if(search!=null&&search!="") {
        	 WHERE("f.name like '%" + search + "%' or u.username like '%" + search + "%' or category_name like '" +
                     search + "'");
         }
         ORDER_BY("id");
    	}}.toString();
    }
    
    //�û���ȡ�Լ����ļ�
    public String getMine(@Param("id")final int id,@Param("search") final String search) {
    	return new SQL() {{
       	 SELECT("distinct f.id,f.user_id,u.username,u.avatar,f.name file_name,f.size,f.create_time,c.name " +
                 "category_name,f"
                 + ".description,f.tag,f.check_times,f.download_times,f.visit_url,f.is_uploadable,f.is_deletable,"
                 + "f.is_updatable,f.is_downloadable,f.is_visible");
         FROM("file f");
         JOIN("user u on u.id=f.user_id");//�ļ�����û�������
         JOIN("category c on c.id=f.category_id");//�ļ���ͷ��������
         if(search!=null&&search!="") {
        	 WHERE("f.name like '%" + search + "%' or category_name like '" +
                     search + "'");
         }
         WHERE("f.is_visible=1 and u.id="+id);
         ORDER_BY("id");
    	}}.toString();
    }
    
    
    //����Ա��ȡ�����ļ�
    private String getBaseSql(final boolean isDownloaded) {
        return new SQL() {{
        	 SELECT("distinct f.id,f.user_id,u.username,u.avatar,f.name file_name,f.size,f.create_time,c.name " +
                     "category_name,f"
                     + ".description,f.tag,f.check_times,f.download_times,f.visit_url,f.is_uploadable,f.is_deletable,"
                     + "f.is_updatable,f.is_downloadable,f.is_visible");
             FROM("file f");
             JOIN("user u on u.id=f.user_id");//�ļ�����û�������
             JOIN("category c on c.id=f.category_id");//�ļ���ͷ��������
             ORDER_BY("id");
        }}.toString();
    }
}
