package it.uniroma3.siw.spring.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
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
import it.uniroma3.siw.spring.model.Opera;
import it.uniroma3.siw.spring.repository.OperaRepository;

@Service
public class OperaService {

	@Autowired
	private OperaRepository operaRepository; 

	@Autowired
	private Cloudinary cloudinary;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
		List<Opera> opere = this.operaRepository.findByTitolo(StringUtils.capitalize(opera.getTitolo()));
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
	@Transactional
	public List<Opera> operePerCollezione(Collezione c) {
		return operaRepository.findAllByCollezione(c);
	}
	@Transactional
	public List<Opera> operaPerTitoloIsLike(String titolo) {
		return operaRepository.findAllByTitoloIsLike(StringUtils.capitalize(titolo));
	}


	public void caricaFoto(Opera opera,MultipartFile foto) throws IOException {
		//crea un file temporaneo con dentro la foto (avrei pututo fare anche File file=new File() )
		File file = Files.createTempFile("temp", foto.getOriginalFilename()).toFile();
		foto.transferTo(file);


		//carica su cloudinary la foto e salva in imgSource il link
		try {
			opera.setImgSource((String)cloudinary.uploader().upload(file,ObjectUtils.emptyMap()).get("secure_url"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<Opera> operePerArtisti(List<Artista> artisti) {
		
		return this.operaRepository.findAllByArtistaCognomeIsLike(null);
	}

}
