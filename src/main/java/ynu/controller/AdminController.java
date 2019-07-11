package ynu.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import ynu.annotation.AuthInterceptor;
import ynu.entity.User;
import ynu.enums.InterceptorLevel;
import ynu.service.IFileService;
import ynu.service.IUserService;
import ynu.service.impl.FileService;
import ynu.util.ControllerUtils;

@RestController
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	private IUserService userService;
	
	@Autowired
	private IFileService fileService;
	
	
	//管理员获取所有用户（user 是条件查询 无条件可以为空）
    @AuthInterceptor(InterceptorLevel.MANAGER)
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public String getUser(String condition, HttpServletRequest request) {
        User u = (User) request.getSession().getAttribute("user");
        return ControllerUtils.listToJson(userService.listUser(u.getPermission(), condition));
    }
    
    
    //用户权限升级,更新用户权限不是文件权限
    @AuthInterceptor(InterceptorLevel.MANAGER)
    @RequestMapping(value = "/{id}/{permission}", method = RequestMethod.PUT)
    public String updatePermission(@PathVariable("id") int id, @PathVariable("permission") int permission,HttpServletRequest request,@RequestParam("auth")String auth) {
    	JSONObject jsonObject=new JSONObject();
        User user = (User) request.getSession().getAttribute("user");
        jsonObject.put("status", "error");
        if (user.getPermission() < 3 && permission > 1) {
            jsonObject.put("message", "权限不够，设置失败");
        } else if (userService.updatePermission(id, permission)&&userService.updateFileAuth(id, auth)) {
            jsonObject.put("status","success");
        	jsonObject.put("message", "更新成功");
        } else {
            jsonObject.put("message", "更新失败，请稍后重新尝试");
        }
        return jsonObject.toJSONString();
    }

    //重置用户密码（管理员）
    @AuthInterceptor(InterceptorLevel.MANAGER)
    @RequestMapping(value = "/reset/{id}/{password}", method = RequestMethod.PUT)
    public String resetPassword(@PathVariable("id") int id, @PathVariable("password") String password) {
        return ControllerUtils.getResponse(userService.resetPassword(id, password));
    }
    
    @RequestMapping(value="/sum" ,method = RequestMethod.GET)
    public String sum() {
    	return ControllerUtils.listToJson(fileService.sum());
    }
    
    
    
}
