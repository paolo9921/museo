package it.uniroma3.siw.spring.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import it.uniroma3.siw.spring.model.Artista;
import it.uniroma3.siw.spring.model.Collezione;
import it.uniroma3.siw.spring.repository.ArtistaRepository;

@Service
public class ArtistaService {
	
	@Autowired 
	private ArtistaRepository artistaRepository;
	
	@Autowired
	private Cloudinary cloudinary;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
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
	
	
	
	@Transactional
	public void cancella(Artista artista) {
		artistaRepository.deleteById(artista.getId());
	}
	
	@Transactional
	public boolean alreadyExists(Artista artista) {
		List<Artista> studenti = this.artistaRepository.findByNomeAndCognome(StringUtils.capitalize(artista.getNome()),StringUtils.capitalize(artista.getCognome()));
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


	public void caricaFoto(Artista artista,MultipartFile foto) throws IOException {
		//crea un file temporaneo con dentro la foto (avrei pututo fare anche File file=new File() )
		File file = Files.createTempFile("temp", foto.getOriginalFilename()).toFile();
		foto.transferTo(file);
			
		
		//carica su cloudinary la foto e salva in imgSource il link
		try {
			artista.setImgSource((String)cloudinary.uploader().upload(file,ObjectUtils.emptyMap()).get("secure_url"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		public List<Artista> artistaPerNomeIsLike(String nome) {	
			return this.artistaRepository.findAllByNomeOrCognomeIsLike(nome);
		}
		
		@Transactional
		public List<Artista> artistaPerNazioneIsLike(String nazione) {	
			return this.artistaRepository.findAllByNazioneIsLike(nazione);
		}
		
}
