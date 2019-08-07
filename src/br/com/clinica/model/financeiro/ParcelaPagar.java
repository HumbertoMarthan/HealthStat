package br.com.clinica.model.financeiro;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.joda.time.LocalDate;
@Entity
@Table(name = "parcelapagar")
public class ParcelaPagar  {
	
	@Id
	private Long idParcela;
	
	private int numero;

	private ContasPagar contaPagar;

	private LocalDate dataVencimento;

	private LocalDate dataPagamento;

	private BigDecimal valorBruto;

	private BigDecimal acrescimoDesconto;

	//ABERTA (A), VENCIDA (V), QUITADA (Q)
	private String situacao = "A";
	
	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public ContasPagar getContaPagar() {
		return contaPagar;
	}

	public void setContaPagar(ContasPagar contaPagar) {
		this.contaPagar = contaPagar;
	}

	public LocalDate getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(LocalDate dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public LocalDate getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(LocalDate dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public BigDecimal getValorBruto() {
		return valorBruto;
	}

	public void setValorBruto(BigDecimal valorBruto) {
		this.valorBruto = valorBruto;
	}

	public BigDecimal getAcrescimoDesconto() {
		return acrescimoDesconto;
	}

	public void setAcrescimoDesconto(BigDecimal acrescimoDesconto) {
		this.acrescimoDesconto = acrescimoDesconto;
	}

	public Long getIdParcela() {
		return idParcela;
	}

	public void setIdParcela(Long idParcela) {
		this.idParcela = idParcela;
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
		ParcelaPagar other = (ParcelaPagar) obj;
		if (idParcela == null) {
			if (other.idParcela != null)
				return false;
		} else if (!idParcela.equals(other.idParcela))
			return false;
		return true;
	}

	
	
}
