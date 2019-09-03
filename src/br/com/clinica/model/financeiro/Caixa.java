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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import br.com.clinica.converters.EntityBase;
import br.com.clinica.model.cadastro.estoque.Fornecedor;
import br.com.clinica.model.cadastro.pessoa.Paciente;

@Audited
@Entity
@Table(name = "caixa")
@SequenceGenerator(name = "caixa_seq", sequenceName = "caixa_seq", initialValue = 1, allocationSize = 1)

public class Caixa implements EntityBase, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "caixa_seq")
	private Long idCaixa;

	@Override
	public Long getId() {
		return idCaixa;
	}

	private String tipo = "";

	@ManyToOne
	@JoinColumn(name = "idPaciente")
	Paciente paciente;

	@ManyToOne
	@JoinColumn(name = "idFornecedor")
	private Fornecedor fornecedor;

	@ManyToOne
	@JoinColumn(name = "idContasReceber")
	private ContasReceber contasReceber;

	@ManyToOne
	@JoinColumn(name = "idContasPagar")
	private ContasPagar contasPagar;

	@ManyToOne
	@JoinColumn(name = "idParcela")
	private ParcelaPagar parcelaPagar;

	@Temporal(TemporalType.DATE)
	private Date dataLancamento;

	private Double valorRetirado;

	private Double valorInserido;

	private Double valorTotal;

	public Long getIdCaixa() {
		return idCaixa;
	}

	public void setIdCaixa(Long idCaixa) {
		this.idCaixa = idCaixa;
	}

	public ContasReceber getContasReceber() {
		return contasReceber;
	}

	public void setContasReceber(ContasReceber contasReceber) {
		this.contasReceber = contasReceber;
	}

	public ContasPagar getContasPagar() {
		return contasPagar;
	}

	public void setContasPagar(ContasPagar contasPagar) {
		this.contasPagar = contasPagar;
	}

	public Date getDataLancamento() {
		return dataLancamento;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public Double getValorRetirado() {
		return valorRetirado;
	}

	public void setValorRetirado(Double valorRetirado) {
		this.valorRetirado = valorRetirado;
	}

	public Double getValorInserido() {
		return valorInserido;
	}

	public void setValorInserido(Double valorInserido) {
		this.valorInserido = valorInserido;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public ParcelaPagar getParcelaPagar() {
		return parcelaPagar;
	}

	public void setParcelaPagar(ParcelaPagar parcelaPagar) {
		this.parcelaPagar = parcelaPagar;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idCaixa == null) ? 0 : idCaixa.hashCode());
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
		Caixa other = (Caixa) obj;
		if (idCaixa == null) {
			if (other.idCaixa != null)
				return false;
		} else if (!idCaixa.equals(other.idCaixa))
			return false;
		return true;
	}
}