package com.spring.rajesh.junitexamples;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class MyController {

	@Autowired
	private MyService myService;
	
	@GetMapping("/{id}")
	public @ResponseBody Account get(@PathVariable int id) {
		return myService.findAccountById(id);
	}
}
