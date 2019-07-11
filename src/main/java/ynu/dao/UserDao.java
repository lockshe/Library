package ynu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.springframework.stereotype.Repository;

import ynu.dao.sqlprovider.UserSqlProvider;
import ynu.entity.User;

@Repository
public interface UserDao {
	
	//����ID�����û�ͷ��·��
	@Update("update user set avatar=#{avatar} where id=#{id}")
	boolean updateAvatarById(@Param("id")int id,@Param("avatar")String avatar);
	
	
	//����username��ȡ�û�ͷ��·��
	@Select("select avatar from user where username=#{username}")
	String getAvatarByUsername(@Param("username")String username);

	/**
     * �����û�Ȩ��
     *
     * @param id �û����
     * @param permission Ȩ��
     *
     * @return �Ƿ���³ɹ�
     */
    @Update("update user set permission=#{permission} where id=#{id}")
    boolean updatePermission(@Param("id") int id, @Param("permission") int permission);

    
    
    
    /**
     * �ù��û�������Ҫ�û������ȡ�û�Id
     *
     * @param usernameOrEmail �û���������
     *
     * @return �û����
     */
    @Select("select id from user where username=#{usernameOrEmail} or email=#{usernameOrEmail}")
    int getUserId(String usernameOrEmail);

    
    
    /**
     * ͨ��ID�����û�������Ϣ
     *
     * @param id ���
     * @param avatar ͷ��
     * @param realName ��ʵ����
     * @param email ����
     *
     * @return �Ƿ���³ɹ�
     */
    @Update("update user set real_name=#{realName},email=#{email} where id=#{id}")
    boolean updateBasicInfo(@Param("id") int id, @Param("realName") String realName,
                            @Param("email") String email);

    
    
    
    /**
     * ͨ��id��ȡһ���û�
     *
     * @param id ���
     *
     * @return {@link User}
     */
    @Select("select * from user where id=#{id}")
    User getUserById(int id);

    
    
    /**
     * ͨ��Ȩ�޻�ȡ�û�
     *
     * @param permission Ȩ��
     * @param condition ����
     * @param offset ƫ��
     *
     * @return {@link List}
     */
    @SelectProvider(type = UserSqlProvider.class, method = "getUserBy")
    List<User> listUserBy(@Param("permission") int permission, @Param("condition") String condition);

    
    
    
    /**
     * �û���¼
     *
     * @param usernameOrEmail �û���
     * @param password ���룬sha2�����㷨
     *
     * @return {@link User}
     */
    @Select("select * from user where (username=#{usernameOrEmail} or email=#{usernameOrEmail}) and password=#{password}")
    User login(@Param("usernameOrEmail") String usernameOrEmail, @Param("password") String password);

    
 
    /**
     * ���һ���û�
     *
     * @param user {@link User}
     *
     * @return �Ƿ����ɹ�
     * 
     * ���ݿ���������sha2���ܣ����Ǵ������ݵĹ�������Ȼ�������Ľ��д����
     */
    @Insert("insert into user(username,real_name,email,password,is_downloadable,is_uploadable,is_deletable," +
            "is_updatable,is_visible) values(#{username},#{realName},#{email},#{password}," +
            "#{isDownloadable},#{isUploadable},#{isDeletable},#{isUpdatable},#{isVisible})")
    boolean insertUser(User user);

    
    /**
     * ͨ��id�����û���¼ʱ��
     *
     * @param id ���
     *
     * @return {@link Boolean}
     */
    @Update("update user set last_login_time=current_timestamp where id=#{id}")
    boolean updateUserLoginTime(int id);

    
    
    
    
    /**
     * ���²����û�Ȩ��
     *
     * @param id �û����
     * @param isDownloadable ����Ȩ��
     * @param isUploadable �ϴ�Ȩ��
     * @param isVisible �ɲ�Ȩ��
     * @param isDeletable ɾ��Ȩ��
     * @param isUpdatable ����Ȩ��
     *
     * @return {@link Boolean}
     */
    @UpdateProvider(type = UserSqlProvider.class, method = "updateAuthById")
    boolean updateAuthById(@Param("id") int id, @Param("isDownloadable") int isDownloadable,
                           @Param("isUploadable") int isUploadable,  @Param("isVisible") int isVisible,@Param("isDeletable") int isDeletable, @Param(
                                   "isUpdatable") int isUpdatable);

    
    
    /**
     * ͨ����Ÿ�������
     *
     * @param id ���
     * @param password ����
     *
     * @return {@link Boolean}
     */
    @Update("update user set password=#{password} where id=#{id}")
    boolean updatePasswordById(@Param("id") int id, @Param("password") String password);

    /**
     * ͨ�������������
     *
     * @param password ����
     * @param email ����
     *
     * @return {@link Boolean}
     */
    @Update("update user set password=#{password} where email=#{email}")
    boolean updatePasswordByEmail(@Param("password") String password, @Param("email") String email);

    
    
    
    /**
     * ����û���
     *
     * @param username �û���
     *
     * @return {@link Integer}
     */
    @Select("select count(*) from user where username=#{username}")
    int checkUsername(String username);

    
    
    
    /**
     * �������
     *
     * @param email ����
     *
     * @return {@link Integer}
     */
    @Select("select count(*) from user where email=#{email}")
    int checkEmail(String email);
    
    
    
}
