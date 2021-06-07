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

import it.uniroma3.siw.spring.controller.validator.ArtistaValidator;
import it.uniroma3.siw.spring.model.Artista;
import it.uniroma3.siw.spring.service.ArtistaService;

@Controller
public class ArtistaController {
	
	@Autowired
	private ArtistaService artistaService;
	
    @Autowired
    private ArtistaValidator artistaValidator;
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private Long idCorrente;

        
    
    @RequestMapping(value = "/artisti", method = RequestMethod.GET)
    public String getArtisti(Model model) {
    		model.addAttribute("artisti", this.artistaService.tuttiPerNome());
    		return "artisti.html";
    }
    
    @RequestMapping(value = "/orderCognome", method = RequestMethod.GET)
    public String getArtistiPerCognome(Model model) {
    	model.addAttribute("artisti", this.artistaService.tuttiPerCognome());
    	return "artisti.html";
    }
    @RequestMapping(value = "/orderNazione", method = RequestMethod.GET)
    public String getArtistiPerNazione(Model model) {
    	model.addAttribute("artisti", this.artistaService.tuttiPerNazione());
    	return "artisti.html";
    }
    @RequestMapping(value = "/orderNascita", method = RequestMethod.GET)
    public String getArtistiPerNascita(Model model) {
    	model.addAttribute("artisti", this.artistaService.tuttiPerNascita());
    	return "artisti.html";
    }
    @RequestMapping(value = "/orderMorte", method = RequestMethod.GET)
    public String getArtistiPerMorte(Model model) {
    	model.addAttribute("artisti", this.artistaService.tuttiPerMorte());
    	return "artisti.html";
    }

    @RequestMapping(value = "/artista/{id}", method = RequestMethod.GET)
    public String getArtista(@PathVariable("id") Long id, Model model) {
    	model.addAttribute("artista", this.artistaService.artistaPerId(id));
    	return "artista.html";
    }
    
    
    /*		ADMIN		*/
    
    @RequestMapping(value = "/admin/artisti", method = RequestMethod.GET)
    public String getArtistiAdmin(Model model) {
    		model.addAttribute("artisti", this.artistaService.tutti());
    		return "admin/artisti.html";
    }
	
    @RequestMapping(value="/admin/addArtista", method = RequestMethod.GET)
    public String addArtista(Model model) {
    	
    	model.addAttribute("artista", new Artista());
        return "admin/artistaForm.html";
    }
	
    @RequestMapping(value = "/admin/addArtista", method = RequestMethod.POST)
    public String newArtista(@ModelAttribute("artista") Artista artista, 
    									Model model, BindingResult bindingResult) {
    	this.artistaValidator.validate(artista, bindingResult);
        if (!bindingResult.hasErrors()) {
	     
	    	this.artistaService.inserisci(this.artistaService.toUpperCase(artista));
            model.addAttribute("artisti", this.artistaService.tutti());
            return "admin/artisti.html";
        }
        return "admin/artistaForm.html";
    }
    
    /*		MODIFICA - ELIMINA		*/
   	@RequestMapping(value = "/artisti/modifica/{id}", method = RequestMethod.GET)
	public String modificaArtista(@PathVariable("id") Long id, Model model) {
		//salvo l'id in una variabile locale per poter eliminare la collezione una volta aggiunta quella nuova (modificata)
		this.idCorrente = id;
		model.addAttribute("artista",this.artistaService.artistaPerId(id));
		
		return "admin/artistaForm.html";
	}
    @RequestMapping(value = "/admin/modificaArtista", method = RequestMethod.POST)
    public String updateArtista(@ModelAttribute("artista") Artista artista, 
    									Model model, BindingResult bindingResult) {
    	this.artistaValidator.validateModifica(artista, bindingResult);
        if (!bindingResult.hasErrors()) {
			if(idCorrente != null) {
				this.artistaService.cancella(this.artistaService.artistaPerId(idCorrente));
				this.idCorrente = null;
			}
	    	this.artistaService.inserisci(this.artistaService.toUpperCase(artista));
            model.addAttribute("artisti", this.artistaService.tutti());
            return "admin/artisti.html";
        }
        return "admin/artistaForm.html";
    }
   	

	@RequestMapping(value = "/artisti/delete/{id}", method = RequestMethod.GET)
	public String cancellaArtista(@PathVariable("id") Long id, Model model) {
		this.artistaService.cancella(this.artistaService.artistaPerId(id));
		model.addAttribute("artisti",this.artistaService.tutti());
		return "admin/artisti.html";
	}
	
	
    /*		RICERCA		*/
    @RequestMapping(value = "/findName",method = RequestMethod.GET)
    public String cercaPerNome(@RequestParam("nomeArtista") String nome, Model model){
    	if(nome.equals("")) {
    		model.addAttribute("artisti",this.artistaService.tutti());
    	}else {
    		
    		model.addAttribute("artisti", this.artistaService.artistaPerNome(nome));
    	}
    	
    	return "artisti.html";
    }
    
    @RequestMapping(value = "/findNazione",method = RequestMethod.GET)
    public String cercaPerNazione(@RequestParam("nazioneArtista") String nazione, Model model){
    	//if(nome.equals(""))
    		//return "collezioni.html";
    	model.addAttribute("artisti", this.artistaService.artistaPerNazione(nazione));
    	
    	return "artisti.html";
    }

    
}
