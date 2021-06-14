package it.uniroma3.siw.spring.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import it.uniroma3.siw.spring.model.Artista;
import it.uniroma3.siw.spring.model.Collezione;
import it.uniroma3.siw.spring.model.Opera;
import it.uniroma3.siw.spring.model.Opera;
import it.uniroma3.siw.spring.repository.OperaRepository;

@Service
public class OperaService {

	@Autowired
	private OperaRepository operaRepository; 
	
	@Transactional
	public Opera inserisci(Opera opera) {
		return operaRepository.save(opera);
	}

	@Transactional
	public List<Opera> tutti() {
		return (List<Opera>) operaRepository.findAll();
	}
	@Transactional
	public List<Opera> tuttiPerTitolo() {
		return (List<Opera>) operaRepository.findByOrderByTitoloAsc();
	}
	@Transactional
	public List<Opera> tuttiPerAnno() {
		return (List<Opera>) operaRepository.findByOrderByAnnoAsc();
	}
	@Transactional
	public List<Opera> tuttiPerCollezione() {
		return (List<Opera>) operaRepository.findByOrderByCollezioneAsc();
	}

	@Transactional
	public Opera operaPerId(Long id) {
		Optional<Opera> optional = operaRepository.findById(id);
		if (optional.isPresent())
			return optional.get();
		else 
			return null;
	}
	
	@Transactional
	public List<Opera> tutteSenzaCollezione(){
		return (List<Opera>) operaRepository.findAllSenzaCollezione();
	}

	@Transactional
	public void cancella(Opera opera) {
		operaRepository.deleteById(opera.getId());
	}
	
	@Transactional
	public boolean alreadyExists(Opera opera) {
		List<Opera> opere = this.operaRepository.findByTitolo(opera.getTitolo());
		if (opere.size() > 0)
			return true;
		else 
			return false;
	}
	
	public Opera toUpperCase(Opera opera) {
		opera.setTitolo(StringUtils.capitalize(opera.getTitolo()));
		opera.setDescrizione(StringUtils.capitalize(opera.getDescrizione()));
		
		return opera;
	}

	@Transactional
	public List<Opera> operaPerTitolo(String titolo) {
		return operaRepository.findByTitolo(titolo);
	}
	@Transactional
	public List<Opera> operePerArtista(Artista a) {
		return operaRepository.findAllByArtista(a);
	}

	public List<Opera> operePerCollezione(Collezione c) {
		
		return operaRepository.findAllByCollezione(c);
	}

}
