package ynu.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSONObject;
import ynu.annotation.AuthInterceptor;
import ynu.entity.User;
import ynu.enums.InterceptorLevel;
import ynu.service.IFileService;
import ynu.util.ControllerUtils;
import ynu.util.FileUtils;


@RestController
@RequestMapping("/file")
public class FileController {
	
	@Autowired
	private IFileService fileService;
	
	
	//处理上传请求,先判断是否有上传权限,再跳转进行下载
    @AuthInterceptor(InterceptorLevel.USER)
    @RequestMapping(value = "/up", method = RequestMethod.GET)
    public String uplo(HttpServletRequest request) throws IllegalStateException, IOException {   
    	JSONObject jsonObject=new JSONObject();
    	User user =(User) request.getSession().getAttribute("user");
    	jsonObject.put("status","success");
    	if(user.getIsUploadable()==0) {
    		jsonObject.put("status", "error");
    		jsonObject.put("message", "上传权限被限制，请联系管理员");
    	}
        return jsonObject.toString();
    }
	
	//处理上传请求
    @AuthInterceptor(InterceptorLevel.USER)
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(HttpServletRequest request,int categoryId, @RequestParam("file")MultipartFile multipartFile) throws IllegalStateException, IOException {   
    	JSONObject jsonObject=new JSONObject();
    	User user =(User) request.getSession().getAttribute("user");
    	if(user.getIsUploadable()==0) {
    		jsonObject.put("status", "error");
    		jsonObject.put("message", "上传权限被限制，请联系管理员");
    		return jsonObject.toString();
    	}
        return ControllerUtils.getResponse(fileService.upload(categoryId,multipartFile,user.getId()));
    }
    
    //处理下载请求,先判断是否能够有权限下载,再跳转url进行下载
    @AuthInterceptor(InterceptorLevel.USER)
	@RequestMapping(value = "/down", method = RequestMethod.GET)
	public String Judgecandownload(HttpServletResponse response, HttpServletRequest request,@RequestParam("id")long id,
			@RequestParam("filename") String filename) throws IOException {
		User user =(User)request.getSession().getAttribute("user");
		JSONObject jsonObject=new JSONObject();
		if(!fileService.download(id,filename)) {
			jsonObject.put("status", "error");
    		jsonObject.put("message", "该文件无法被下载");
    		return jsonObject.toString();
		}
    	if(user.getIsDownloadable()==0||user==null){
    		jsonObject.put("status", "error");
    		jsonObject.put("message", "下载权限被限制，请联系管理员");
    		return jsonObject.toString();
    	}
		jsonObject.put("status", "success");
		return jsonObject.toString();
	}
    
