package com.cheuks.bin.spring.boot.data.jpa.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cheuks.bin.spring.boot.data.jpa.model.DemoModel;
import com.cheuks.bin.spring.boot.data.jpa.service.DemoService;

@RestController
public class JpaController {

	@Autowired
	DemoService<DemoModel> demoService;

	@ResponseBody
	@GetMapping("/add/{name}")
	public Object add(@PathVariable(required = false, name = "name") String name, HttpServletRequest request,
			HttpServletResponse response) {
		return demoService.add(new DemoModel(name + "_" + System.currentTimeMillis()));
	}

	@ResponseBody
	@GetMapping("/find")
	public Object findList(HttpServletRequest request, HttpServletResponse response) {
		return demoService.getList();
	}

	@ResponseBody
	@GetMapping("/find/{page}/{size}")
	public Object getPage(@PathVariable(required = false, name = "page") int page,
			@PathVariable(required = false, name = "size") int size, HttpServletRequest request,
			HttpServletResponse response) {
		return demoService.getPage(page, size);
	}

}
