package it.uniroma3.siw.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.spring.model.Collezione;

public interface CollezioneRepository extends CrudRepository<Collezione,Long>{

	public List<Collezione> findAllByNome(String nome);
	
	public Collezione findByNome(String nome);
	
	public void deleteById(Long id);

	@Query("SELECT c FROM Collezione c WHERE nome LIKE %?1%")
	public List<Collezione> findAllByNomeIsLike(String nome);

}
