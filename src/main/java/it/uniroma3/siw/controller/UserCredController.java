package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.validator.CredentialsValidator;
import jakarta.validation.Valid;

@Controller
public class UserCredController {

//	@Autowired private UserService userService; 
	@Autowired private CredentialsService credentialsService;
	@Autowired private CredentialsValidator  credentialsValidator; 
	
	@GetMapping(value="/user/personalArea/{username}")
	public String personalArea(Model model, @PathVariable("username") String username) {
		System.out.println(username); 
		Credentials credentials = this.credentialsService.getCredentials(username); 
		if(credentials == null) return "user/userError.html";
		User user = credentials.getUser(); 
		if(user == null) return "user/userError.html";
		model.addAttribute("user",user); 
		model.addAttribute("credentials",credentials); 
		return "user/personalArea.html"; 
	}
	
	@GetMapping(value="/user/formChangePwd/{credentialsId}")
	public String showFormChangePwd(Model model, @PathVariable("credentialsId") Long id) {
		Credentials credentials = this.credentialsService.getCredentials(id); 
		if(credentials == null) return "userError.html";
		//altrimenti 
		model.addAttribute("credentials",credentials);
		return "/user/formChangePwd.html"; 
	}
	
	@PostMapping(value="/user/changePassword")
	public String changePwd(@RequestParam String newPassword, @Valid @ModelAttribute("credentials") Credentials credentials, 
	BindingResult bindingResultCredentials, Model model) {
		 System.out.println("dopo"); 
	 if(credentials == null) return "user/userError.html"; 
	 //altrimenti 
	 String oldPwd = credentials.getPassword(); 
	 credentials.setPassword(newPassword); 
	 this.credentialsValidator.validate(credentials,bindingResultCredentials); 
	 if(bindingResultCredentials.hasErrors()) {credentials.setPassword(oldPwd); this.credentialsService.saveCredentials(credentials); return "/user/formChangePwd.html";} 
	 System.out.println("successo"); 
	 this.credentialsService.saveCredentials(credentials);
	 model.addAttribute("credentials",credentials); 
	 model.addAttribute("user",credentials.getUser());
     return "redirect:/user/personalArea/"+credentials.getUsername(); 
	}
	
}
