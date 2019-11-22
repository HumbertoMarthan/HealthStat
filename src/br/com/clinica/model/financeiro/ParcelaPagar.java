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

import org.hibernate.envers.Audited;

import br.com.clinica.converters.EntityBase;
import br.com.clinica.model.cadastro.usuario.Login;

@Entity
@Audited
@Table(name = "parcelapagar")
@SequenceGenerator(name = "parcelapagar_seq", sequenceName = "parcelapagar_seq", initialValue = 1, allocationSize = 1)

public class ParcelaPagar implements EntityBase, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parcelapagar_seq")
	private Long idParcela;

	@Override
	public Long getId() {
		return idParcela;
	}

	private int numeroParcela;
	
	@Column(columnDefinition="Decimal(10,2) default '0.0'")
	private Double pagamentoEspecial;

	@ManyToOne
	@JoinColumn(name = "idContasReceber")
	private ContasReceber contasReceber;

	private Date dataVencimento;

	private Date dataPagamento;

	@Column(columnDefinition="Decimal(10,2) default '0.0'")
	private Double valorBruto;

	@Column(columnDefinition="Decimal(10,2) default '0.0'")
	private Double valorDesconto;
	
	@ManyToOne
	@JoinColumn(name = "idLogin")
	Login Login;

	// PENDENTE (P), PAGA(PA)
	private String situacao = "P";

	public ParcelaPagar() {}

	public Long getIdParcela() {
		return idParcela;
	}

	public int getNumeroParcela() {
		return numeroParcela;
	}

	public ParcelaPagar(long cod) {
		this.idParcela = cod;
	}

	public Double getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(Double valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public ContasReceber getContasReceber() {
		return contasReceber;
	}

	public Double getValorBruto() {
		return valorBruto;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setIdParcela(Long idParcela) {
		this.idParcela = idParcela;
	}

	public void setNumeroParcela(int numeroParcela) {
		this.numeroParcela = numeroParcela;
	}

	public void setContasReceber(ContasReceber contasReceber) {
		this.contasReceber = contasReceber;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public void setValorBruto(Double valorBruto) {
		this.valorBruto = valorBruto;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Double getPagamentoEspecial() {
		return pagamentoEspecial;
	}

	public void setPagamentoEspecial(Double pagamentoEspecial) {
		this.pagamentoEspecial = pagamentoEspecial;
	}
	
	public Login getLogin() {
		return Login;
	}

	public void setLogin(Login login) {
		Login = login;
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
