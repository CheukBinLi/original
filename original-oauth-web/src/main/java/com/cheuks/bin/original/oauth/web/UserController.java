package com.cheuks.bin.original.oauth.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cheuks.bin.original.oauth.util.ResultFactory;

@Controller
@RequestMapping("/user/")
public class UserController {

	ResultFactory resultFactory = new ResultFactory();

	@ResponseBody
	@GetMapping("loginPage")
	public Object loginPage(HttpServletRequest request) throws Throwable {
//		return new ModelAndView("redirect:https://www.163.com");
		return "请登陆";
	}
	
}
