package br.com.clinica.model.cadastro.agendamento;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import br.com.clinica.converters.EntityBase;
import br.com.clinica.model.cadastro.pessoa.Medico;
import br.com.clinica.model.cadastro.pessoa.Paciente;

/**
 * @author Humberto
 *
 */

@Audited
@Entity
@Table(name = "evento")
@SequenceGenerator(name = "evento_seq", sequenceName = "evento_seq", initialValue = 1, allocationSize = 1)
public class Evento implements EntityBase, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "evento_seq")
	private Long id;

	@Override
	public Long getId() {
		return id;
	}

	private String titulo;

	private Date dataInicio;
	private Date dataFim;
	private boolean diaInteiro;
	private TipoEvento tipoEvento;
	private String descricao;
	private Date dataInicioRelatorio;
	private Date dataFimRelatorio;
	private String confirmaConsulta;
	/*
	 * @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	 * 
	 * @JoinColumn private Paciente paciente;
	 * 
	 * @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	 * 
	 * @JoinColumn private Medico medico;
	 */
	

	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn
	private Paciente paciente;

	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn
	private Medico medico;

	public Evento() {
		this.tipoEvento = TipoEvento.CONSULTA;
		this.titulo = "";
		this.diaInteiro = false;
	}

	public Evento(Long id, String titulo, Date dataInicio, Date dataFim, boolean diaInteiro, TipoEvento tipoEvento,
			String descricao, Paciente paciente, Medico medico) {

		this.id = id;
		this.titulo = titulo;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.diaInteiro = diaInteiro;
		this.tipoEvento = tipoEvento;
		this.setDescricao(descricao);
		this.paciente = paciente;
		this.medico = medico;
	}

	public Evento(Long id, String titulo, Date dataInicio, Date dataFim, boolean diaInteiro, TipoEvento tipoEvento,
			String descricao, Paciente paciente, Medico medico, String confirma) {

		this.id = id;
		this.titulo = titulo;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.diaInteiro = diaInteiro;
		this.tipoEvento = tipoEvento;
		this.setDescricao(descricao);
		this.paciente = paciente;
		this.medico = medico;
		this.confirmaConsulta = confirma;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		// Calendar calendar = java.util.Calendar.getInstance() ;
		// calendar.set(Calendar.HOUR_OF_DAY, 8) ;
		// calendar.set(Calendar);

		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public boolean isDiaInteiro() {
		return diaInteiro;
	}

	public void setDiaInteiro(boolean diaInteiro) {
		this.diaInteiro = diaInteiro;
	}

	public TipoEvento getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(TipoEvento tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	public Medico getMedico() {
		return medico;
	}

	public void setMedico(Medico medico) {
		this.medico = medico;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 29 * hash + Objects.hashCode(this.id);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Evento other = (Evento) obj;
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		return true;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDataInicioRelatorio() {
		return dataInicioRelatorio;
	}

	public void setDataInicioRelatorio(Date dataInicioRelatorio) {
		this.dataInicioRelatorio = dataInicioRelatorio;
	}

	public Date getDataFimRelatorio() {
		return dataFimRelatorio;
	}

	public void setDataFimRelatorio(Date dataFimRelatorio) {
		this.dataFimRelatorio = dataFimRelatorio;
	}

	public String getConfirmaConsulta() {
		return confirmaConsulta;
	}

	public void setConfirmaConsulta(String confirmaConsulta) {
		this.confirmaConsulta = confirmaConsulta;
	}

}
