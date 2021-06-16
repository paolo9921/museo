package it.uniroma3.siw.spring.controller;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import it.uniroma3.siw.spring.controller.validator.ArtistaValidator;
import it.uniroma3.siw.spring.model.Artista;
import it.uniroma3.siw.spring.model.Opera;
import it.uniroma3.siw.spring.service.ArtistaService;

@Controller
public class ArtistaController {
	
	@Autowired
	private ArtistaService artistaService;
	
    @Autowired
    private ArtistaValidator artistaValidator;
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private String imgSourceCorrente;


        
    
    @RequestMapping(value = "/artisti", method = RequestMethod.GET)
    public String getArtisti(Model model) {
    		model.addAttribute("artisti", this.artistaService.tuttiPerNome());
    		return "artisti.html";
    }
    @RequestMapping(value = "admin/artisti", method = RequestMethod.GET)
    public String getArtistiAdmin(Model model) {
    	model.addAttribute("artisti", this.artistaService.tuttiPerNome());
    	return "admin/artisti.html";
    }
    
    @RequestMapping(value = "/orderCognome", method = RequestMethod.GET)
    public String getArtistiPerCognome(Model model) {
    	model.addAttribute("artisti", this.artistaService.tuttiPerCognome());
    	return "artisti.html";
    }
    @RequestMapping(value = "admin/orderCognome", method = RequestMethod.GET)
    public String getArtistiPerCognomeAdmin(Model model) {
    	model.addAttribute("artisti", this.artistaService.tuttiPerCognome());
    	return "admin/artisti.html";
    }
    @RequestMapping(value = "/orderNazione", method = RequestMethod.GET)
    public String getArtistiPerNazione(Model model) {
    	model.addAttribute("artisti", this.artistaService.tuttiPerNazione());
    	return "artisti.html";
    }
    @RequestMapping(value = "admin/orderNazione", method = RequestMethod.GET)
    public String getArtistiPerNazioneAdmin(Model model) {
    	model.addAttribute("artisti", this.artistaService.tuttiPerNazione());
    	return "admin/artisti.html";
    }
    @RequestMapping(value = "/orderNascita", method = RequestMethod.GET)
    public String getArtistiPerNascita(Model model) {
    	model.addAttribute("artisti", this.artistaService.tuttiPerNascita());
    	return "artisti.html";
    }
    @RequestMapping(value = "admin/orderNascita", method = RequestMethod.GET)
    public String getArtistiPerNascitaAdmin(Model model) {
    	model.addAttribute("artisti", this.artistaService.tuttiPerNascita());
    	return "admin/artisti.html";
    }
    @RequestMapping(value = "/orderMorte", method = RequestMethod.GET)
    public String getArtistiPerMorte(Model model) {
    	model.addAttribute("artisti", this.artistaService.tuttiPerMorte());
    	return "artisti.html";
    }
    @RequestMapping(value = "admin/orderMorte", method = RequestMethod.GET)
    public String getArtistiPerMorteAdmin(Model model) {
    	model.addAttribute("artisti", this.artistaService.tuttiPerMorte());
    	return "admin/artisti.html";
    }

    @RequestMapping(value = "/artista/{id}", method = RequestMethod.GET)
    public String getArtista(@PathVariable("id") Long id, Model model) {
    	model.addAttribute("artista", this.artistaService.artistaPerId(id));
    	return "artista.html";
    }
    
    
    /*		ADMIN		*/
    

	
    @RequestMapping(value="/admin/addArtista", method = RequestMethod.GET)
    public String addArtista(Model model) {
      	model.addAttribute("artista", new Artista());
      	model.addAttribute("modif", false);
      	return "admin/artistaForm.html";
    }
	
    @RequestMapping(value = "/admin/addArtista", method = RequestMethod.POST)
    public String newArtista(@RequestParam("foto") MultipartFile foto,@ModelAttribute("artista") Artista artista, 
    									Model model, BindingResult bindingResult) {
    	this.artistaValidator.validate(artista, bindingResult);
        if (!bindingResult.hasErrors()) {
        	
        	if(!foto.isEmpty()) {
	        	try {
					this.artistaService.caricaFoto(artista,foto);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "error.html";
				}
        	}
			this.artistaService.inserisci(this.artistaService.toUpperCase(artista));
			
			model.addAttribute("artisti", this.artistaService.tutti());
            return "admin/artisti.html";
        }
        return "admin/artistaForm.html";
    }
    
    @RequestMapping(value = "/admin/addArtista", method = RequestMethod.POST, params="modifica")
    public String modificaArtista(@ModelAttribute("artista") Artista artista, 
    		Model model, BindingResult bindingResult, @RequestParam("foto") MultipartFile foto){
    	this.artistaValidator.validateModifica(artista, bindingResult);
    	if (!bindingResult.hasErrors()) {			
    		
        	if(!foto.isEmpty()) {
	        	try {
					this.artistaService.caricaFoto(artista,foto);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "error.html";
				}
        	}
        	
        	if(artista.getImgSource() == null)
            	artista.setImgSource(imgSourceCorrente);
        	
    		this.artistaService.inserisci(this.artistaService.toUpperCase(artista));    		
    		model.addAttribute("artisti", this.artistaService.tutti());
    		return "admin/artisti.html";
    	}
    	return "admin/artistaForm.html";
    }
    
    /*		MODIFICA - ELIMINA		*/
   	@RequestMapping(value = "/artisti/modifica/{id}", method = RequestMethod.GET)
	public String modificaArtista(@PathVariable("id") Long id, Model model) {
   		this.imgSourceCorrente = this.artistaService.artistaPerId(id).getImgSource();
		model.addAttribute("artista",this.artistaService.artistaPerId(id));
		model.addAttribute("modif",true);
		return "admin/artistaForm.html";
	}
    
   	

	@RequestMapping(value = "/artisti/delete/{id}", method = RequestMethod.GET)
	public String cancellaArtista(@PathVariable("id") Long id, Model model) {
		List<Opera> opere = this.artistaService.artistaPerId(id).getOpere();
		for (Opera opera : opere)
			opera.setArtista(null);
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
    		model.addAttribute("artisti", this.artistaService.artistaPerNomeIsLike(StringUtils.capitalize(nome)));
    	}
    	
    	return "artisti.html";
    }
    
    @RequestMapping(value = "/findNazione",method = RequestMethod.GET)
    public String cercaPerNazione(@RequestParam("nazioneArtista") String nazione, Model model){
    	//if(nome.equals(""))
    		//return "collezioni.html";
    	model.addAttribute("artisti", this.artistaService.artistaPerNazioneIsLike(StringUtils.capitalize(nazione)));
    	
    	return "artisti.html";
    }

    
}
