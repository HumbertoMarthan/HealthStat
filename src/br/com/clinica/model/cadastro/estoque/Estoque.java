package br.com.clinica.model.cadastro.estoque;

import java.io.Serializable;

import javax.persistence.CascadeType;
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

@Audited
@Entity
@Table(name = "estoque")
@SequenceGenerator(name = "estoque_seq", sequenceName = "estoque_seq", initialValue = 1, allocationSize = 1)
public class Estoque  implements EntityBase,Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,	generator = "estoque_seq")
	private Long idEstoque;
	
	@Override
	public Long getId() {
			return idEstoque;
		}
	
	//private long idMaterial;
	
	@JoinColumn(name ="idMaterial")
	@ManyToOne(cascade = CascadeType.REFRESH)
	Material material;
	
	@JoinColumn(name ="idPedido")
	@ManyToOne(cascade = CascadeType.REFRESH)
	Pedido pedido;
	
	private Integer numPedido;
	
	private String status = "EM"; // Aprovado - Em analise - Reprovado
	
	private Integer quantidade;
	
	private Double valorUnitario;
	
	private Double total;
	
	private String setor;
	
	public String getSetor() {
		return setor;
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}

	public Long getIdEstoque() {
		return idEstoque;
	}

	public Integer getNumPedido() {
		return numPedido;
	}

	public void setNumPedido(Integer numPedido) {
		this.numPedido = numPedido;
	}

	public Double getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(Double valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public void setIdEstoque(Long idEstoque) {
		this.idEstoque = idEstoque;
	}
	
	

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	public Integer getQuantidade() {
		return quantidade;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	//public long getIdMaterial() {
	//	return idMaterial;
	//}

	//public void setIdMaterial(long idMaterial) {
	//	this.idMaterial = idMaterial;
	//}

}