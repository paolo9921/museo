package it.uniroma3.siw.spring.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import it.uniroma3.siw.spring.model.Artista;
import it.uniroma3.siw.spring.model.Collezione;
import it.uniroma3.siw.spring.repository.ArtistaRepository;

@Service
public class ArtistaService {
	
	@Autowired 
	private ArtistaRepository artistaRepository;
	
	
	@Transactional 
	public Artista inserisci(Artista artista) {
		return artistaRepository.save(artista);
	}
	
	
	@Transactional
	public List<Artista> tutti() {
		return (List<Artista>) artistaRepository.findAll();
	}
	@Transactional
	public List<Artista> tuttiDisponibili(Artista a){
		List<Artista> lista = (List<Artista>)artistaRepository.findAll();
		lista.remove(a);
		return lista;
	}
	
	@Transactional
	public List<Artista> tuttiPerNome() {
		return (List<Artista>) artistaRepository.findByOrderByNomeAsc();
	}
	@Transactional
	public List<Artista> tuttiPerCognome() {
		return (List<Artista>) artistaRepository.findByOrderByCognomeAsc();
	}
	@Transactional
	public List<Artista> tuttiPerNazione() {
		return (List<Artista>) artistaRepository.findByOrderByNazionalitaAsc();
	}
	@Transactional
	public List<Artista> tuttiPerNascita() {
		return (List<Artista>) artistaRepository.findByOrderByDataNascitaAsc();
	}
	@Transactional
	public List<Artista> tuttiPerMorte() {
		return (List<Artista>) artistaRepository.findByOrderByDataMorteAsc();
	}
	
	@Transactional
	public Artista artistaPerId(Long id) {
		Optional<Artista> optional = artistaRepository.findById(id);
		if (optional.isPresent())
			return optional.get();
		else 
			return null;
	}
	
	//la variabile nome potrebbe essere anche il cognome che l'utente ha cercato
	@Transactional
	public List<Artista> artistaPerNome(String nome) {
		return artistaRepository.findByNomeOrCognome(nome, nome);
	}
	
	@Transactional
	public List<Artista> artistaPerNazione(String nazione) {
		return artistaRepository.findByNazionalita(nazione);
	}
	
	@Transactional
	public void cancella(Artista artista) {
		artistaRepository.deleteById(artista.getId());
	}
	
	@Transactional
	public boolean alreadyExists(Artista artista) {
		List<Artista> studenti = this.artistaRepository.findByNomeAndCognome(artista.getNome(), artista.getCognome());
		if (studenti.size() > 0)
			return true;
		else 
			return false;
	}
	
	//restituisce l'artista con le iniziali maiuscole
	public Artista toUpperCase(Artista artista) {
		artista.setNome(StringUtils.capitalize(artista.getNome()));
		artista.setCognome(StringUtils.capitalize(artista.getCognome()));
		artista.setNazionalita(StringUtils.capitalize(artista.getNazionalita()));
		artista.setLuogoNascita(StringUtils.capitalize(artista.getLuogoNascita()));
		artista.setLuogoMorte(StringUtils.capitalize(artista.getLuogoMorte()));
		
		return artista;
	}


	public Artista artistaPerCognome(String nome) {
		return this.artistaRepository.findByCognome(nome);
	}
}
