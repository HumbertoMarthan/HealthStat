package br.com.projeto.model;

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

import br.com.projeto.anotacoes.IdentificaCampoPesquisa;
import br.com.projeto.converters.EntityBase;

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
	
	@IdentificaCampoPesquisa(descricaoCampo = "Estoquisa", campoConsulta = "pessoa.pessoaNome", principal = 2)
	@JoinColumn(unique=true , referencedColumnName="idPessoa")
	@OneToOne(cascade = CascadeType.ALL)
	private Pessoa pessoa = new Pessoa();
	
	@IdentificaCampoPesquisa(descricaoCampo = "Código", campoConsulta ="idEstoquista", principal = 1 )
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,	generator = "estoquista_seq")
	private Long idEstoquista;
	
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
