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
     * �����ļ�Ȩ��
     *
     * @param id ���
     * @param isDownloadable ����Ȩ��
     * @param isUploadable �ϴ�Ȩ��
     * @param isVisible �ɲ�Ȩ��
     * @param isDeletable ɾ��Ȩ��
     * @param isUpdatable �ϴ�Ȩ��
     *
     * @return �Ƿ���³ɹ�
     */
    @UpdateProvider(type = FileSqlProvider.class, method = "updateAuthById")
    boolean updateAuthById(@Param("id") long id, @Param("isDownloadable") int isDownloadable, @Param("isUploadable")
            int isUploadable, @Param("isVisible") int isVisible, @Param("isDeletable") int isDeletable,@Param("isUpdatable") int isUpdatable);
    
    /**
     * ͨ����Ż�ȡ�ļ�
     *
     * @param id ���
     *
     * @return {@link File}
     */
	
    @Select("select * from file where id=#{id}")
    File getById(long id);

    /**
     * ͨ��ID��ȡ���·��
     *
     * @param fileId �ļ����
     *
     * @return {@link String}
     */
    @Select("select visit_url from file where id=#{id}")
    String getVisitUrlById(long fileId);

    /**
     * ͨ�����ɾ���ļ�
     *
     * @param id ���
     *
     * @return �Ƿ�ɾ���ɹ�
     */
    @Delete("delete from file where id=#{id}")
    boolean removeById(long id);

    /**
     * ͨ������·����ȡ�ļ����
     *
     * @param visitUrl ����·��
     *
     * @return ���
     */
    @Select("select id from file where visit_url=#{visitUrl}")
    long getIdByVisitUrl(String visitUrl);

    /**
     * ͨ������·����ȡ�ļ����
     *
     * @param localUrl ����·��
     *
     * @return ���
     */
    @Select("select id from file where local_url=#{localUrl}")
    long getIdByLocalUrl(String localUrl);

    /**
     * ͨ������·����ȡ�����ļ�·��
     *
     * @param visitUrl ����·��
     *
     * @return {@link String}
     */
    @Select("select local_url from file where visit_url=#{visitUrl}")
    String getLocalUrlByVisitUrl(String visitUrl);

    /**
     * ͨ������·��ɾ��
     *
     * @param visitUrl ����·��
     *
     * @return �Ƿ�ɾ���ɹ�
     */
    @Delete("delete from file where visit_url=#{visitUrl}")
    boolean removeByVisitUrl(String visitUrl);

    /**
     * ͨ������·��ɾ��
     *
     * @param localUrl ����·��
     *
     * @return �Ƿ�ɾ���ɹ�
     */
    @Delete("delete from file where local_url=#{localUrl}")
    boolean removeByLocalUrl(String localUrl);

    /**
     * ��鱾��·��
     *
     * @param localUrl ����·��
     *
     * @return {@link Integer}
     */
    @Select("select count(*) from file where local_url=#{localUrl}")
    int checkLocalUrl(String localUrl);

    /**
     * ������·��
     *
     * @param visitUrl ����·��
     *
     * @return {@link Integer}
     */
    @Select("select count(*) from file where visit_url=#{visitUrl}")
    int checkVisitUrl(String visitUrl);

    /**
     * ���һ���ļ�
     *
     * @param file {@link File}
     *
     * @return �Ƿ���ӳɹ�
     */
    @Insert("insert into file(name,suffix,local_url,visit_url,size,description,tag,user_id,category_id," +
            "is_downloadable,is_uploadable,is_deletable,is_updatable,is_visible) values(#{name},#{suffix}," +
            "#{localUrl},#{visitUrl},#{size},#{description},#{tag},#{userId},#{categoryId},#{isDownloadable}," +
            "#{isUploadable},#{isDeletable},#{isUpdatable},#{isVisible})")
    boolean insertFile(File file);

    /**
     * ɾ��һ���ļ�
     *
     * @param id �ļ����
     */
    @Delete("delete from file where id=#{id}")
    void deleteFileById(long id);
    
    //����Ա�鿴�����ļ�
    @SelectProvider(type = FileSqlProvider.class, method = "getBasicBy")
    List<FileRecord> listBasicBy();
    
    //�û��鿴�����ļ�
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
     * �����ļ��鿴����
     *
     * @param id ���
     */
    @Update("update file set check_times=check_times+1 where id=#{id}")
    void updateCheckTimesById(long id);

    /**
     * �����ļ����ش���
     *
     * @param id ���
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