    //处理下载请求,获取file的名字比如"yzj.jpg"
    @AuthInterceptor(InterceptorLevel.USER)
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public String download(HttpServletResponse response, HttpServletRequest request,@RequestParam("id")long id,
			@RequestParam("filename") String filename) throws IOException {
		User user =(User)request.getSession().getAttribute("user");
		JSONObject jsonObject=new JSONObject();
		if(!fileService.download(id,filename)) {
			jsonObject.put("status", "error");
    		jsonObject.put("message", "该文件无法被下载");
    		return jsonObject.toString();
		}
    	if(user.getIsUploadable()==0||user==null) {
    		jsonObject.put("status", "error");
    		jsonObject.put("message", "下载权限被限制，请联系管理员");
    		return jsonObject.toString();
    	}
		String downloadPath = "D:" + FileUtils.separator + "server" + FileUtils.separator+filename;
		File file = new File(downloadPath);
		response.setHeader("Content-Disposition",
				"attachment;filename=" + new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"));// 设置response相应头进行下载
		FileInputStream in = new FileInputStream(file);
		ServletOutputStream os = response.getOutputStream();
		byte[] b;
		while (in.available() > 0) {
			b = in.available() > 1024 ? new byte[1024] : new byte[in.available()];
			in.read(b, 0, b.length);
			os.write(b, 0, b.length);
		}
		in.close();
		os.flush();
		os.close();
		jsonObject.put("status", "success");
		return jsonObject.toString();
	}
	
    
	//文件预览
	@AuthInterceptor
	@RequestMapping(value="/preview",method=RequestMethod.GET)
	public String preview(@RequestParam("id")long id) throws IOException, InterruptedException {		
		return fileService.preview(id);
	}
	
	
	//用户获取所有文件，被设置了权限的用户可能看不见一部分文件
	@AuthInterceptor(InterceptorLevel.USER)
	@RequestMapping(value="/view",method=RequestMethod.GET)
	public String getAll(@RequestParam("search") String search,HttpServletRequest request) {
		User user =(User) request.getSession().getAttribute("user");
		JSONObject jsonObject =new JSONObject();
		if(user.getIsVisible()==0) {
    		jsonObject.put("status", "error");
    		jsonObject.put("message", "您的查看权限被限制，请联系管理员");
    		return jsonObject.toString();
		}
		return ControllerUtils.listToJson(fileService.listViewAll(search));
	}
	
	//用户获取自己的资源,只有领导及以上可以使用该功能
	@AuthInterceptor(InterceptorLevel.LEADER)
	@RequestMapping(value="/my",method=RequestMethod.GET)
	public String getMy(@RequestParam("search") String search,HttpServletRequest request) {
		User user=(User)request.getSession().getAttribute("user");
		JSONObject jsonObject=new JSONObject();
		if(user.getPermission()<2) {
    		jsonObject.put("status", "error");
    		jsonObject.put("message", "您没有权限使用该功能");
    		return jsonObject.toString();
		}
		return ControllerUtils.listToJson(fileService.listMyFile(user,search));
	}
	
	//用户从我的资源中删除文件
    @AuthInterceptor(InterceptorLevel.USER)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String removeFile(@PathVariable("id") long id,HttpServletRequest request) {
    	JSONObject jsonObject=new JSONObject();
        User user = (User) request.getSession().getAttribute("user");
        jsonObject.put("status", "error");
        if (user==null) {
            jsonObject.put("message", "请先登录");
        } else if(user.getIsDeletable()==0){
            jsonObject.put("message", "删除失败，权限不够，请联系管理员");
        }else if(fileService.removeFile(user, id)) {
        	jsonObject.put("status","success");
        	jsonObject.put("message", "删除成功");
        }else {
        	jsonObject.put("message", "文件被设置权限，无法被删除");
        }
        return jsonObject.toString();
    }
    
    //用户从我的资源中更新文件信息
    @RequestMapping(value="/{id}",method=RequestMethod.PUT)
    public String updateFile(@PathVariable("id")int id,String name,int categoryId,String description,HttpServletRequest request) {
    	User user=(User) request.getSession().getAttribute("user");
    	JSONObject jsonObject=new JSONObject();
    	jsonObject.put("status", "error");
    	if(user.getIsUpdatable()==0) {
    		jsonObject.put("message", "无法更新，您没有更新权限,请联系管理员");
    	}else if(fileService.updateFile(id, name,categoryId, description)){
    		jsonObject.put("status", "success");
    	}else {
    		jsonObject.put("message", "文件被设置权限，无法被更新");
    	}
    	return jsonObject.toString();
    }

    //管理员更新文件信息
    @AuthInterceptor(InterceptorLevel.MANAGER)
    @RequestMapping(value = "/admin/{id}", method = RequestMethod.PUT)
    public String adminUpdateFile(@PathVariable("id")int id,String name,int categoryId,String description,HttpServletRequest request) {
    	JSONObject jsonObject =new JSONObject();
    	fileService.updateFile(id, name,categoryId, description);
    	jsonObject.put("status", "success");
    	return jsonObject.toString();
    }
    
    //管理员更新文件
    @AuthInterceptor(InterceptorLevel.MANAGER)
    @RequestMapping(value = "/admin/{id}", method = RequestMethod.DELETE)
    public String adminremoveFile(@PathVariable("id") long id ,HttpServletRequest request) {
    	JSONObject jsonObject =new JSONObject();
    	fileService.adminRemoveFile(id);
    	jsonObject.put("status", "success");
    	return jsonObject.toString();
    }
    
	//管理员获取所有文件信息
	@AuthInterceptor(InterceptorLevel.MANAGER)
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public String getBasicAll() {
        return ControllerUtils.listToJson(fileService.listBasicAll());
    }
	
    //管理员修改文件权限
    @AuthInterceptor(InterceptorLevel.MANAGER)
    @RequestMapping(value = "/{id}/auth", method = RequestMethod.PUT)
    public String updateFileAuth(@PathVariable("id") int id, String auth) {
        return ControllerUtils.getResponse(fileService.updateAuth(id, auth));
    }
    
   
}
