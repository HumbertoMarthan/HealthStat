
package br.com.projeto.model;

import java.io.Serializable;

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
@Table(name = "paciente")
@SequenceGenerator(name = "paciente_seq", sequenceName = "paciente_seq", initialValue = 1, allocationSize = 1)
public class Paciente implements EntityBase, Serializable {

	private static final long serialVersionUID = 1L;

	@IdentificaCampoPesquisa(descricaoCampo = "Paciente", campoConsulta = "pessoa.pessoaNome", principal = 2)
	@JoinColumn(unique = true, referencedColumnName = "idPessoa")
	@OneToOne(cascade = javax.persistence.CascadeType.ALL)
	private Pessoa pessoa = new Pessoa();

	@IdentificaCampoPesquisa(descricaoCampo = "Código", campoConsulta = "idPaciente", principal = 1)
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "paciente_seq")
	private Long idPaciente;
	
	
	@Override
	public Long getId() {
		return idPaciente;
	}
	
	//private String permissaoAcompanhante;
	
	@OneToOne
	@JoinColumn(name = "idConvenio")
	Convenio convenio;

	// GETTERS E SETTERS-------------------------

	public Long getIdPaciente() {
		return idPaciente;
	}

	public void setIdPaciente(Long idPaciente) {
		this.idPaciente = idPaciente;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	// HASH & EQUALS

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idPaciente == null) ? 0 : idPaciente.hashCode());
		return result;
	}

	public Convenio getConvenio() {
		return convenio;
	}

	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Paciente other = (Paciente) obj;
		if (idPaciente == null) {
			if (other.idPaciente != null)
				return false;
		} else if (!idPaciente.equals(other.idPaciente))
			return false;
		return true;
	}

	

}
