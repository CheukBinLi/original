package com.cheuks.bin.original.oauth.web;

import com.cheuks.bin.original.common.util.web.ResultFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user/")
public class UserController {

    ResultFactory resultFactory = new ResultFactory();

    //	@ResponseBody
    @GetMapping("loginPage")
    public Object loginPage(HttpServletRequest request) throws Throwable {
        return new ModelAndView("redirect:https://www.163.com");
//		return "请登陆";
    }

    @GetMapping("loginPage1")
    public Object loginPage1(HttpServletRequest request) throws Throwable {
        return new ModelAndView("redirect:https://www.163.com");
//		return "请登陆";
    }

}
