package it.uniroma3.siw.spring.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames= {"nome","cognome","email"}))
public class Curatore {

	//id sarebbe la matricola
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(nullable = false)
	public String nome;
	
	@Column(nullable = false)
	public String cognome;
	
	@Column
	private String email;
	
	@Column 
	private String numeroTelefono;
	
	@Column 
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private LocalDate dataNascita;
	
	@Column 
	private String luogoNascita;
	
	

	@Column
	@OneToMany(mappedBy = "curatore")
	//@OrderBy("creation time asc")
	public List<Collezione> collezioni;

	
	public Curatore(String name, String last) {
		this.nome = name;
		this.cognome = last;
		this.collezioni = new ArrayList<>();
	}

	public Curatore() {};

	public List<Collezione> getCollezioni() { return collezioni; }

	public void setCollezione(List<Collezione> collezione) { this.collezioni = collezione; }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() { return nome; }

	public void setNome(String firstName) { this.nome = firstName; }

	public String getCognome() { return cognome; }

	public void setCognome(String lastName) { this.cognome = lastName; }

	public String getEmail() { return email; }

	public void setEmail(String email) { this.email = email; }

	public String getNumeroTelefono() { return numeroTelefono; }

	public void setNumeroTelefono(String phoneNumber) { this.numeroTelefono = phoneNumber; }

	public LocalDate getDataNascita() { return dataNascita; }

	public void setDataNascita(LocalDate dateOfBirth) { this.dataNascita = dateOfBirth; }
	public String getLuogoNascita() {
		return luogoNascita;
	}

	public void setLuogoNascita(String luogoNascita) {
		this.luogoNascita = luogoNascita;
	}

	public void setCollezioni(List<Collezione> collezioni) {
		this.collezioni = collezioni;
	}
}
