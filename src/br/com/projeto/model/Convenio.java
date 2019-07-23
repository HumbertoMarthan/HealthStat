package br.com.projeto.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import br.com.projeto.anotacoes.IdentificaCampoPesquisa;
import br.com.projeto.converters.EntityBase;

/**
 * @author Humberto
 *
 */
@Entity
@Audited
@Table(name="convenio")
@SequenceGenerator(name="convenio_seq", sequenceName="convenio_seq", initialValue = 1, allocationSize = 1)
public class Convenio implements  EntityBase,Serializable {

	private static final long serialVersionUID = 1L;
	
	@IdentificaCampoPesquisa(descricaoCampo = "C�digo", campoConsulta ="idConvenio", principal = 1 )
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,	generator = "convenio_seq")
	private Long idConvenio;
	
	@Override
		public Long getId() {
			return idConvenio;
		}
	
	@Column
	private String nomeConvenio;
	
	
	private String logradouro;
	
	private String telefone;
	
	
	//GETTERS E SETTERS----------------------------------------------
	
	public Long getIdConvenio() {
		return idConvenio;
	}
	public void setIdConvenio(Long idConvenio) {
		this.idConvenio = idConvenio;
	}
	public String getNomeConvenio() {
		return nomeConvenio;
	}
	public void setNomeConvenio(String nomeConvenio) {
		this.nomeConvenio = nomeConvenio;
	}
	
	//EQUALS E HASCODE ----------------------------------------
	
	
	public String getLogradouro() {
		return logradouro;
	}
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idConvenio == null) ? 0 : idConvenio.hashCode());
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
		Convenio other = (Convenio) obj;
		if (idConvenio == null) {
			if (other.idConvenio != null)
				return false;
		} else if (!idConvenio.equals(other.idConvenio))
			return false;
		return true;
	}
	
	
	
	
}	