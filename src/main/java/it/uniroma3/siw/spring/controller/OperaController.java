package it.uniroma3.siw.spring.controller;

import java.io.IOException;

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
import org.springframework.web.multipart.MultipartFile;

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
    
    private String imgSourceCorrente;
    
    
    @RequestMapping(value = "/opere", method = RequestMethod.GET)
    public String getOpere(Model model) {
    		model.addAttribute("opere", this.operaService.tuttiPerTitolo());
    		return "opere.html";
    }
    @RequestMapping(value = "/admin/opere", method = RequestMethod.GET)
    public String getOpereAdmin(Model model) {
    	model.addAttribute("opere", this.operaService.tuttiPerTitolo());
    	return "admin/opere.html";
    }
    @RequestMapping(value="/orderCollezione",method=RequestMethod.GET)
    public String getOperePerCollezione(Model model) {
    	model.addAttribute("opere", this.operaService.tuttiPerCollezione());
    	return "opere.html";
    }
    @RequestMapping(value="/admin/orderCollezione",method=RequestMethod.GET)
    public String getOperePerCollezioneAdmin(Model model) {
    	model.addAttribute("opere", this.operaService.tuttiPerCollezione());
    	return "admin/opere.html";
    }
    
    @RequestMapping(value="/orderAnno",method=RequestMethod.GET)
    public String getOperePerAnno(Model model) {
    	model.addAttribute("opere", this.operaService.tuttiPerAnno());
    	return "opere.html";
    }
    @RequestMapping(value="/admin/orderAnno",method=RequestMethod.GET)
    public String getOperePerAnnoAdmin(Model model) {
    	model.addAttribute("opere", this.operaService.tuttiPerAnno());
    	return "admin/opere.html";
    }
    
    @RequestMapping(value = "/opera/{id}", method = RequestMethod.GET)
    public String getOpera(@PathVariable("id") Long id, Model model) {
    	model.addAttribute("opera", this.operaService.operaPerId(id));
    	return "opera.html";
    }



    
	@RequestMapping(value="/admin/addOpera", method = RequestMethod.GET)
	public String addAdminOpera(Model model) {
		model.addAttribute("opera", new Opera());
		model.addAttribute("altriArtisti",this.artistaService.tuttiDisponibili(null));
		model.addAttribute("altreCollezioni",this.collezioneService.tutteDisponibili(null));
		model.addAttribute("modif", false);

		return "admin/operaForm.html";
	}

    @RequestMapping(value = "/admin/addOpera", method = RequestMethod.POST)
    public String newOpera(@RequestParam Long artistaSelezionato,@RequestParam Long collezioneSelezionata,
    						@RequestParam("foto") MultipartFile foto, @ModelAttribute("opera") Opera opera, Model model, BindingResult bindingResult) {
    	
    	this.operaValidator.validate(opera, bindingResult);
        if (!bindingResult.hasErrors()) {
        	opera.setArtista(this.artistaService.artistaPerId(artistaSelezionato));
        	if(collezioneSelezionata != null)
        		opera.setCollezione(this.collezioneService.collezionePerId(collezioneSelezionata));
           	
        	if(!foto.isEmpty()) {
	        	try {
					this.operaService.caricaFoto(opera,foto);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "error.html";
				}
        	}
        	this.operaService.inserisci(this.operaService.toUpperCase(opera));

            model.addAttribute("opere", this.operaService.tutti());
            return "admin/opere.html";
        }
        model.addAttribute("artisti",this.artistaService.tutti());
		model.addAttribute("collezioni",this.collezioneService.tutti());
        return "admin/operaForm.html";
    }
    
    @RequestMapping(value = "/admin/addOpera", method = RequestMethod.POST, params="modifica")
    public String modificaOpera(@RequestParam Long artistaSelezionato,@RequestParam Long collezioneSelezionata,
    							@RequestParam("foto") MultipartFile foto,@ModelAttribute("opera") Opera opera, Model model, BindingResult bindingResult) {
    
    	logger.debug("*************MODIFICA. ID OPERA= "+opera.getId());
    	this.operaValidator.validateModifica(opera, bindingResult);
    	if (!bindingResult.hasErrors()) {
    		if(artistaSelezionato != null)
    			opera.setArtista(this.artistaService.artistaPerId(artistaSelezionato));
    		if(collezioneSelezionata != null)
    			opera.setCollezione(this.collezioneService.collezionePerId(collezioneSelezionata));
        	
        	if(!foto.isEmpty()) {
	        	try {
					this.operaService.caricaFoto(opera,foto);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return "error.html";
				}
        	}
    		if(opera.getImgSource() == null)
            	opera.setImgSource(imgSourceCorrente);
    		
    		this.operaService.inserisci(this.operaService.toUpperCase(opera));

    		/*if(this.operaService.operaPerId(opera.getId()).getImgSource().equals("") && imgSourceCorrente != null) {
    			opera.setImgSource(imgSourceCorrente);
    			logger.debug("************* HO IMPOSTATO IMGSOURCE****"+opera.getImgSource());
        		this.operaService.inserisci(this.operaService.toUpperCase(opera));

    			imgSourceCorrente=null;
    		}*/
    		

    		model.addAttribute("opere", this.operaService.tutti());
    		return "admin/opere.html";
    	}
    	model.addAttribute("artisti",this.artistaService.tutti());
    	model.addAttribute("collezioni",this.collezioneService.tutti());
    	return "admin/operaForm.html";
    }
    
	@RequestMapping(value = "/opere/modifica/{id}", method = RequestMethod.GET)
	public String modificaOpera(@PathVariable("id") Long id, Model model) {
		this.imgSourceCorrente = this.operaService.operaPerId(id).getImgSource();
		logger.debug("*****IMG SOURCE= "+imgSourceCorrente);
		model.addAttribute("opera",this.operaService.operaPerId(id));
		model.addAttribute("altriArtisti",this.artistaService.tuttiDisponibili(this.operaService.operaPerId(id).getArtista()));
		//altreCollezioni è la lista di tutte le collezioni senza quella di cui l'opera fa parte
		model.addAttribute("altreCollezioni",(this.collezioneService.tutteDisponibili(this.operaService.operaPerId(id).getCollezione())));
		model.addAttribute("modif", true);
		
		return "admin/operaForm.html";
	}
    
	@RequestMapping(value = "/opere/delete/{id}", method = RequestMethod.GET)
	public String cancellaOpera(@PathVariable("id") Long id, Model model) {
		this.operaService.cancella(this.operaService.operaPerId(id));
		model.addAttribute("opere",this.operaService.tutti());
		return "admin/opere.html";
	}
	
	/*		RICERCA		*/
	
	@RequestMapping(value = "/findTitolo",method = RequestMethod.GET)
    public String cercaPerTitolo(@RequestParam("titoloOpera") String titolo, Model model){
    	if(titolo.equals("")) {
    		model.addAttribute("opere",this.operaService.tutti());
    	}else {    		
    		model.addAttribute("opere", this.operaService.operaPerTitoloIsLike(StringUtils.capitalize(titolo)));
    		}
    	
    	return "opere.html";
    }
	@RequestMapping(value = "/admin/findTitolo",method = RequestMethod.GET)
	public String cercaPerTitoloAdmin(@RequestParam("titoloOpera") String titolo, Model model){
		if(titolo.equals("")) {
			model.addAttribute("opere",this.operaService.tutti());
		}else {    		
			model.addAttribute("opere", this.operaService.operaPerTitoloIsLike(StringUtils.capitalize(titolo)));
		}
		
		return "admin/opere.html";
	}
	
	@RequestMapping(value = "/findAutore",method = RequestMethod.GET)
	public String cercaPerAutore(@RequestParam("nomeAutore") String nome, Model model){
		if(nome.equals("")) {
			model.addAttribute("opere",this.operaService.tutti());
		}else {
			
			model.addAttribute("opere", this.operaService.operePerArtista(this.artistaService.artistaPerCognome(StringUtils.capitalize(nome))));
		}
		
		return "opere.html";
	}
	@RequestMapping(value = "/admin/findAutore",method = RequestMethod.GET)
	public String cercaPerAutoreAdmin(@RequestParam("nomeAutore") String nome, Model model){
		if(nome.equals("")) {
			model.addAttribute("opere",this.operaService.tutti());
		}else {
			
			model.addAttribute("opere", this.operaService.operePerArtista(this.artistaService.artistaPerCognome(StringUtils.capitalize(nome))));
		}
		
		return "admin/opere.html";
	}
	
	@RequestMapping(value = "/findCollezione",method = RequestMethod.GET)
	public String cercaPerCollezione(@RequestParam("nomeCollezione") String nome, Model model){
		if(nome.equals("")) {
			model.addAttribute("opere",this.operaService.tutti());
		}else {
			
			model.addAttribute("opere", this.operaService.operePerCollezione(this.collezioneService.collezionePerNome(StringUtils.capitalize(nome))));
		}
		
		return "opere.html";
	}
	@RequestMapping(value = "/admin/findCollezione",method = RequestMethod.GET)
	public String cercaPerCollezioneAdmin(@RequestParam("nomeCollezione") String nome, Model model){
		if(nome.equals("")) {
			model.addAttribute("opere",this.operaService.tutti());
		}else {
			
			model.addAttribute("opere", this.operaService.operePerCollezione(this.collezioneService.collezionePerNome(StringUtils.capitalize(nome))));
		}
		
		return "admin/opere.html";
	}
}
