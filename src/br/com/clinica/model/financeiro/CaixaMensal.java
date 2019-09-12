package br.com.clinica.model.financeiro;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import br.com.clinica.converters.EntityBase;

@Audited
@Entity
@Table(name = "caixamensal")
@SequenceGenerator(name = "caixamensal_seq", sequenceName = "caixamensal_seq", initialValue = 1, allocationSize = 1)

public class CaixaMensal implements EntityBase, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "caixamensal_seq")
	private Long idCaixaMensal;

	@Override
	public Long getId() {
		return idCaixaMensal;
	}

	@Temporal(TemporalType.DATE)
	private Date dataInicioMes;

	@Temporal(TemporalType.DATE)
	private Date dataFinalMes;
	
	private Integer mes;

	private Double valorReceita;

	private Double valorDespesa;

	public Long getIdCaixaMensal() {
		return idCaixaMensal;
	}

	public void setIdCaixaMensal(Long idCaixaMensal) {
		this.idCaixaMensal = idCaixaMensal;
	}

	public Date getDataInicioMes() {
		return dataInicioMes;
	}

	public void setDataInicioMes(Date dataInicioMes) {
		this.dataInicioMes = dataInicioMes;
	}

	public Date getDataFinalMes() {
		return dataFinalMes;
	}

	public void setDataFinalMes(Date dataFinalMes) {
		this.dataFinalMes = dataFinalMes;
	}

	public Double getValorReceita() {
		return valorReceita;
	}

	public void setValorReceita(Double valorReceita) {
		this.valorReceita = valorReceita;
	}

	public Double getValorDespesa() {
		return valorDespesa;
	}

	public void setValorDespesa(Double valorDespesa) {
		this.valorDespesa = valorDespesa;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}
}