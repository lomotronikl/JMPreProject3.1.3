package ru.jm.jmspringboot.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.jm.jmspringboot.model.User;
import ru.jm.jmspringboot.service.UserService;

@Controller
@RequestMapping(path = "/", produces = "application/json;charset=UTF-8")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(value = { "/user"}, method = RequestMethod.GET)
	public String indexPage(ModelMap model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user=userService.getUser(auth.getName());
		model.addAttribute("users", userService.getAllUsers() );
		model.addAttribute("userinfo",user.toString());

		return "user/index";
	}

    @RequestMapping(value = {"/", "/login"}, method = RequestMethod.GET)
    public String loginPage() {
		return "login";
    }



}