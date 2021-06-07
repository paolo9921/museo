package it.uniroma3.siw.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.util.StringUtils;

import it.uniroma3.siw.spring.controller.validator.CollezioneValidator;
import it.uniroma3.siw.spring.model.Collezione;
import it.uniroma3.siw.spring.service.CollezioneService;
import it.uniroma3.siw.spring.service.CuratoreService;

@Controller
public class CollezioneController {
	
	@Autowired
	private CollezioneService collezioneService;
	
	@Autowired
	private CollezioneValidator collezioneValidator;
	
	@Autowired
	private CuratoreService curatoreService;
	
	private Long idCorrente;
	
	
	@RequestMapping(value="/collezioni", method = RequestMethod.GET)
	public String getCollezioni(Model model) {
		model.addAttribute("collezioni", this.collezioneService.tutti());
		return "collezioni.html";
	}
	
    @RequestMapping(value = "/collezione/{id}", method = RequestMethod.GET)
    public String getCollezione(@PathVariable("id") Long id, Model model) {
    	model.addAttribute("collezione", this.collezioneService.collezionePerId(id));
    	return "collezione.html";
    }
    
	@RequestMapping(value="/admin/collezioni", method = RequestMethod.GET)
	public String getCollezioniAdmin(Model model) {
		model.addAttribute("collezioni", this.collezioneService.tutti());
		return "admin/collezioni.html";
	}
	
	@RequestMapping(value="/admin/addCollezione", method = RequestMethod.GET)
	public String addCollezione(Model model) {
		model.addAttribute("collezione", new Collezione());
		model.addAttribute("curatori", this.curatoreService.tutti());
		return "admin/collezioneForm.html";
	}

	@RequestMapping(value = "/admin/addCollezione", method = RequestMethod.POST)
	public String newCollezione(@RequestParam Long curatoreSelezionato,@ModelAttribute("collezione") Collezione collezione, Model model,BindingResult bindingResult) {
		this.collezioneValidator.validate(collezione, bindingResult);
		if (!bindingResult.hasErrors()) {
			collezione.setCuratore(this.curatoreService.curatorePerId(curatoreSelezionato));
			collezione.setNome(StringUtils.capitalize(collezione.getNome()));
			this.collezioneService.inserisci(collezione);
			//se sto nella modifica di una collezione elimino quella vecchia
			if(idCorrente != null) {
				this.collezioneService.cancella(this.collezioneService.collezionePerId(idCorrente));
				this.idCorrente = null;
			}
			model.addAttribute("collezioni", this.collezioneService.tutti());
			return "admin/collezioni.html";
		}
		model.addAttribute("curatori", this.curatoreService.tutti());
		return "admin/collezioneForm.html";
	}
	
	@RequestMapping(value = "/collezioni/modifica/{id}", method = RequestMethod.GET)
	public String modificaCollezione(@PathVariable("id") Long id, Model model) {
		//salvo l'id in una variabile locale per poter eliminare la collezione una volta aggiunta quella nuova (modificata)
		this.idCorrente = id;
		model.addAttribute("collezione",this.collezioneService.collezionePerId(id));
		model.addAttribute("curatori", this.curatoreService.tutti());
		
		return "admin/collezioneForm.html";
	}

	
	@RequestMapping(value = "/collezioni/delete/{id}", method = RequestMethod.GET)
	public String cancellaCollezione(@PathVariable("id") Long id, Model model) {
		this.collezioneService.cancella(this.collezioneService.collezionePerId(id));
		model.addAttribute("collezioni",this.collezioneService.tutti());
		return "admin/collezioni.html";
	}

	
    @RequestMapping(value = "/findCollezioneName",method = RequestMethod.GET)
    public String cercaCollezionePerNome(@RequestParam("nomeCollezione") String nome, Model model){
    	if(nome.equals("")) {
    		model.addAttribute("collezioni",this.collezioneService.tutti());
    	}else {
    		
    		model.addAttribute("collezioni", this.collezioneService.collezionePerNome(nome));
    	}
    	
    	return "collezioni.html";
    }
	
}
