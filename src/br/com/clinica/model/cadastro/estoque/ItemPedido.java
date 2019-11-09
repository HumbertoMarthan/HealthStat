package br.com.clinica.model.cadastro.estoque;

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

/**
 * @author Humberto
 *
 */
@Entity
@Audited
@Table(name = "itempedido")
@SequenceGenerator(name = "itempedido_seq", sequenceName = "itempedido_seq", initialValue = 1, allocationSize = 1)
public class ItemPedido implements EntityBase, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "itempedido_seq")
	private Long idItemPedido;

	@Override
	public Long getId() {
		return idItemPedido;
	}
	
	private Date dataPedido;
	
	private Integer quantidade;
	
	private Integer numPedido;

	@JoinColumn(name = "idMaterial")
	@ManyToOne
	private Material material;

	@JoinColumn(name = "idFornecedor")
	@ManyToOne
	Fornecedor fornecedor;

	private String ativo = "A";

	private Double valorUnitario;

	public ItemPedido() {
	}

	public ItemPedido(Long id) {
		this.idItemPedido = id;
	}

	// GETTERS E SETTERS

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public Long getIdItemPedido() {
		return idItemPedido;
	}

	public void setIdItemPedido(Long idItemPedido) {
		this.idItemPedido = idItemPedido;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public Double getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(Double valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public Integer getNumPedido() {
		return numPedido;
	}

	public void setNumPedido(Integer numPedido) {
		this.numPedido = numPedido;
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

	public Date getDataPedido() {
		return dataPedido;
	}

	public void setDataPedido(Date dataPedido) {
		this.dataPedido = dataPedido;
	}
}