package it.uniroma3.siw.spring.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import it.uniroma3.siw.spring.model.Collezione;
import it.uniroma3.siw.spring.service.CollezioneService;

@Component
public class CollezioneValidator implements Validator{

	@Autowired
	private CollezioneService collezioneService;
	

	
	@Override
	public void validate(Object o, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome", "required");
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "curatore", "required");
		
		
		if (!errors.hasErrors()) {
			if (this.collezioneService.alreadyExists((Collezione)o)) {
				errors.reject("collezione.duplicato");
			}
		}
	}
	
	public void validateModifica(Object o, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nome", "required");

	}

	@Override
	public boolean supports(Class<?> clazz) {
		// TODO Auto-generated method stub
		return false;
	}

}
