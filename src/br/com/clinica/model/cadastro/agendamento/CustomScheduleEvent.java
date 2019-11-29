package br.com.clinica.model.cadastro.agendamento;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.primefaces.model.ScheduleEvent;

import br.com.clinica.model.cadastro.pessoa.Atendente;
import br.com.clinica.model.cadastro.pessoa.Medico;
import br.com.clinica.model.cadastro.pessoa.Paciente;

public class CustomScheduleEvent implements ScheduleEvent, Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String title;
	private Date startDate;
	private Date endDate;
	private String styleClass;
	private Object data;
	private String url;
	private String description;
	private boolean allDay;
	private boolean editable;
	private Medico medico;
	private Paciente paciente;
	private Atendente atendente;
	private String confirmarConsulta;
	
	@Temporal(TemporalType.DATE)
	private Date vencimentoContasReceber;

	public CustomScheduleEvent() {
	}

	public CustomScheduleEvent(String title, Date start, Date end, boolean allDay, Object data) {
		this.title = title;
		this.startDate = start;
		this.endDate = end;
		this.allDay = allDay;
		this.data = data;
	}

	public CustomScheduleEvent(String title, Date start, Date end, String styleClass, boolean allDay,
			String description, Object data) {
		this.title = title;
		this.startDate = start;
		this.endDate = end;
		this.styleClass = styleClass;
		this.allDay = allDay;
		this.data = data;
		this.description = description;
	}
	
	public CustomScheduleEvent(String title, String styleClass,
			String description, Object data) {
		this.title = title;
		this.styleClass = styleClass;
		this.data = data;
		this.description = description;
	}

	public CustomScheduleEvent(String title, Date start, Date end, String styleClass, boolean allDay,
			String description, Medico medico, Paciente paciente, Object data) {
		this.title = title;
		this.startDate = start;
		this.endDate = end;
		this.styleClass = styleClass;
		this.allDay = allDay;
		this.data = data;
		this.description = description;
		this.medico = medico;
		this.paciente = paciente;
	}

	public CustomScheduleEvent(String title, Date start, Date end, String styleClass, boolean allDay,
			String description, Medico medico, Paciente paciente, String confirma, Object data) {
		this.title = title;
		this.startDate = start;
		this.endDate = end;
		this.styleClass = styleClass;
		this.allDay = allDay;
		this.data = data;
		this.description = description;
		this.medico = medico;
		this.paciente = paciente;
		this.setConfirmarConsulta(confirma);
	}
	
	public CustomScheduleEvent(String title, Date start, Date end, String styleClass, boolean allDay,
			String description, Medico medico, Paciente paciente, Atendente atendente, String confirma, Object data) {
		this.title = title;
		this.startDate = start;
		this.endDate = end;
		this.styleClass = styleClass;
		this.allDay = allDay;
		this.data = data;
		this.description = description;
		this.medico = medico;
		this.paciente = paciente;
		this.atendente = atendente;
		this.setConfirmarConsulta(confirma);
	}
	
	public CustomScheduleEvent(String title, Date start, Date end, String styleClass, boolean allDay,
			String description, Medico medico, Paciente paciente, Atendente atendente, Date vencimentoContasReceber ,String confirma, Object data) {
		this.title = title;
		this.startDate = start;
		this.endDate = end;
		this.styleClass = styleClass;
		this.allDay = allDay;
		this.data = data;
		this.description = description;
		this.medico = medico;
		this.paciente = paciente;
		this.atendente = atendente;
		this.setConfirmarConsulta(confirma);
	}

	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Atendente getAtendente() {
		return atendente;
	}

	public void setAtendente(Atendente atendente) {
		this.atendente = atendente;
	}

	@Override
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public boolean isAllDay() {
		return allDay;
	}

	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}

	@Override
	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	@Override
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public Medico getMedico() {
		return medico;
	}

	public void setMedico(Medico medico) {
		this.medico = medico;
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		CustomScheduleEvent other = (CustomScheduleEvent) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public String getConfirmarConsulta() {
		return confirmarConsulta;
	}

	public void setConfirmarConsulta(String confirmarConsulta) {
		this.confirmarConsulta = confirmarConsulta;
	}

	public Date getVencimentoContasReceber() {
		return vencimentoContasReceber;
	}

	public void setVencimentoContasReceber(Date vencimentoContasReceber) {
		this.vencimentoContasReceber = vencimentoContasReceber;
	}

	
	
}
