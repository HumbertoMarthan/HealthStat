package br.com.clinica.model.financeiro;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "formapagamento")
@SequenceGenerator(name = "formapagamento_seq", sequenceName = "formapagamento_seq", initialValue = 1, allocationSize = 1)
public class FormaPagamento  implements EntityBase,Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,	generator = "formapagamento_seq")
	private Long idFormaPagamento;
	
	@Override
	public Long getId() {
		return idFormaPagamento;
	}
	
	private String nomePagamento;
	
	private String siglaPagamento;
	
	//GETTERS E SETTERS-------------------------
	
	public Long getIdFormaPagamento() {
		return idFormaPagamento;
	}

	public String getNomePagamento() {
		return nomePagamento;
	}

	public String getSiglaPagamento() {
		return siglaPagamento;
	}

	public void setSiglaPagamento(String siglaPagamento) {
		this.siglaPagamento = siglaPagamento;
	}

	public void setNomePagamento(String nomePagamento) {
		this.nomePagamento = nomePagamento;
	}

	public void setIdFormaPagamento(Long idFormaPagamento) {
		this.idFormaPagamento = idFormaPagamento;
	}
	
	public FormaPagamento() {
		
	}
	
	public FormaPagamento (Long cod) {
		this.idFormaPagamento = cod;
	}
	
}
	
	 

	
	
