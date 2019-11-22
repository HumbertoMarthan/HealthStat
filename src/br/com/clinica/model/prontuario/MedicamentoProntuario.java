package br.com.clinica.model.prontuario;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "medicamentoprontuario")
@SequenceGenerator(name = "medicamentoprontuario_seq", sequenceName = "medicamentoprontuario_seq", initialValue = 1, allocationSize = 1)
public class MedicamentoProntuario implements EntityBase, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medicamentoprontuario_seq")
	private Long idMedicamentoProntuario;
	
	@ManyToOne
	@JoinColumn(name="idMedicamento")
	private Medicamento medicamento;
	
	@ManyToOne
	@JoinColumn(name="idProntuario")
	private Prontuario prontuario;

	@Override
	public Long getId() {
		return idMedicamentoProntuario;
	}

	public Long getIdMedicamentoProntuario() {
		return idMedicamentoProntuario;
	}

	public void setIdMedicamentoProntuario(Long idMedicamentoProntuario) {
		this.idMedicamentoProntuario = idMedicamentoProntuario;
	}

	public Medicamento getMedicamento() {
		return medicamento;
	}

	public void setMedicamento(Medicamento medicamento) {
		this.medicamento = medicamento;
	}

	public Prontuario getProntuario() {
		return prontuario;
	}

	public void setProntuario(Prontuario prontuario) {
		this.prontuario = prontuario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idMedicamentoProntuario == null) ? 0 : idMedicamentoProntuario.hashCode());
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
		MedicamentoProntuario other = (MedicamentoProntuario) obj;
		if (idMedicamentoProntuario == null) {
			if (other.idMedicamentoProntuario != null)
				return false;
		} else if (!idMedicamentoProntuario.equals(other.idMedicamentoProntuario))
			return false;
		return true;
	}
}	
