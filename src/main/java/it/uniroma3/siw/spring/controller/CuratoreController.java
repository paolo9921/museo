package it.uniroma3.siw.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.spring.controller.validator.CuratoreValidator;
import it.uniroma3.siw.spring.model.Collezione;
import it.uniroma3.siw.spring.model.Curatore;
import it.uniroma3.siw.spring.model.Opera;
import it.uniroma3.siw.spring.service.CuratoreService;

@Controller
public class CuratoreController {

	@Autowired
	private CuratoreService curatoreService;
	
	@Autowired
	private CuratoreValidator curatoreValidator;

	private Long idCorrente;
	
	
	@RequestMapping(value="/admin/addCuratore", method = RequestMethod.GET)
	public String addCuratore(Model model) {
		model.addAttribute("curatore", new Curatore());
		model.addAttribute("modif",false);
		return "admin/curatoreForm.html";
	}
	
    @RequestMapping(value = "/admin/curatori", method = RequestMethod.GET)
    public String getCuratoriAdmin(Model model) {
    		model.addAttribute("curatori", this.curatoreService.tutti());
    		return "admin/curatori.html";
    }
    
    @RequestMapping(value = "/admin/addCuratore", method = RequestMethod.POST)
    public String newCuratore(@ModelAttribute("curatore") Curatore curatore, Model model, BindingResult bindingResult) {
    	this.curatoreValidator.validate(curatore, bindingResult);
        if (!bindingResult.hasErrors()) {
          	
          	if(idCorrente != null) {
          		curatore.setId(idCorrente);
  				this.idCorrente = null;
			}
          	this.curatoreService.inserisci(this.curatoreService.toUpperCase(curatore));
            model.addAttribute("curatori", this.curatoreService.tutti());
            return "admin/curatori.html";
        }
        return "admin/curatoreForm.html";
    }
    @RequestMapping(value = "/admin/addCuratore", method = RequestMethod.POST, params="modifica")
    public String modificaCuratore(@ModelAttribute("curatore") Curatore curatore, Model model, BindingResult bindingResult) {
    	this.curatoreValidator.validateModifica(curatore, bindingResult);
        if (!bindingResult.hasErrors()) {
          	
          /*	if(idCorrente != null) {
          		curatore.setId(idCorrente);
  				this.idCorrente = null;
			}*/
          	this.curatoreService.inserisci(this.curatoreService.toUpperCase(curatore));
            model.addAttribute("curatori", this.curatoreService.tutti());
            return "admin/curatori.html";
        }
        return "admin/curatoreForm.html";
    }
    
	@RequestMapping(value = "/curatori/modifica/{id}", method = RequestMethod.GET)
	public String modificaCollezione(@PathVariable("id") Long id, Model model) {
		//salvo l'id in una variabile locale per poter eliminare la collezione una volta aggiunta quella nuova (modificata)
		//this.idCorrente = id;
		model.addAttribute("curatore",this.curatoreService.curatorePerId(id));
		model.addAttribute("modif",true);

		return "admin/curatoreForm.html";
	}
    
	@RequestMapping(value = "/curatori/delete/{id}", method = RequestMethod.GET)
	public String cancellaCuratore(@PathVariable("id") Long id, Model model) {
		List<Collezione> collezioni = this.curatoreService.curatorePerId(id).getCollezioni();
		for (Collezione collezione : collezioni)
			collezione.setCuratore(null);
		this.curatoreService.cancella(this.curatoreService.curatorePerId(id));
		model.addAttribute("curatori",this.curatoreService.tutti());
		return "admin/curatori.html";
	}
}
