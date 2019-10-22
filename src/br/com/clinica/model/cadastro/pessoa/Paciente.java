
package br.com.clinica.model.cadastro.pessoa;

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

import br.com.clinica.converters.EntityBase;
import br.com.clinica.model.cadastro.outro.Convenio;

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

	@JoinColumn(unique = true, referencedColumnName = "idPessoa")
	@OneToOne(cascade = javax.persistence.CascadeType.ALL)
	private Pessoa pessoa = new Pessoa();

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "paciente_seq")
	private Long idPaciente;
	
	private String email;
	
	private String status = "P"; //Pendente & Finalizado
	
	private String ativo = "A"; //Pendente & Finalizado
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public Long getId() {
		return idPaciente;
	}

	// private String permissaoAcompanhante;

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

	/*
	 * private String path; //Caminho base
	 * 
	 * private String pathToReportPackage; // Caminho para o package onde estão
	 * armazenados os relatorios Jarper
	 */
	/*
	 * public Paciente() { this.path =
	 * this.getClass().getClassLoader().getResource("").getPath();
	 * this.pathToReportPackage = this.path + "relatorio/";
	 * System.out.println(path); }
	 * 
	 * public String getPathToReportPackage() { return this.pathToReportPackage; }
	 * 
	 * public String getPath() { return this.path; }
	 */
	
	public Paciente() {}
	public Paciente(Long cod) {
		this.idPaciente = cod;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
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
