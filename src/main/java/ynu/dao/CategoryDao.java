package ynu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import ynu.entity.Category;


@Repository
public interface CategoryDao {

	 /**
     * ͨ����������ȡID
     *
     * @param name ������
     *
     * @return {@link Integer}
     */
    @Select("select id from category where name=#{name}")
    int getIdByName(String name);

    /**
     * ���һ������
     *
     * @param name ������
     *
     * @return �Ƿ���ӳɹ�
     */
    @Insert("insert into category(name) values(#{name})")
    boolean insertCategory(String name);

    /**
     * ͨ�����ɾ��һ������
     *
     * @param id ���
     *
     * @return �Ƿ�ɾ���ɹ�
     */
    @Delete("delete from category where id=#{id}")
    boolean removeCategoryById(int id);

    /**
     * ͨ������ɾ��һ������
     *
     * @param name ��������
     *
     * @return �Ƿ�ɾ���ɹ�
     */
    @Delete("delete from category where name=#{name}")
    boolean removeCategoryByName(String name);

    /**
     * ����һ��������
     *
     * @param name ������
     * @param id ����ID
     *
     * @return �Ƿ���³ɹ�
     */
    @Update("update category set name=#{name} where id=#{id}")
    boolean updateNameById(@Param("id") int id, @Param("name") String name);

    /**
     * ͨ�����������·�����
     *
     * @param newName �µķ�����
     * @param oldName �ɵķ�����
     */
    @Update("update category set name=#{newName} where name=#{oldName}")
    void updateNameByName(String newName, String oldName);

    /**
     * ��ȡ���з���
     *
     * @return {@link List}
     */
    @Select("select * from category")
    List<Category> listCategory();

    /**
     * ͨ����Ż�ȡһ������
     *
     * @param id ���
     *
     * @return {@link Category}
     */
    @Select("select * from category where id=#{id}")
    Category getCategoryById(int id);

    /**
     * ͨ�����ƻ�ȡһ������
     *
     * @param name ����
     *
     * @return {@link Category}
     */
    @Select("select * from category where name=#{name}")
    Category getCategoryByName(String name);
    
}
