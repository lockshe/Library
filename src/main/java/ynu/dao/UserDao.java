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
	
	//根据ID设置用户头像路径
	@Update("update user set avatar=#{avatar} where id=#{id}")
	boolean updateAvatarById(@Param("id")int id,@Param("avatar")String avatar);
	
	
	//根据username获取用户头像路径
	@Select("select avatar from user where username=#{username}")
	String getAvatarByUsername(@Param("username")String username);

	/**
     * 更新用户权限
     *
     * @param id 用户编号
     * @param permission 权限
     *
     * @return 是否更新成功
     */
    @Update("update user set permission=#{permission} where id=#{id}")
    boolean updatePermission(@Param("id") int id, @Param("permission") int permission);

    
    
    
    /**
     * 用过用户名或者要用户邮箱获取用户Id
     *
     * @param usernameOrEmail 用户名或邮箱
     *
     * @return 用户编号
     */
    @Select("select id from user where username=#{usernameOrEmail} or email=#{usernameOrEmail}")
    int getUserId(String usernameOrEmail);

    
    
    /**
     * 通过ID更新用户基本信息
     *
     * @param id 编号
     * @param avatar 头像
     * @param realName 真实姓名
     * @param email 邮箱
     *
     * @return 是否更新成功
     */
    @Update("update user set real_name=#{realName},email=#{email} where id=#{id}")
    boolean updateBasicInfo(@Param("id") int id, @Param("realName") String realName,
                            @Param("email") String email);

    
    
    
    /**
     * 通过id获取一个用户
     *
     * @param id 编号
     *
     * @return {@link User}
     */
    @Select("select * from user where id=#{id}")
    User getUserById(int id);

    
    
    /**
     * 通过权限获取用户
     *
     * @param permission 权限
     * @param condition 条件
     * @param offset 偏移
     *
     * @return {@link List}
     */
    @SelectProvider(type = UserSqlProvider.class, method = "getUserBy")
    List<User> listUserBy(@Param("permission") int permission, @Param("condition") String condition);

    
    
    
    /**
     * 用户登录
     *
     * @param usernameOrEmail 用户名
     * @param password 密码，sha2加密算法
     *
     * @return {@link User}
     */
    @Select("select * from user where (username=#{usernameOrEmail} or email=#{usernameOrEmail}) and password=#{password}")
    User login(@Param("usernameOrEmail") String usernameOrEmail, @Param("password") String password);

    
 
    /**
     * 添加一个用户
     *
     * @param user {@link User}
     *
     * @return 是否插入成功
     * 
     * 数据库对密码进行sha2加密，但是传输数据的过程中仍然是以明文进行传输的
     */
    @Insert("insert into user(username,real_name,email,password,is_downloadable,is_uploadable,is_deletable," +
            "is_updatable,is_visible) values(#{username},#{realName},#{email},#{password}," +
            "#{isDownloadable},#{isUploadable},#{isDeletable},#{isUpdatable},#{isVisible})")
    boolean insertUser(User user);

    
    /**
     * 通过id更新用户登录时间
     *
     * @param id 编号
     *
     * @return {@link Boolean}
     */
    @Update("update user set last_login_time=current_timestamp where id=#{id}")
    boolean updateUserLoginTime(int id);

    
    
    
    
    /**
     * 更新操作用户权限
     *
     * @param id 用户编号
     * @param isDownloadable 下载权限
     * @param isUploadable 上传权限
     * @param isVisible 可查权限
     * @param isDeletable 删除权限
     * @param isUpdatable 更新权限
     *
     * @return {@link Boolean}
     */
    @UpdateProvider(type = UserSqlProvider.class, method = "updateAuthById")
    boolean updateAuthById(@Param("id") int id, @Param("isDownloadable") int isDownloadable,
                           @Param("isUploadable") int isUploadable,  @Param("isVisible") int isVisible,@Param("isDeletable") int isDeletable, @Param(
                                   "isUpdatable") int isUpdatable);

    
    
    /**
     * 通过编号更新密码
     *
     * @param id 编号
     * @param password 密码
     *
     * @return {@link Boolean}
     */
    @Update("update user set password=#{password} where id=#{id}")
    boolean updatePasswordById(@Param("id") int id, @Param("password") String password);

    /**
     * 通过邮箱更新密码
     *
     * @param password 密码
     * @param email 邮箱
     *
     * @return {@link Boolean}
     */
    @Update("update user set password=#{password} where email=#{email}")
    boolean updatePasswordByEmail(@Param("password") String password, @Param("email") String email);

    
    
    
    /**
     * 检查用户名
     *
     * @param username 用户名
     *
     * @return {@link Integer}
     */
    @Select("select count(*) from user where username=#{username}")
    int checkUsername(String username);

    
    
    
    /**
     * 检查邮箱
     *
     * @param email 邮箱
     *
     * @return {@link Integer}
     */
    @Select("select count(*) from user where email=#{email}")
    int checkEmail(String email);
    
    
    
}
