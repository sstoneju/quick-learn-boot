package com.example.web;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.domain.Customer;
import com.example.service.CustomerService;

@Controller
@RequestMapping("customers")
public class CustomerController {
	
	@Autowired
	CustomerService customerService;
	
	@ModelAttribute
	CustomerForm setUpForm() {
		return new CustomerForm();
	}
	
	@RequestMapping(method = RequestMethod.GET)
	String list(Model model) {
		List<Customer> customers = customerService.findAll();
		model.addAttribute("customers", customers);
		return "customers/list"; // Thymeleaf는 default 값으로 classpath:templates/ + "리턴값" + .html
	}
	
	@RequestMapping(value = "create", method = RequestMethod.POST)
	String create(@Validated CustomerForm form, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return list(model);
		}
		Customer customer = new Customer();
		BeanUtils.copyProperties(form, customer);
		customerService.create(customer);
		return "redirect:/customers";
	}
	
	@RequestMapping(value="edit", params="form", method = RequestMethod.GET)
	String editForm(@RequestParam Integer id , CustomerForm form) { // CustomerForm이 어떻게 쓰이는지 자세히 공부하기 
		Customer customer = customerService.findOne(id);
		BeanUtils.copyProperties(customer, form); // beanUtils.copy도 잘 모르겠음. 그냥 복사만하는건지 validation도 하는건지.
		return "customers/edit";
	}
	
	@RequestMapping(value="edit", method = RequestMethod.POST)
	String edit(@RequestParam Integer id, @Validated CustomerForm form, BindingResult result) {
		if (result.hasErrors()) {
			return editForm(id, form);
		}
		Customer customer = new Customer();
		BeanUtils.copyProperties(form, customer);
		customer.setId(id);
		customerService.update(customer);
		return "redirect:/customers";
	}
	
	
	@RequestMapping(value = "edit", params = "goToTop")
	String goToTop() {
		return "redirect:/customers";
	}
	
	@RequestMapping(value="delete", method = RequestMethod.POST)
	String edit(@RequestParam Integer id) {
		customerService.delete(id);
		return "redirect:/customers";
	}
	
	
}
