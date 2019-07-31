package br.com.clinica.model.cadastro.outro;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import br.com.clinica.converters.EntityBase;

/**
 * @author Humberto
 *
 */
@Entity
@Audited
@Table(name="doenca")
@SequenceGenerator(name="doenca_seq", sequenceName="doenca_seq", initialValue = 1, allocationSize = 1)
public class Doenca implements EntityBase,Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,	generator = "doenca_seq")
	private Long idDoenca;
	
	@Override
		public Long getId() {
			return idDoenca;
		}

	private String nomeDoenca;
	
	private String cid;
	

	//GETTERS E SETTERS
	
	public Long getIdDoenca() {
		return idDoenca;
	}

	public void setIdDoenca(Long idDoenca) {
		this.idDoenca = idDoenca;
	}

	public String getNomeDoenca() {
		return nomeDoenca;
	}

	public void setNomeDoenca(String nomeDoenca) {
		this.nomeDoenca = nomeDoenca;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}


	//EQUALS E HASCODE
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idDoenca == null) ? 0 : idDoenca.hashCode());
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
		Doenca other = (Doenca) obj;
		if (idDoenca == null) {
			if (other.idDoenca != null)
				return false;
		} else if (!idDoenca.equals(other.idDoenca))
			return false;
		return true;
	}
	
}
