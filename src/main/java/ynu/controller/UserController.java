
package ynu.controller;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.*;
import ynu.annotation.AuthInterceptor;
import ynu.entity.User;
import ynu.enums.InterceptorLevel;
import ynu.service.IUserService;
import ynu.util.ControllerUtils;
import ynu.util.VerifyUtils;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private IUserService userService;
	
	//用户登录
    @AuthInterceptor(InterceptorLevel.NONE)
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(String username, String password,String code,HttpServletRequest httpServletRequest) {

    	String verifycode=(String) httpServletRequest.getSession().getAttribute("authcode");
    	JSONObject jsonObject=new JSONObject();
        User user = userService.login(username, password, null);
        if(code.equalsIgnoreCase(verifycode)) {
        	jsonObject.put("status", "error");
        	jsonObject.put("message", "验证码错误");
        }
        else if (user==null || user.getPermission() < 1) {
            jsonObject.put("status", "error");
            jsonObject.put("message", "用户名或者密码错误");
        } else {
            httpServletRequest.getSession().setAttribute("user", user);
            jsonObject.put("status", "success"); 
        }
        return jsonObject.toString();
    }
        
    //用户注册
    @AuthInterceptor(InterceptorLevel.NONE)
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(String username, String email, String password,String password2,String code,HttpServletRequest httpServletRequest) {
    		String verifycode=(String) httpServletRequest.getSession().getAttribute("authcode");
    		JSONObject jsonObject=new JSONObject();
        	jsonObject.put("status", "error");
            if (userService.usernameExists(username)) {
                jsonObject.put("message", "用户名已经存在");
            } else if (userService.emailExists(email)) {
                jsonObject.put("message", "该邮箱已经被注册啦");
            }else if(code.equalsIgnoreCase(verifycode)){
            	jsonObject.put("message", "验证码不对");
            }else if (userService.register(username, email, password)) {
                jsonObject.put("status", "success");
            }
            else {
                jsonObject.put("message", "数据格式不合法");
            }

        return jsonObject.toString();
    }
    
    //处理验证码
    @AuthInterceptor(InterceptorLevel.NONE)
    @RequestMapping(value="/verify",method=RequestMethod.GET)
    public void verify(HttpServletResponse response ,HttpServletRequest request) throws IOException {
        Object[] verifycode=VerifyUtils.createImage();
    	response.setHeader("Pragma", "No-cache");  
        response.setHeader("Cache-Control", "no-cache");  
        response.setDateHeader("Expires", 0); 
        //将字符保存到session中用于前端的验证  
        request.getSession().setAttribute("authCode", verifycode[0]);
        ImageIO.write((RenderedImage) verifycode[1], "JPEG", response.getOutputStream());
        response.getOutputStream().flush();  
    }
    
    //用户获取用户基本信息
    @AuthInterceptor(InterceptorLevel.USER)
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String getInfo(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        JSONObject object =new JSONObject();
        object.put("username", user.getUsername());
        object.put("realname", user.getRealName());
        object.put("permission", user.getPermission());
        object.put("loginTime", user.getLastLoginTime());
        object.put("email", user.getEmail());
        return object.toString();
    }
    
    //用户修改用户基本信息
    @AuthInterceptor(InterceptorLevel.USER)
    @RequestMapping(value = "/info", method = RequestMethod.PUT)
    public String setInfo(HttpServletRequest request,@RequestParam("realname")String realname,@RequestParam("email")String email) {
        User user = (User) request.getSession().getAttribute("user");
        JSONObject jsonObject =new JSONObject();
        jsonObject.put("status", "error");
        jsonObject.put("message", "错误");
        if(userService.updateBasicInfoById(user.getId(),realname, email)) {
        	user.setRealName(realname);
        	user.setEmail(email);
        	request.getSession().setAttribute("user", user);//在seesion中重新设置user
        	jsonObject.put("status", "success");
			jsonObject.put("message", "修改用户信息成功!");
        }       
        return jsonObject.toString();
    }
    
    
    //用户更新自己的密码 
	@AuthInterceptor(InterceptorLevel.USER)
	@RequestMapping(value = "/password", method = RequestMethod.PUT)
	public String updatePassword(HttpServletRequest request, String oldPassword, String newPassword) {
		JSONObject jsonObject = new JSONObject();
		User user = (User) request.getSession().getAttribute("user");
		jsonObject.put("status", "error");
		if (user.getPassword().equals(oldPassword)) {
			if (userService.updatePasswordById(user.getId(),newPassword)) {
				jsonObject.put("status", "success");
				jsonObject.put("message", "修改密码成功!");
			} else {
				jsonObject.put("message", "失败，请检查密码格式");
			}
		} else {
			jsonObject.put("message", "原密码不正确");
		}
		return jsonObject.toString();
	}
	
	//用户上传头像
	@AuthInterceptor(InterceptorLevel.USER)
	@RequestMapping(value="/uploadavatar",method=RequestMethod.POST)
	public String uploadavatar(@RequestParam("file") MultipartFile mutipartFile,HttpServletRequest request) throws IllegalStateException, IOException {
    	User user=(User)request.getSession().getAttribute("user");
    	return ControllerUtils.getResponse(userService.uploadAvatar(user.getId(),mutipartFile));

	}
	
	//根据用户名获取头像
	@AuthInterceptor(InterceptorLevel.USER)
	@RequestMapping(value="/getavatar/{username}",method=RequestMethod.GET)
	public void avatar(@PathVariable("username")String username,HttpServletResponse response) throws IOException {		
		String image = userService.getAvatarByUsername(username);
		File file = new File(image);
		FileInputStream fInputStream = new FileInputStream(file);
		OutputStream os = response.getOutputStream();
		int bytesRead = 0;
		byte[] buffer = new byte[1024 * 8];
		while ((bytesRead = fInputStream.read(buffer)) != -1) {
			os.write(buffer, 0, bytesRead);
			os.flush();
		}
		fInputStream.close();
		os.close();
	}

	
	//获取自己的头像
	@AuthInterceptor(InterceptorLevel.USER)
	@RequestMapping(value="/avatar/my",method=RequestMethod.GET)
	public void myavatar(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		User user=(User)request.getSession().getAttribute("user");
		String image=userService.getAvatarByUsername(user.getUsername());
		File file=new File(image);
		FileInputStream fInputStream = new FileInputStream(file);
		OutputStream os=response.getOutputStream();
        int bytesRead = 0;
        byte[] buffer = new byte[1024 * 8];
        while ((bytesRead = fInputStream.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
            os.flush();
        }
        fInputStream.close();
        os.close();	
	}
}
