package br.com.clinica.model.cadastro.estoque;

import java.io.Serializable;

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
@Table(name = "pedidomaterial")
@SequenceGenerator(name = "pedidomaterial_seq", sequenceName = "pedidomaterial_seq", initialValue = 1, allocationSize = 1)
public class PedidoMaterial  implements EntityBase,Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,	generator = "pedidomaterial_seq")
	private Long idPedidoMaterial; //numero pedido
	
	@Override
	public Long getId() {
			return idPedidoMaterial;
		}
	
	Long idPedido;
	
	Integer numPedido;
	
	Long idMaterial;
	
	Double valorUnitario;
	
	public Long getIdPedido() {
		return idPedido;
	}

	public Double getValorUnitario() {
		return valorUnitario;
	}



	public void setValorUnitario(Double valorUnitario) {
		this.valorUnitario = valorUnitario;
	}



	public void setIdPedido(Long idPedido) {
		this.idPedido = idPedido;
	}

	public Long getIdMaterial() {
		return idMaterial;
	}

	public void setIdMaterial(Long idMaterial) {
		this.idMaterial = idMaterial;
	}

	public PedidoMaterial() {
	}
	
	public PedidoMaterial(long id) {
		this.idPedidoMaterial = id;
	}
	
	public Integer getNumPedido() {
		return numPedido;
	}

	public void setNumPedido(Integer numPedido) {
		this.numPedido = numPedido;
	}

	public Long getIdPedidoMaterial() {
		return idPedidoMaterial;
	}

	public void setIdPedidoMaterial(Long idPedidoMaterial) {
		this.idPedidoMaterial = idPedidoMaterial;
	}
}