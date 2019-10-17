package br.com.clinica.model.financeiro;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import br.com.clinica.converters.EntityBase;
import br.com.clinica.model.cadastro.estoque.Fornecedor;
import br.com.clinica.model.cadastro.estoque.Material;

/**
 * @author Humberto
 *
 */
@Entity
@Audited
@Table(name = "contaspagar")
@SequenceGenerator(name = "contaspagar_seq", sequenceName = "contaspagar_seq", initialValue = 1, allocationSize = 1)
public class ContasPagar implements EntityBase, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contaspagar_seq")
	private Long idContasPagar;

	@Override
	public Long getId() {
		return idContasPagar;
	}
	
	private String status  = "P";
	
	private String tipoConta;
	
	private Double valorConta;
	
	private Double valorTotalConta;
	
	private int quantidadeParcelas;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataPagamento;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataLancamento;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataVencimento;
	
	@ManyToOne
	@JoinColumn(name = "idFornecedor")
	private Fornecedor fornecedor;

	@ManyToOne
	@JoinColumn(name = "idMaterial")
	private Material material = null;
	
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "idFormaPagamento")
	private FormaPagamento formaPagamento;
	
	private Integer quantidade;
	
	private Integer parcelas;
	
	public ContasPagar(Long cod) {
		this.idContasPagar = cod;
	}

	public ContasPagar() {
		
	}
	
	public Long getIdContasPagar() {
		return idContasPagar;
	}

	public void setIdContasPagar(Long idContasPagar) {
		this.idContasPagar = idContasPagar;
	}

	public Double getValorConta() {
		return valorConta;
	}

	public void setValorConta(Double valorConta) {
		this.valorConta = valorConta;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public Date getDataLancamento() {
		return dataLancamento;
	}

	public void setDataLancamento(Date dataLancamento) {
		this.dataLancamento = dataLancamento;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
	
	public String getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(String tipoConta) {
		this.tipoConta = tipoConta;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public int getQuantidadeParcelas() {
		return quantidadeParcelas;
	}

	public void setQuantidadeParcelas(int quantidadeParcelas) {
		this.quantidadeParcelas = quantidadeParcelas;
	}

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public Double getValorTotalConta() {
		return valorTotalConta;
	}

	public void setValorTotalConta(Double valorTotalConta) {
		this.valorTotalConta = valorTotalConta;
	}
	

	public Integer getParcelas() {
		return parcelas;
	}

	public void setParcelas(Integer parcelas) {
		this.parcelas = parcelas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idContasPagar == null) ? 0 : idContasPagar.hashCode());
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
		ContasPagar other = (ContasPagar) obj;
		if (idContasPagar == null) {
			if (other.idContasPagar != null)
				return false;
		} else if (!idContasPagar.equals(other.idContasPagar))
			return false;
		return true;
	}

}