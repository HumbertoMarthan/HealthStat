package br.com.clinica.model.financeiro;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import br.com.clinica.converters.EntityBase;
import br.com.clinica.model.cadastro.estoque.Fornecedor;

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
	
	private BigDecimal valorTotalConta;
	
	//private List<ParcelaPagar> parcelas;

	private Fornecedor credor;
	
	public Long getIdContasPagar() {
		return idContasPagar;
	}

	public void setIdContasPagar(Long idContasPagar) {
		this.idContasPagar = idContasPagar;
	}


	public BigDecimal getValorTotalConta() {
		return valorTotalConta;
	}

	public void setValorTotalConta(BigDecimal valorTotalConta) {
		this.valorTotalConta = valorTotalConta;
	}

	public Fornecedor getCredor() {
		return credor;
	}

	public void setCredor(Fornecedor credor) {
		this.credor = credor;
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