package com.spring.rajesh.junitexamples.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.rajesh.junitexamples.domain.EmployeeView;
import com.spring.rajesh.junitexamples.service.MyService;

@RestController
@RequestMapping("/account")
public class MyController {

	private MyService myService;

	@Autowired
	public MyController(MyService myService) {
		this.myService = myService;
	}

	@GetMapping("/{id}")
	public @ResponseBody EmployeeView get(@PathVariable int id) {
		return myService.findEmployeeViewById(id);
	}
}
