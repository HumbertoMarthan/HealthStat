package br.com.clinica.model.cadastro.pessoa;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import br.com.clinica.converters.EntityBase;

/**
 * @author Humberto
 *
 */

@Audited
@Entity
@Table(name = "atendente")
@SequenceGenerator(name = "atendente_seq", sequenceName = "atendente_seq", initialValue = 1, allocationSize = 1)
public class Atendente  implements EntityBase, Serializable {
   
	private static final long serialVersionUID = 1L;
	
	@JoinColumn(unique=true , referencedColumnName="idPessoa")
	@OneToOne(cascade = CascadeType.ALL)
	private Pessoa pessoa = new Pessoa();
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,	generator = "atendente_seq")
	private Long idAtendente;
	
	@Override
	public Long getId() {
		return idAtendente;
	}
	
	//GETTERS E SETTERS-------------------------
	
	public Pessoa getPessoa() { 
		return pessoa; 
	}
	 
	public void setPessoa(Pessoa pessoa) { 
		 this.pessoa = pessoa; 
	}
	
	public Long getIdAtendente() {
		return idAtendente;
	}
	
	public void setIdAtendente(Long idAtendente) {
		this.idAtendente = idAtendente;
	}
	
	// HASH CODE & EQUALS

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idAtendente == null) ? 0 : idAtendente.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Atendente other = (Atendente) obj;
		if (idAtendente == null) {
			if (other.idAtendente != null)
				return false;
		} else if (!idAtendente.equals(other.idAtendente))
			return false;
		return true;
	}
	 
	 

	
	
}
