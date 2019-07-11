package ynu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.hibernate.validator.constraints.ParameterScriptAssert;
import org.springframework.stereotype.Repository;

import ynu.dao.sqlprovider.FileSqlProvider;
import ynu.entity.CategorySum;
import ynu.entity.File;
import ynu.entity.FileRecord;


@Repository
public interface FileDao {
	
	
    /**
     * 更新文件权限
     *
     * @param id 编号
     * @param isDownloadable 下载权限
     * @param isUploadable 上传权限
     * @param isVisible 可查权限
     * @param isDeletable 删除权限
     * @param isUpdatable 上传权限
     *
     * @return 是否更新成功
     */
    @UpdateProvider(type = FileSqlProvider.class, method = "updateAuthById")
    boolean updateAuthById(@Param("id") long id, @Param("isDownloadable") int isDownloadable, @Param("isUploadable")
            int isUploadable, @Param("isVisible") int isVisible, @Param("isDeletable") int isDeletable,@Param("isUpdatable") int isUpdatable);
    
    /**
     * 通过编号获取文件
     *
     * @param id 编号
     *
     * @return {@link File}
     */
	
    @Select("select * from file where id=#{id}")
    File getById(long id);

    /**
     * 通过ID获取相对路径
     *
     * @param fileId 文件编号
     *
     * @return {@link String}
     */
    @Select("select visit_url from file where id=#{id}")
    String getVisitUrlById(long fileId);

    /**
     * 通过编号删除文件
     *
     * @param id 编号
     *
     * @return 是否删除成功
     */
    @Delete("delete from file where id=#{id}")
    boolean removeById(long id);

    /**
     * 通过本地路径获取文件编号
     *
     * @param visitUrl 本地路径
     *
     * @return 编号
     */
    @Select("select id from file where visit_url=#{visitUrl}")
    long getIdByVisitUrl(String visitUrl);

    /**
     * 通过本地路径获取文件编号
     *
     * @param localUrl 本地路径
     *
     * @return 编号
     */
    @Select("select id from file where local_url=#{localUrl}")
    long getIdByLocalUrl(String localUrl);

    /**
     * 通过访问路径获取本地文件路径
     *
     * @param visitUrl 访问路径
     *
     * @return {@link String}
     */
    @Select("select local_url from file where visit_url=#{visitUrl}")
    String getLocalUrlByVisitUrl(String visitUrl);

    /**
     * 通过访问路径删除
     *
     * @param visitUrl 访问路径
     *
     * @return 是否删除成功
     */
    @Delete("delete from file where visit_url=#{visitUrl}")
    boolean removeByVisitUrl(String visitUrl);

    /**
     * 通过本地路径删除
     *
     * @param localUrl 本地路径
     *
     * @return 是否删除成功
     */
    @Delete("delete from file where local_url=#{localUrl}")
    boolean removeByLocalUrl(String localUrl);

    /**
     * 检查本地路径
     *
     * @param localUrl 本地路径
     *
     * @return {@link Integer}
     */
    @Select("select count(*) from file where local_url=#{localUrl}")
    int checkLocalUrl(String localUrl);

    /**
     * 检查访问路径
     *
     * @param visitUrl 访问路径
     *
     * @return {@link Integer}
     */
    @Select("select count(*) from file where visit_url=#{visitUrl}")
    int checkVisitUrl(String visitUrl);

    /**
     * 添加一个文件
     *
     * @param file {@link File}
     *
     * @return 是否添加成功
     */
    @Insert("insert into file(name,suffix,local_url,visit_url,size,description,tag,user_id,category_id," +
            "is_downloadable,is_uploadable,is_deletable,is_updatable,is_visible) values(#{name},#{suffix}," +
            "#{localUrl},#{visitUrl},#{size},#{description},#{tag},#{userId},#{categoryId},#{isDownloadable}," +
            "#{isUploadable},#{isDeletable},#{isUpdatable},#{isVisible})")
    boolean insertFile(File file);

    /**
     * 删除一个文件
     *
     * @param id 文件编号
     */
    @Delete("delete from file where id=#{id}")
    void deleteFileById(long id);
    
    //管理员查看所有文件
    @SelectProvider(type = FileSqlProvider.class, method = "getBasicBy")
    List<FileRecord> listBasicBy();
    
    //用户查看所有文件
    @SelectProvider(type=FileSqlProvider.class,method="getViewAll")
    List<FileRecord> listViewBy(@Param("search")String search);
    
    @SelectProvider(type=FileSqlProvider.class,method="getMine")
    List<FileRecord> listMyFile(@Param("id")int id,@Param("search") String search );
    
    @Select("select is_deletable from file where id=#{id}")
    int getIsDeteable(@Param("id") long id);
    
    @Select("select is_downloadable from file where name=#{name}")
    int getIsDownloadable(@Param("name") String filename);
    
    @Select("select is_updatable from file where id=#{id}")
    int getIsUpdateable(@Param("id") long id);
    /**
     * 更新文件查看次数
     *
     * @param id 编号
     */
    @Update("update file set check_times=check_times+1 where id=#{id}")
    void updateCheckTimesById(long id);

    /**
     * 更新文件下载次数
     *
     * @param id 编号
     */
    @Update("update file set download_times=download_times+1 where id=#{id}")
    void updateDownloadTimesById(long id);
    
    
    @Update("update file set name=#{name} where id=#{id}")
    void updateNameById(@Param("id") int id, @Param("name") String name);
 
    @Update("update file set category_id=#{categoryId} where id=#{id}")
    void updateCategoryById(@Param("id") int id, @Param("categoryId") int categoryId);
    
    @Update("upadte file set description=#{description} where id=#{id}")
    void updateDescriptionById(@Param("id") int id,@Param("description") String description);
    
    @Select("select category.id,category.name,count(category_id) as sum from category left join file on(category.id=file.category_id) group by category.id")
    List<CategorySum> CategorySum();
}
