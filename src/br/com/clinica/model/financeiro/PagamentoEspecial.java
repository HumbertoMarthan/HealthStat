package br.com.clinica.model.financeiro;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
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

@Audited
@Entity
@Table(name = "pagamentoespecial")
@SequenceGenerator(name = "pagamentoespecial_seq", sequenceName = "pagamentoespecial_seq", initialValue = 1, allocationSize = 1)

public class PagamentoEspecial implements EntityBase, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pagamentoespecial_seq")
	private Long idPagamentoEspecial;

	@Override
	public Long getId() {
		return idPagamentoEspecial;
	}

	@ManyToOne
	@JoinColumn(name = "idContasReceber")
	private ContasReceber contasReceber;

	@Temporal(TemporalType.DATE)
	private Date dataLancamento;
	
	@Temporal(TemporalType.DATE)
	private Date dataPagamento;

	@Temporal(TemporalType.DATE)
	private Date dataVencimento;
	
	@Column(columnDefinition="Decimal(10,2) default '0.0'")
	private Double valorBruto;

	private String situacao ="P";
	
	@ManyToOne
	@JoinColumn(name = "idFormaPagamento")
	private FormaPagamento formaPagamento;
	
	public PagamentoEspecial() {};
	
	public PagamentoEspecial(Long cod) {this.idPagamentoEspecial = cod;};
	
	public Long getIdPagamentoEspecial() {
		return idPagamentoEspecial;
	}

	public void setIdPagamentoEspecial(Long idPagamentoEspecial) {
		this.idPagamentoEspecial = idPagamentoEspecial;
	}

	public ContasReceber getContasReceber() {
		return contasReceber;
	}

	public void setContasReceber(ContasReceber contasReceber) {
		this.contasReceber = contasReceber;
	}

	public Date getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Double getValorBruto() {
		return valorBruto;
	}

	public void setValorBruto(Double valorBruto) {
		this.valorBruto = valorBruto;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idPagamentoEspecial == null) ? 0 : idPagamentoEspecial.hashCode());
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
		PagamentoEspecial other = (PagamentoEspecial) obj;
		if (idPagamentoEspecial == null) {
			if (other.idPagamentoEspecial != null)
				return false;
		} else if (!idPagamentoEspecial.equals(other.idPagamentoEspecial))
			return false;
		return true;
	}

}