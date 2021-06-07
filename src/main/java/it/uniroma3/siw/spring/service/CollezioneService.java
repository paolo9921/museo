package it.uniroma3.siw.spring.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.spring.model.Collezione;
import it.uniroma3.siw.spring.repository.CollezioneRepository;

@Service
public class CollezioneService {

	@Autowired 
	private CollezioneRepository collezioneRepository;
	
	
	@Transactional 
	public Collezione inserisci(Collezione collezione) {
		return collezioneRepository.save(collezione);
	}
	
	@Transactional
	public List<Collezione> tutti() {
		return (List<Collezione>) collezioneRepository.findAll();
	}
	
	@Transactional
	public Collezione collezionePerId(Long id) {
		Optional<Collezione> optional = collezioneRepository.findById(id);
		if (optional.isPresent())
			return optional.get();
		else 
			return null;
	}

	
	@Transactional
	public List<Collezione> collezionePerNome(String nome) {
		return collezioneRepository.findByNome(nome);
	}
	
	
	@Transactional
	public void cancella(Collezione collezione) {
		collezioneRepository.deleteById(collezione.getId());
	}

		
	@Transactional
	public boolean alreadyExists(Collezione collezione) {
		List<Collezione> studenti = this.collezioneRepository.findByNome(collezione.getNome());
		if (studenti.size() > 0)
			return true;
		else 
			return false;
	}
	
	
	


}
