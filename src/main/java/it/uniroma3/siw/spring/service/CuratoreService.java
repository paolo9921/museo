package it.uniroma3.siw.spring.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	 private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
		logger.debug("***STO QUA**");
		//devo mettere la prima lettera maiuscola perche nel db sono salvati cosi
		List<Curatore> curatori = this.curatoreRepository.findByNomeAndCognomeAndEmail(StringUtils.capitalize(curatore.getNome()), StringUtils.capitalize(curatore.getCognome()),curatore.getEmail());
		if (curatori.size() > 0)
			return true;
		else 
			return false;
	}
	
	public Curatore toUpperCase(Curatore curatore) {
		curatore.setNome(StringUtils.capitalize(curatore.getNome()));
		curatore.setCognome(StringUtils.capitalize(curatore.getCognome()));
		if(curatore.getLuogoNascita()!= null)
			curatore.setLuogoNascita(StringUtils.capitalize(curatore.getLuogoNascita()));
		
		return curatore;
	}
}
