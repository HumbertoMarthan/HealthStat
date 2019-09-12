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
@Table(name = "estoquista")
@SequenceGenerator(name = "estoquista_seq", sequenceName = "estoquista_seq", initialValue = 1, allocationSize = 1)
public class Estoquista  implements EntityBase,Serializable {

	private static final long serialVersionUID = 1L;
	
	@JoinColumn(unique=true , referencedColumnName="idPessoa")
	@OneToOne(cascade = CascadeType.ALL)
	private Pessoa pessoa = new Pessoa();
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,	generator = "estoquista_seq")
	private Long idEstoquista;
	
	private String ativo = "A";
	
	@Override
	public Long getId() {
		return idEstoquista;
	}
	
	//GETTERS E SETTERS-------------------------
	public Pessoa getPessoa() { 
		return pessoa; 
	}
	 
	public void setPessoa(Pessoa pessoa) { 
		 this.pessoa = pessoa; 
	}
	
	public void setIdEstoquista(Long idEstoquista) {
		this.idEstoquista = idEstoquista;
	}
	
	public Long getIdEstoquista() {
		return idEstoquista;
	}

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}
	
	// HASH CODE & EQUALS
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idEstoquista == null) ? 0 : idEstoquista.hashCode());
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
		Estoquista other = (Estoquista) obj;
		if (idEstoquista == null) {
			if (other.idEstoquista != null)
				return false;
		} else if (!idEstoquista.equals(other.idEstoquista))
			return false;
		return true;
	}
	

	
	 

	
	
}
