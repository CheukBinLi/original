package com.cheuks.bin.original.prototype.spring.boot.unit.one;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Min;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@RestController
public class UnitOneController {

	@GetMapping("/get/{id}/by/{me}")
	public String getting(@PathVariable("me") String name, @PathVariable("id") Integer id) {
		return name + " your account no is " + id;
	}

	@GetMapping("/redirect/to/{path}")
	public RedirectView redirect(@PathVariable("path") String path, HttpServletRequest request) {
		// return new ModelAndView("redirect:/" + path, request.getParameterMap());
		RedirectView result = new RedirectView(path);
		result.setAttributesMap(request.getParameterMap());
		return result;
	}

	@GetMapping("/forward/to/{path}")
	public ModelAndView forward(@PathVariable("path") String path, HttpServletRequest request) {
		return new ModelAndView("forward:/" + path, request.getParameterMap());
	}

	/***
	 * @see 校验
	 * @param user
	 * @param result
	 * @return
	 */
	@PostMapping("/validate")
	public String validate(@ModelAttribute("user") @Validated User user, BindingResult result) {
		if (result.hasErrors())
			return result.getFieldError().getDefaultMessage();
		return "success:" + user.id;
	}

	@AllArgsConstructor
	@NoArgsConstructor
	static class User {
		@Setter
		@Min(value = 1, message = "id is null or zero !")
		private Integer id;
	}

	@GetMapping("/test")
	public String test() {
		return "success";
	}

}
