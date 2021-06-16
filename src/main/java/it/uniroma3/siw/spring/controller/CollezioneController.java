package it.uniroma3.siw.spring.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.spring.controller.validator.CollezioneValidator;
import it.uniroma3.siw.spring.model.Collezione;
import it.uniroma3.siw.spring.model.Opera;
import it.uniroma3.siw.spring.service.CollezioneService;
import it.uniroma3.siw.spring.service.CuratoreService;
import it.uniroma3.siw.spring.service.OperaService;

@Controller
public class CollezioneController {
	
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	@Autowired
	private CollezioneService collezioneService;
	
	@Autowired
	private CollezioneValidator collezioneValidator;
	
	@Autowired
	private CuratoreService curatoreService;
	
	@Autowired
	private OperaService operaService;
	
	
	
	@RequestMapping(value="/collezioni", method = RequestMethod.GET)
	public String getCollezioni(Model model) {
		model.addAttribute("collezioni", this.collezioneService.tutti());
		return "collezioni.html";
	}
	@RequestMapping(value="/admin/collezioni", method = RequestMethod.GET)
	public String getCollezioniAdmin(Model model) {
		model.addAttribute("collezioni", this.collezioneService.tutti());
		return "admin/collezioni.html";
	}
	
    @RequestMapping(value = "/collezione/{id}", method = RequestMethod.GET)
    public String getCollezione(@PathVariable("id") Long id, Model model) {
    	model.addAttribute("collezione", this.collezioneService.collezionePerId(id));
    	return "collezione.html";
    }
    

	
	@RequestMapping(value="/admin/addCollezione", method = RequestMethod.GET)
	public String addCollezione(Model model) {
		model.addAttribute("collezione", new Collezione());
		model.addAttribute("curatori", this.curatoreService.tutti());
		model.addAttribute("opere", this.operaService.tutteSenzaCollezione());
		model.addAttribute("modif", false);

		
		return "admin/collezioneForm.html";
	}

	@RequestMapping(value = "/admin/addCollezione", method = RequestMethod.POST)
	public String newCollezione(@RequestParam(name="curatoreSelezionato", required=false)Long curatoreSelezionato, @RequestParam(name="opera", required=false)Long operaSelezionata,
								@ModelAttribute("collezione") Collezione collezione, Model model,BindingResult bindingResult) {
		logger.debug("*******ADD COLLEZIONE*****");
		this.collezioneValidator.validate(collezione, bindingResult);
		if (!bindingResult.hasErrors()) {
						
			if(collezione.getCuratore() != null)
				collezione.setCuratore(this.curatoreService.curatorePerId(curatoreSelezionato));
			collezione.setNome(StringUtils.capitalize(collezione.getNome()));
			if(operaSelezionata != null)
				this.operaService.operaPerId(operaSelezionata).setCollezione(collezione);

			this.collezioneService.inserisci(collezione);
			
			model.addAttribute("collezioni", this.collezioneService.tutti());
			return "admin/collezioni.html";
		}
		model.addAttribute("curatori", this.curatoreService.tutti());
		return "admin/collezioneForm.html";
	}
	
	@RequestMapping(value = "/admin/addCollezione", method = RequestMethod.POST, params="modifica")
	public String modificaCollezione(@ModelAttribute("collezione") Collezione collezione,
									 @RequestParam(name="curatoreSelezionato", required=false)Long curatoreSelezionato, 
									 @RequestParam(name="opera", required=false)Long operaSelezionata, Model model,BindingResult bindingResult) {
		logger.debug("*******MODIFICA*****");
		this.collezioneValidator.validateModifica(collezione, bindingResult);
		if (!bindingResult.hasErrors()) {
			//collezione.setId(idCorrente);
			logger.debug("************ID CORRENTE: "+collezione.getId()+".");
			logger.debug("*****ID OPERA: "+operaSelezionata);

			collezione.setNome(StringUtils.capitalize(collezione.getNome()));
			if(curatoreSelezionato != null)
				collezione.setCuratore(this.curatoreService.curatorePerId(curatoreSelezionato));
			if(operaSelezionata != null)
				this.operaService.operaPerId(operaSelezionata).setCollezione(collezione);
			this.collezioneService.inserisci(collezione);
			//se sto nella modifica di una collezione elimino quella vecchia
			/*if(idCorrente != null) {
				this.collezioneService.cancella(this.collezioneService.collezionePerId(idCorrente));
				this.idCorrente = null;
			}*/
			model.addAttribute("collezioni", this.collezioneService.tutti());
			return "admin/collezioni.html";
		}
		model.addAttribute("curatori", this.curatoreService.tutti());
		return "admin/collezioneForm.html";
	}
	
	@RequestMapping(value = "/collezioni/modifica/{id}", method = RequestMethod.GET)
	public String modificaCollezione(@PathVariable("id") Long id, Model model) {
		//salvo l'id in una variabile locale per poter eliminare la collezione una volta aggiunta quella nuova (modificata)
		//this.idCorrente = id;
		model.addAttribute("collezione",this.collezioneService.collezionePerId(id));
		model.addAttribute("curatori", this.curatoreService.tutti());
		model.addAttribute("opere", this.operaService.tutteSenzaCollezione());
		model.addAttribute("modif", true);
		logger.debug("*******ID= "+id);

		return "admin/collezioneForm.html";
	}

	
	@RequestMapping(value = "/collezioni/delete/{id}", method = RequestMethod.GET)
	public String cancellaCollezione(@PathVariable("id") Long id, Model model) {
		List<Opera> opere = this.collezioneService.collezionePerId(id).getOpere();
		for (Opera opera : opere) {
			opera.setCollezione(null);
			logger.debug("collezione opera: "+opera.getCollezione()+" fine");
		}
		this.collezioneService.cancella(this.collezioneService.collezionePerId(id));
		model.addAttribute("collezioni",this.collezioneService.tutti());
		return "admin/collezioni.html";
	}

	
    @RequestMapping(value = "/findCollezioneName",method = RequestMethod.GET)
    public String cercaCollezionePerNome(@RequestParam("nomeCollezione") String nome, Model model){
    	if(nome.equals("")) {
    		model.addAttribute("collezioni",this.collezioneService.tutti());
    	}else {
    		
    		model.addAttribute("collezioni", this.collezioneService.collezionePerNomeIsLike(StringUtils.capitalize(nome)));
    	}
    	
    	return "collezioni.html";
    }
    @RequestMapping(value = "/admin/findCollezioneName",method = RequestMethod.GET)
    public String cercaCollezionePerNomeAdmin(@RequestParam("nomeCollezione") String nome, Model model){
    	if(nome.equals("")) {
    		model.addAttribute("collezioni",this.collezioneService.tutti());
    	}else {
    		
    		model.addAttribute("collezioni", this.collezioneService.collezionePerNomeIsLike(StringUtils.capitalize(nome)));
    	}
    	
    	return "admin/collezioni.html";
    }
	
}
