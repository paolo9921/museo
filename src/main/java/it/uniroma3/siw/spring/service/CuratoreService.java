package it.uniroma3.siw.spring.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import it.uniroma3.siw.spring.model.Artista;
import it.uniroma3.siw.spring.model.Collezione;
import it.uniroma3.siw.spring.model.Curatore;
import it.uniroma3.siw.spring.repository.CuratoreRepository;

@Service
public class CuratoreService {

	@Autowired
	private CuratoreRepository curatoreRepository; 
	
	@Transactional
	public Curatore inserisci(Curatore curatore) {
		return curatoreRepository.save(curatore);
	}

	@Transactional
	public List<Curatore> tutti() {
		return (List<Curatore>) curatoreRepository.findAll();
	}

	@Transactional
	public Curatore curatorePerId(Long id) {
		Optional<Curatore> optional = curatoreRepository.findById(id);
		if (optional.isPresent())
			return optional.get();
		else 
			return null;
	}
	
	@Transactional
	public void cancella(Curatore curatore) {
		curatoreRepository.deleteById(curatore.getId());
	}

	@Transactional
	public boolean alreadyExists(Curatore curatore) {
		List<Curatore> opere = this.curatoreRepository.findByNomeAndCognome(curatore.getNome(), curatore.getCognome());
		if (opere.size() > 0)
			return true;
		else 
			return false;
	}
	
	public Curatore toUpperCase(Curatore curatore) {
		curatore.setNome(StringUtils.capitalize(curatore.getNome()));
		curatore.setCognome(StringUtils.capitalize(curatore.getCognome()));
		
		return curatore;
	}
}
