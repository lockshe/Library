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
	
	
	//�����ϴ�����,���ж��Ƿ����ϴ�Ȩ��,����ת��������
    @AuthInterceptor(InterceptorLevel.USER)
    @RequestMapping(value = "/up", method = RequestMethod.GET)
    public String uplo(HttpServletRequest request) throws IllegalStateException, IOException {   
    	JSONObject jsonObject=new JSONObject();
    	User user =(User) request.getSession().getAttribute("user");
    	jsonObject.put("status","success");
    	if(user.getIsUploadable()==0) {
    		jsonObject.put("status", "error");
    		jsonObject.put("message", "�ϴ�Ȩ�ޱ����ƣ�����ϵ����Ա");
    	}
        return jsonObject.toString();
    }
	
	//�����ϴ�����
    @AuthInterceptor(InterceptorLevel.USER)
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(HttpServletRequest request,int categoryId, @RequestParam("file")MultipartFile multipartFile) throws IllegalStateException, IOException {   
    	JSONObject jsonObject=new JSONObject();
    	User user =(User) request.getSession().getAttribute("user");
    	if(user.getIsUploadable()==0) {
    		jsonObject.put("status", "error");
    		jsonObject.put("message", "�ϴ�Ȩ�ޱ����ƣ�����ϵ����Ա");
    		return jsonObject.toString();
    	}
        return ControllerUtils.getResponse(fileService.upload(categoryId,multipartFile,user.getId()));
    }
    
    //������������,���ж��Ƿ��ܹ���Ȩ������,����תurl��������
    @AuthInterceptor(InterceptorLevel.USER)
	@RequestMapping(value = "/down", method = RequestMethod.GET)
	public String Judgecandownload(HttpServletResponse response, HttpServletRequest request,@RequestParam("id")long id,
			@RequestParam("filename") String filename) throws IOException {
		User user =(User)request.getSession().getAttribute("user");
		JSONObject jsonObject=new JSONObject();
		if(!fileService.download(id,filename)) {
			jsonObject.put("status", "error");
    		jsonObject.put("message", "���ļ��޷�������");
    		return jsonObject.toString();
		}
    	if(user.getIsDownloadable()==0||user==null){
    		jsonObject.put("status", "error");
    		jsonObject.put("message", "����Ȩ�ޱ����ƣ�����ϵ����Ա");
    		return jsonObject.toString();
    	}
		jsonObject.put("status", "success");
		return jsonObject.toString();
	}
    
    //������������,��ȡfile�����ֱ���"yzj.jpg"
    @AuthInterceptor(InterceptorLevel.USER)
	@RequestMapping(value = "/download", method = RequestMethod.GET)
	public String download(HttpServletResponse response, HttpServletRequest request,@RequestParam("id")long id,
			@RequestParam("filename") String filename) throws IOException {
		User user =(User)request.getSession().getAttribute("user");
		JSONObject jsonObject=new JSONObject();
		if(!fileService.download(id,filename)) {
			jsonObject.put("status", "error");
    		jsonObject.put("message", "���ļ��޷�������");
    		return jsonObject.toString();
		}
    	if(user.getIsUploadable()==0||user==null) {
    		jsonObject.put("status", "error");
    		jsonObject.put("message", "����Ȩ�ޱ����ƣ�����ϵ����Ա");
    		return jsonObject.toString();
    	}
		String downloadPath = "D:" + FileUtils.separator + "server" + FileUtils.separator+filename;
		File file = new File(downloadPath);
		response.setHeader("Content-Disposition",
				"attachment;filename=" + new String(file.getName().getBytes("UTF-8"), "ISO-8859-1"));// ����response��Ӧͷ��������
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
	
    
	//�ļ�Ԥ��
	@AuthInterceptor
	@RequestMapping(value="/preview",method=RequestMethod.GET)
	public String preview(@RequestParam("id")long id) throws IOException, InterruptedException {		
		return fileService.preview(id);
	}
	
	
	//�û���ȡ�����ļ�����������Ȩ�޵��û����ܿ�����һ�����ļ�
	@AuthInterceptor(InterceptorLevel.USER)
	@RequestMapping(value="/view",method=RequestMethod.GET)
	public String getAll(@RequestParam("search") String search,HttpServletRequest request) {
		User user =(User) request.getSession().getAttribute("user");
		JSONObject jsonObject =new JSONObject();
		if(user.getIsVisible()==0) {
    		jsonObject.put("status", "error");
    		jsonObject.put("message", "���Ĳ鿴Ȩ�ޱ����ƣ�����ϵ����Ա");
    		return jsonObject.toString();
		}
		return ControllerUtils.listToJson(fileService.listViewAll(search));
	}
	
	//�û���ȡ�Լ�����Դ,ֻ���쵼�����Ͽ���ʹ�øù���
	@AuthInterceptor(InterceptorLevel.LEADER)
	@RequestMapping(value="/my",method=RequestMethod.GET)
	public String getMy(@RequestParam("search") String search,HttpServletRequest request) {
		User user=(User)request.getSession().getAttribute("user");
		JSONObject jsonObject=new JSONObject();
		if(user.getPermission()<2) {
    		jsonObject.put("status", "error");
    		jsonObject.put("message", "��û��Ȩ��ʹ�øù���");
    		return jsonObject.toString();
		}
		return ControllerUtils.listToJson(fileService.listMyFile(user,search));
	}
	
	//�û����ҵ���Դ��ɾ���ļ�
    @AuthInterceptor(InterceptorLevel.USER)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public String removeFile(@PathVariable("id") long id,HttpServletRequest request) {
    	JSONObject jsonObject=new JSONObject();
        User user = (User) request.getSession().getAttribute("user");
        jsonObject.put("status", "error");
        if (user==null) {
            jsonObject.put("message", "���ȵ�¼");
        } else if(user.getIsDeletable()==0){
            jsonObject.put("message", "ɾ��ʧ�ܣ�Ȩ�޲���������ϵ����Ա");
        }else if(fileService.removeFile(user, id)) {
        	jsonObject.put("status","success");
        	jsonObject.put("message", "ɾ���ɹ�");
        }else {
        	jsonObject.put("message", "�ļ�������Ȩ�ޣ��޷���ɾ��");
        }
        return jsonObject.toString();
    }
    
    //�û����ҵ���Դ�и����ļ���Ϣ
    @RequestMapping(value="/{id}",method=RequestMethod.PUT)
    public String updateFile(@PathVariable("id")int id,String name,int categoryId,String description,HttpServletRequest request) {
    	User user=(User) request.getSession().getAttribute("user");
    	JSONObject jsonObject=new JSONObject();
    	jsonObject.put("status", "error");
    	if(user.getIsUpdatable()==0) {
    		jsonObject.put("message", "�޷����£���û�и���Ȩ��,����ϵ����Ա");
    	}else if(fileService.updateFile(id, name,categoryId, description)){
    		jsonObject.put("status", "success");
    	}else {
    		jsonObject.put("message", "�ļ�������Ȩ�ޣ��޷�������");
    	}
    	return jsonObject.toString();
    }

    //����Ա�����ļ���Ϣ
    @AuthInterceptor(InterceptorLevel.MANAGER)
    @RequestMapping(value = "/admin/{id}", method = RequestMethod.PUT)
    public String adminUpdateFile(@PathVariable("id")int id,String name,int categoryId,String description,HttpServletRequest request) {
    	JSONObject jsonObject =new JSONObject();
    	fileService.updateFile(id, name,categoryId, description);
    	jsonObject.put("status", "success");
    	return jsonObject.toString();
    }
    
    //����Ա�����ļ�
    @AuthInterceptor(InterceptorLevel.MANAGER)
    @RequestMapping(value = "/admin/{id}", method = RequestMethod.DELETE)
    public String adminremoveFile(@PathVariable("id") long id ,HttpServletRequest request) {
    	JSONObject jsonObject =new JSONObject();
    	fileService.adminRemoveFile(id);
    	jsonObject.put("status", "success");
    	return jsonObject.toString();
    }
    
	//����Ա��ȡ�����ļ���Ϣ
	@AuthInterceptor(InterceptorLevel.MANAGER)
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public String getBasicAll() {
        return ControllerUtils.listToJson(fileService.listBasicAll());
    }
	
    //����Ա�޸��ļ�Ȩ��
    @AuthInterceptor(InterceptorLevel.MANAGER)
    @RequestMapping(value = "/{id}/auth", method = RequestMethod.PUT)
    public String updateFileAuth(@PathVariable("id") int id, String auth) {
        return ControllerUtils.getResponse(fileService.updateAuth(id, auth));
    }
    
   
}
