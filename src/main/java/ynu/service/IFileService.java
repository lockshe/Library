package ynu.service;


import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ynu.entity.CategorySum;
import ynu.entity.File;
import ynu.entity.FileRecord;
import ynu.entity.User;

public interface IFileService {
    /**
     * 更新文件权限
     *
     * @param id 文件编号
     * @param auth 权限集
     *
     * @return 是否更新成功
     */
    boolean updateAuth(long id, String auth);
    
    
    /**
     * 获取资源
     *
     * @param visitUrl 访问路径
     * @param request {@link HttpServletRequest}
     *
     * @return {@link File}
     */
    String getResource(String visitUrl, HttpServletRequest request);

    /**
     * 通过访问路径获取本地文件路径
     *
     * @param visitUrl 访问路径
     *
     * @return {@link String}
     */
    String getLocalUrlByVisitUrl(String visitUrl);
    
    /**
     * 上传文件
     *
     * @param categoryId 分类ID
     * @param tag 标签
     * @param description 描述
     * @param prefix 自定义前缀
     * @param multipartFile 文件
     * @param user {@link User}
     *
     * @return 是否上传成功
     * @throws IOException 
     * @throws IllegalStateException 
     */
    boolean upload(int categoryId, MultipartFile multipartFile ,int userid) throws IllegalStateException, IOException;
    
    /**
     * 本地路径是否存在
     *
     * @param localUrl 本地路径
     *
     * @return {@link Boolean}
     */
    boolean localUrlExists(String localUrl);

    /**
     * 访问路径是否存在
     *
     * @param visitUrl 访问路径
     *
     * @return {@link Boolean}
     */
    boolean visitUrlExists(String visitUrl);

    /**
     * 通过本地路径获取文件编号
     *
     * @param localUrl 本地路径
     *
     * @return 文件编号
     */
    long getFileId(String localUrl);
    
    List<FileRecord> listBasicAll();
    
    List<FileRecord> listViewAll(String search);
    
    List<FileRecord> listMyFile(User user,String search);
    
    String preview(long id) throws IOException, InterruptedException;
    
    boolean download(long id,String filename);

	boolean removeFile(User user, long id);
	
	boolean adminRemoveFile(long id);
	
	boolean updateFile(int id,String name,int categoryId,String description);
	
	List<CategorySum> sum();
}
