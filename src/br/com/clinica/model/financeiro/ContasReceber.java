package br.com.clinica.model.financeiro;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import br.com.clinica.converters.EntityBase;
import br.com.clinica.model.cadastro.pessoa.Paciente;

/**
 * @author Humberto
 *
 */

@Entity
@Audited
@Table(name = "contasreceber")
@SequenceGenerator(name = "contasreceber_seq", sequenceName = "contasreceber_seq", initialValue = 1, allocationSize = 1)
public class ContasReceber implements EntityBase, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contasreceber_seq")
	private Long idContasReceber;

	@Override
	public Long getId() {
		return idContasReceber;
	}
	
	private Date dataPagamento;

	private BigDecimal valorConsulta;
	
	private String status;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataInicioAgendamento;
	
	@JoinColumn(name="idPaciente")
	@ManyToOne
	private Paciente paciente;

	//----------------------------------------------------
	
	
	
	public Long getIdContasReceber() {
		return idContasReceber;
	}

	public Date getDataInicioAgendamento() {
		return dataInicioAgendamento;
	}

	public void setDataInicioAgendamento(Date dataInicioAgendamento) {
		this.dataInicioAgendamento = dataInicioAgendamento;
	}

	public void setIdContasReceber(Long idContasReceber) {
		this.idContasReceber = idContasReceber;
	}

	public BigDecimal getValorConsulta() {
		return valorConsulta;
	}

	public void setValorConsulta(BigDecimal valorConsulta) {
		this.valorConsulta = valorConsulta;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}