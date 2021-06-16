package it.uniroma3.siw.spring.controller.validator;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import ch.qos.logback.classic.Logger;
import it.uniroma3.siw.spring.model.Artista;
import it.uniroma3.siw.spring.model.Curatore;
import it.uniroma3.siw.spring.service.CuratoreService;

import org.slf4j.LoggerFactory;
@Component
public class CuratoreValidator implements Validator {

	@Autowired
	private CuratoreService curatoreService;
	
	 private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void validate(Object o, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cognome", "required");

		if (!errors.hasErrors()) {
			
			if (this.curatoreService.alreadyExists((Curatore)o)) {
			
				errors.reject("curatore.duplicato");
			}
		}
	}

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;
	}

	public void validateModifica(Object o, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome", "required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cognome", "required");
		
		if (this.curatoreService.alreadyExists((Curatore)o)) {
			
			errors.reject("curatore.duplicato");
		}
		
	}
}
