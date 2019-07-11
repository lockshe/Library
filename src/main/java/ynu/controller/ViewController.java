package ynu.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class ViewController {
	
	@RequestMapping(value="/",method=RequestMethod.GET)
	public String sign() {
		return "signin";
	}
	
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public String index() {
		return "index";
	}
	
	@RequestMapping(value="/admin",method=RequestMethod.GET)
	public String admin() {
		return "admin";
	}
	
	@RequestMapping(value="/logout")
    public String execute(HttpSession session){
        session.invalidate();
        return "redirect:/signin";
    }

}
