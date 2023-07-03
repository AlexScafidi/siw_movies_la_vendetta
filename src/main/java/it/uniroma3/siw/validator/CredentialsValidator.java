package it.uniroma3.siw.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import it.uniroma3.siw.model.Credentials;

@Component
public class CredentialsValidator  implements org.springframework.validation.Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return this.getClass().equals(clazz); 
	}

	@Override
	public void validate(Object target, Errors errors) {
		// TODO Auto-generated method stub
		Credentials credentials = (Credentials)target; 
		//controllo psw
		if(!this.pwdValid(credentials.getPassword())) errors.reject("credentials.pwd.format");
		
	}
	
	
	private boolean pwdValid(String pwd) {
		boolean min = false, maiusc = false, number = false;
		for(char c : pwd.toCharArray()) {
			if(Character.isUpperCase(c)) maiusc = true; 
			if(Character.isLowerCase(c)) min = true; 
			if(Character.isDigit(c)) number = true; 
		}
		return maiusc && min && number; 
	}

	

}
