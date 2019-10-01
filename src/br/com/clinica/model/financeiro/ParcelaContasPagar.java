package br.com.clinica.model.financeiro;

import java.io.Serializable;
import java.util.Date;

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

@Entity
@Audited
@Table(name = "parcelacontaspagar")
@SequenceGenerator(name = "parcelacontaspagar_seq", sequenceName = "parcelacontaspagar_seq", initialValue = 1, allocationSize = 1)

public class ParcelaContasPagar implements EntityBase, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parcelacontaspagar_seq")
	private Long idParcela;

	@Override
	public Long getId() {
		return idParcela;
	}

	private int numeroParcela;

	private Double pagamentoEspecial;

	@ManyToOne
	@JoinColumn(name = "idContasPagar")
	private ContasPagar contasPagar;

	private Date dataVencimento;

	private Date dataPagamento;

	private Double valorBruto;

	private Double valorDesconto;

	// PENDENTE (P), PAGA(PA)}
	private String situacao = "P";

	public ParcelaContasPagar(long cod) {
		this.idParcela = cod;
	}

	public ParcelaContasPagar() {}

	public Long getIdParcela() {
		return idParcela;
	}

	public void setIdParcela(Long idParcela) {
		this.idParcela = idParcela;
	}

	public int getNumeroParcela() {
		return numeroParcela;
	}

	public void setNumeroParcela(int numeroParcela) {
		this.numeroParcela = numeroParcela;
	}

	public Double getPagamentoEspecial() {
		return pagamentoEspecial;
	}

	public void setPagamentoEspecial(Double pagamentoEspecial) {
		this.pagamentoEspecial = pagamentoEspecial;
	}

	public ContasPagar getContasPagar() {
		return contasPagar;
	}

	public void setContasPagar(ContasPagar contasPagar) {
		this.contasPagar = contasPagar;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Double getValorBruto() {
		return valorBruto;
	}

	public void setValorBruto(Double valorBruto) {
		this.valorBruto = valorBruto;
	}

	public Double getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(Double valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idParcela == null) ? 0 : idParcela.hashCode());
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
		ParcelaContasPagar other = (ParcelaContasPagar) obj;
		if (idParcela == null) {
			if (other.idParcela != null)
				return false;
		} else if (!idParcela.equals(other.idParcela))
			return false;
		return true;
	}
	
	
}