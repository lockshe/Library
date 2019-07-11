package ynu.service.impl;


import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ynu.dao.FileDao;
import ynu.entity.CategorySum;
import ynu.entity.File;
import ynu.entity.FileRecord;
import ynu.entity.User;
import ynu.service.IFileService;
import ynu.util.BeanUtils;
import ynu.util.ConvertUtils;
import ynu.util.FileUtils;

@Service
public class FileService implements IFileService{
	
	@Autowired
	private FileDao fileDao;

	//文件上传
	public boolean upload(int categoryId,  MultipartFile multipartFile,int userid) throws IllegalStateException, IOException {
			String name = multipartFile.getOriginalFilename();
			String suffix=name.substring(name.lastIndexOf("."));//获得文件后缀
			String localUrl="D:"+FileUtils.separator+"server"+FileUtils.separator+name;
			String visitUrl=name;
			long size=multipartFile.getSize();
			boolean canupload=!multipartFile.isEmpty()&&size<999999999;
			if(canupload) {
			multipartFile.transferTo(new java.io.File(localUrl));// 将文件转存到本地
			File file = new File(name, suffix, localUrl, visitUrl, "description", "tag", userid, categoryId);
			int auth[] = { 1, 1, 1, 1, 1 };
			file.setAuth(auth[0], auth[1], auth[2], auth[3], auth[4]);
			boolean isSuccess = fileDao.insertFile(file);
			return isSuccess;
		}
		return false;
	}
	
	//管理员查看所有资源
	public List<FileRecord> listBasicAll() {
        
		return fileDao.listBasicBy();
    }
	
	//用户查看所有资源
	public List<FileRecord> listViewAll(String search) {
		
		return fileDao.listViewBy(search);
	}
	
	//展示我的资源
	public List<FileRecord> listMyFile(User user,String search) {
		
		return fileDao.listMyFile(user.getId(),search);
	}
	
	//根据文件id删除文件
    public boolean removeById(long id) {
        
        return fileDao.removeById(id);
    }
    
    //文件下载
	public boolean download(long id,String filename) {
		int i=fileDao.getIsDownloadable(filename);
		if(i==0) {
			return false;
		}
		fileDao.updateDownloadTimesById(id);//更新下载次数
		return true;
	}
	
	//文件预览
    public String preview(long id) throws IOException, InterruptedException {
    	
    	fileDao.updateCheckTimesById(id);//更新查看次数
    	String filename=fileDao.getVisitUrlById(id);
    	ConvertUtils convertUtils =ConvertUtils.getConvertUtilsInstance();
    	return convertUtils.Convert(filename);
    }
    
    //更新文件权限
	public boolean updateAuth(long id, String auths) {
		int auth[] = BeanUtils.toIntArr(auths);
		return fileDao.updateAuthById(id,auth[0] , auth[1], auth[2], auth[3],auth[4]);
	}

	
	//用户删除文件，需要权限
	public boolean removeFile(User user, long id) {
		int isDeleteable=fileDao.getIsDeteable(id);
		if(isDeleteable==0)return false;
		String filename =fileDao.getVisitUrlById(id);
		fileDao.deleteFileById(id);
		System.out.print(filename);
		boolean isSuccess = FileUtils.deleteFile(filename);
		return isSuccess;
	}
	
	//管理员删除权限,不需要权限
	public boolean adminRemoveFile(long id) {
		fileDao.deleteFileById(id);
		String filename =fileDao.getVisitUrlById(id);
		boolean isSuccess = FileUtils.deleteFile(filename);
		return isSuccess;
	}
	
	//更新文件信息
	public boolean updateFile(int id,String name,int categoryId,String description) {
		int isUpdateable=fileDao.getIsUpdateable(id);
		if(isUpdateable==0) {
			return false;
		}
		else {
			fileDao.updateNameById(id, name);
			fileDao.updateDescriptionById(id, description);
			fileDao.updateCategoryById(id, categoryId);
			return true;
		}
	}
	
	public List<CategorySum> sum() {
		return fileDao.CategorySum();
	}
	
	public String getResource(String visitUrl, HttpServletRequest request) {
		return null;
	}

	public String getLocalUrlByVisitUrl(String visitUrl) {
		return null;
	}

	public boolean localUrlExists(String localUrl) {
		return false;
	}

	public boolean visitUrlExists(String visitUrl) {
		return false;
	}

	public long getFileId(String localUrl) {
		return 0;
	}



}
