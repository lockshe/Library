package ynu.service.impl;



import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ynu.dao.UserDao;
import ynu.entity.File;
import ynu.entity.User;
import ynu.service.IUserService;
import ynu.util.BeanUtils;
import ynu.util.FileUtils;


@Service
public class UserService implements IUserService{
	
	private final UserDao userDao;
	
	@Autowired
	public UserService(UserDao userDao) {
		this.userDao=userDao;
	}

	public boolean resetPassword(int id, String password) {

		return userDao.updatePasswordById(id, password);
	}
	
	//升级用户管理权限
	public boolean updatePermission(int id, int permission) {
		
		 return userDao.updatePermission(id, permission > 2 ? 2 : permission);
	}
	
	//升级用户文件权限
	public boolean updateFileAuth(int id, String auths) {
		int auth[] = BeanUtils.toIntArr(auths);
		return userDao.updateAuthById(id,auth[0] , auth[1], auth[2], auth[3],auth[4]);
	}

	public List<User> listUser(int permission, String condition) {
		  return userDao.listUserBy(permission, condition);
	}

	public User login(String loginName, String password, HttpServletResponse response) {
	        User user = null;
	        user=userDao.login(loginName, password);
	        updateUserLoginTime(user);
	        return user;
	}

	public boolean register(String username, String email, String password) {
		  User user = new User(username, "", email, password);
		  int auth[]= {1,1,1,1,1};
          user.setAuth(auth[0], auth[1], auth[2], auth[3], auth[4]);
          return userDao.insertUser(user);
	}

	public boolean resetPasswordByEmail(String email, String password) {

		return false;
	}
	
	//检查用户名是否已经存在
	public boolean usernameExists(String username) {

		return username!=""&&userDao.checkUsername(username) > 0;
	}
	
	
	//
	public User getUserById(int id) {
		
		return userDao.getUserById(id);
	}
	
	//登陆时间更新
	public void updateUserLoginTime(User user) {
        if (user!=null) {
            userDao.updateUserLoginTime(user.getId());
        }
	}
	
	//用户更新自己的密码
	public boolean updatePasswordById(int id,String password) {

		return userDao.updatePasswordById(id, password);
	}

	//检查email是否已经存存在
	public boolean emailExists(String email) {

		return email!=""&&userDao.checkEmail(email)>0;
	}
	
	//用户更新基本信息
	public boolean updateBasicInfoById(int id, String realName, String email) {
		
		return userDao.updateBasicInfo(id, realName, email);
	}
	
	//根据ID设置用户头像路径
	public boolean uploadAvatar(int id, MultipartFile multipartFile) throws IllegalStateException, IOException {
		
		String name = multipartFile.getOriginalFilename();
		String suffix = name.substring(name.lastIndexOf("."));// 获得文件后缀
		if (suffix.equals(".jpg") || suffix.equals(".jpeg")) {
			String localUrl = "D:" + FileUtils.separator + "avatar" + FileUtils.separator + name;
				multipartFile.transferTo(new java.io.File(localUrl));// 将文件转存到本地
				boolean isSuccess = userDao.updateAvatarById(id, localUrl);
				return isSuccess;
		}
		return false;
	}	
	
	public boolean checkPassword(String password) {
		return false;
	}

	public int getUserId(String usernameOrEmail) {

		return 0;
	}

	public String getAvatarByUsername(String username) {
		return userDao.getAvatarByUsername(username);
	}




}
