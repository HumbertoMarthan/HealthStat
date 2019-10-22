package br.com.clinica.model.cadastro.agendamento;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import br.com.clinica.converters.EntityBase;
import br.com.clinica.model.cadastro.pessoa.Paciente;

/**
 * @author Humberto
 *
 */

//@Audited
//@Entity
//@Table(name = "eventopaciente")
//@SequenceGenerator(name = "eventopaciente_seq", sequenceName = "eventopaciente_seq", initialValue = 1, allocationSize = 1)
public class EventoPaciente implements EntityBase, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "eventopaciente_seq")
	private Long idEventoPaciente;
	
	private Long idEvento;

	@Override
	public Long getId() {
		return idEventoPaciente;
	}
	
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name="idPaciente")
	private Paciente paciente;

	public Long getIdEvento() {
		return idEvento;
	}

	public void setIdEvento(Long idEvento) {
		this.idEvento = idEvento;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idEvento == null) ? 0 : idEvento.hashCode());
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
		EventoPaciente other = (EventoPaciente) obj;
		if (idEvento == null) {
			if (other.idEvento != null)
				return false;
		} else if (!idEvento.equals(other.idEvento))
			return false;
		return true;
	}

	public Long getIdEventoPaciente() {
		return idEventoPaciente;
	}

	public void setIdEventoPaciente(Long idEventoPaciente) {
		this.idEventoPaciente = idEventoPaciente;
	}
	
}