package it.uniroma3.siw.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.spring.model.Artista;

public interface ArtistaRepository extends CrudRepository<Artista, Long>{

	public List<Artista> findByNome(String nome);

	public List<Artista> findByNomeAndCognome(String nome, String cognome);

	public List<Artista> findByNomeOrCognome(String nome, String cognome);

	public List<Artista> findByNazionalita(String nazione);
	
	public List<Artista> findByOrderByNomeAsc();
	
	public List<Artista> findByOrderByCognomeAsc();

	public List<Artista> findByOrderByNazionalitaAsc();
	
	public List<Artista> findByOrderByDataNascitaAsc();

	public List<Artista> findByOrderByDataMorteAsc();

	public Artista findByCognome(String nome);
	
	@Query("SELECT a FROM Artista a WHERE nome LIKE %?1% OR cognome LIKE %?1%")
	public List<Artista> findAllByNomeOrCognomeIsLike(String nome);
	
	@Query("SELECT a FROM Artista a WHERE nazionalita LIKE %?1%")
	public List<Artista> findAllByNazioneIsLike(String nazione);


}
