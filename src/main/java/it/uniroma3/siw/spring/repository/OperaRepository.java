package it.uniroma3.siw.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.spring.model.Opera;

public interface OperaRepository extends CrudRepository<Opera,Long>{

	public List<Opera> findByTitolo(String titolo);

	@Query("SELECT o FROM Opera o WHERE o.collezione = NULL")
	public List<Opera> findAllSenzaCollezione();

}
