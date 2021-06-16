package it.uniroma3.siw.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.spring.model.Artista;
import it.uniroma3.siw.spring.model.Collezione;
import it.uniroma3.siw.spring.model.Opera;

public interface OperaRepository extends CrudRepository<Opera,Long>{

	public List<Opera> findByTitolo(String titolo);

	@Query("SELECT o FROM Opera o WHERE o.collezione = NULL")
	public List<Opera> findAllSenzaCollezione();

	@Query("SELECT o FROM Opera o WHERE o.artista =?1")
	public List<Opera> findAllByArtista(Artista a);

	@Query("SELECT o FROM Opera o WHERE o.collezione =?1")
	public List<Opera> findAllByCollezione(Collezione c);

	public List<Opera> findByOrderByTitoloAsc();
	public List<Opera> findByOrderByAnnoAsc();
	public List<Opera> findByOrderByCollezioneAsc();
	
	@Query("SELECT o FROM Opera o WHERE titolo LIKE %?1%")
	public List<Opera> findAllByTitoloIsLike(String nome);
	
	@Query("SELECT o FROM Opera o WHERE artista.nome LIKE %?1%")
	public List<Opera> findAllByArtistaIsLike(String nome);
	
	@Query("SELECT o FROM Opera o WHERE artista.cognome LIKE %?1%")
	public List<Opera> findAllByArtistaCognomeIsLike(String cognome);
}
