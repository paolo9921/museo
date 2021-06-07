package it.uniroma3.siw.spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.spring.controller.validator.OperaValidator;
import it.uniroma3.siw.spring.model.Opera;
import it.uniroma3.siw.spring.service.ArtistaService;
import it.uniroma3.siw.spring.service.CollezioneService;
import it.uniroma3.siw.spring.service.OperaService;

@Controller
public class OperaController {
	
	@Autowired
	private OperaService operaService;
	
    @Autowired
    private OperaValidator operaValidator;
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
	private ArtistaService artistaService;
    @Autowired
	private CollezioneService collezioneService;

	private Long idCorrente;
    
    
    @RequestMapping(value = "/opere", method = RequestMethod.GET)
    public String getOpere(Model model) {
    		model.addAttribute("opere", this.operaService.tutti());
    		return "opere.html";
    }
    
    @RequestMapping(value = "/opera/{id}", method = RequestMethod.GET)
    public String getOpera(@PathVariable("id") Long id, Model model) {
    	model.addAttribute("opera", this.operaService.operaPerId(id));
    	return "opera.html";
    }


    @RequestMapping(value = "/admin/opere", method = RequestMethod.GET)
    public String getOpereAdmin(Model model) {
    		model.addAttribute("opere", this.operaService.tutti());
    		return "admin/opere.html";
    }
    
	@RequestMapping(value="/admin/addOpera", method = RequestMethod.GET)
	public String addAdminOpera(Model model) {
		model.addAttribute("opera", new Opera());
		model.addAttribute("artisti",this.artistaService.tutti());
		model.addAttribute("collezioni",this.collezioneService.tutti());
		return "admin/operaForm.html";
	}

    @RequestMapping(value = "/admin/addOpera", method = RequestMethod.POST)
    public String newOpera(@RequestParam Long artistaSelezionato,@RequestParam Long collezioneSelezionata,
    						@ModelAttribute("opera") Opera opera, Model model, BindingResult bindingResult) {
    	this.operaValidator.validate(opera, bindingResult);
        if (!bindingResult.hasErrors()) {
        	opera.setArtista(this.artistaService.artistaPerId(artistaSelezionato));
        	opera.setCollezione(this.collezioneService.collezionePerId(collezioneSelezionata));
           	this.operaService.inserisci(this.operaService.toUpperCase(opera));
           	if(idCorrente != null) {
				this.operaService.cancella(this.operaService.operaPerId(idCorrente));
				this.idCorrente = null;
			}
            model.addAttribute("opere", this.operaService.tutti());
            return "admin/opere.html";
        }
        model.addAttribute("artisti",this.artistaService.tutti());
		model.addAttribute("collezioni",this.collezioneService.tutti());
        return "admin/operaForm.html";
    }
    
	@RequestMapping(value = "/opere/modifica/{id}", method = RequestMethod.GET)
	public String modificaOpera(@PathVariable("id") Long id, Model model) {
		//salvo l'id in una variabile locale per poter eliminare l'opera una volta aggiunta quella nuova (modificata)
		this.idCorrente = id;
		model.addAttribute("opera",this.operaService.operaPerId(id));
		model.addAttribute("artisti",this.artistaService.tutti());
		model.addAttribute("collezioni",this.collezioneService.tutti());
		
		return "admin/operaForm.html";
	}
    
	@RequestMapping(value = "/opere/delete/{id}", method = RequestMethod.GET)
	public String cancellaOpera(@PathVariable("id") Long id, Model model) {
		this.operaService.cancella(this.operaService.operaPerId(id));
		model.addAttribute("opere",this.operaService.tutti());
		return "admin/opere.html";
	}
    
}
