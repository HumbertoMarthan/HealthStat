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
@Table(name = "acompanhante")
@SequenceGenerator(name = "acompanhante_seq", sequenceName = "acompanhante_seq", initialValue = 1, allocationSize = 1)
public class Acompanhante  implements EntityBase, Serializable {
   
	private static final long serialVersionUID = 1L;
	
	@JoinColumn(unique=true , referencedColumnName="idPessoa")
	@OneToOne(cascade = CascadeType.ALL)
	private Pessoa pessoa = new Pessoa();
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,	generator = "acompanhante_seq")
	private Long idAcompanhante;
	
	private String parentescos;
	
	private long idPacienteFK;
	
	public Long getIdAcompanhante() {
		return idAcompanhante;
	}

	public void setIdAcompanhante(Long idAcompanhante) {
		this.idAcompanhante = idAcompanhante;
	}

	@Override
	public Long getId() {
		return idAcompanhante;
	}
	
	//GETTERS E SETTERS-------------------------
	
	public Pessoa getPessoa() { 
		return pessoa; 
	}
	 
	public void setPessoa(Pessoa pessoa) { 
		 this.pessoa = pessoa; 
	}
	
	public Long getIdAtendente() {
		return idAcompanhante;
	}
	
	public void setIdAtendente(Long idAcompanhante) {
		this.idAcompanhante = idAcompanhante;
	}
	
	public String getParentescos() {
		return parentescos;
	}
	
	public void setParentescos(String parentescos) {
		this.parentescos = parentescos;
	}
	
	// HASH CODE & EQUALS

	public long getIdPacienteFK() {
		return idPacienteFK;
	}

	public void setIdPacienteFK(long idPacienteFK) {
		this.idPacienteFK = idPacienteFK;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idAcompanhante == null) ? 0 : idAcompanhante.hashCode());
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
		Acompanhante other = (Acompanhante) obj;
		if (idAcompanhante == null) {
			if (other.idAcompanhante != null)
				return false;
		} else if (!idAcompanhante.equals(other.idAcompanhante))
			return false;
		return true;
	}
	 
	 

	
	
}
