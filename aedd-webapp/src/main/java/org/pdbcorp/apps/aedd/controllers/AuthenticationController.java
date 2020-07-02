/*
 * Copyright 2020 PDB Corp.
 *
 * Proprietary Software built off of open-source software?
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.pdbcorp.apps.aedd.controllers;

import javax.validation.Valid;

import org.pdbcorp.apps.aedd.commands.LoginCommand;
import org.pdbcorp.apps.aedd.commands.SignupCommand;
import org.pdbcorp.apps.aedd.data.models.User;
import org.pdbcorp.apps.aedd.services.AppUserDetailsService;
import org.pdbcorp.apps.aedd.services.SignupCommandConversionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author jaradat-pdb
 *
 */
@Controller
public class AuthenticationController {
	private AppUserDetailsService appUserDetailsService;
	private SignupCommandConversionService signupCommandConversionService;

	@Autowired
	public AuthenticationController(
			AppUserDetailsService appUserDetailsService, SignupCommandConversionService signupCommandConversionService) {
		
		this.appUserDetailsService = appUserDetailsService;
		this.signupCommandConversionService = signupCommandConversionService;
	}

	@GetMapping("/login")
	public String getLogin(Model model) {
		model.addAttribute("loginCommand", new LoginCommand());
		return "login";
	}

	@GetMapping("/goodbye")
	public String getGoodbye() {
		return "goodbye";
	}

	@GetMapping("/signup")
	public String getSignup(Model model) {
		model.addAttribute("signupCommand", new SignupCommand());
		return "signup";
	}

	@PostMapping("/signup")
	public ModelAndView signupNewUser(@Valid SignupCommand signupCommand, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		User userExists = appUserDetailsService.findUserByEmail(signupCommand.getEmail());
		if(userExists != null) {
			bindingResult.rejectValue("email", "error.user", "The provided email is already registered in the system");
		}
		
		if(bindingResult.hasErrors()) {
			modelAndView.setViewName("signup");
		}
		else {
			appUserDetailsService.saveUser(signupCommandConversionService.convert(signupCommand));
			modelAndView.addObject("successMessage", "User has been registered successfully");
			modelAndView.addObject("loginCommand", new LoginCommand());
			modelAndView.setViewName("login");
		}
		return modelAndView;
	}
}
