package br.com.clinica.model.cadastro.outro;

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
@Entity
@Audited
@Table(name="precoconsulta")
@SequenceGenerator(name="precoconsulta_seq", sequenceName="precoconsulta_seq", initialValue = 1, allocationSize = 1)
public class PrecoConsulta implements  EntityBase,Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,	generator = "precoconsulta_seq")
	private Long idPrecoConsulta;
	
	@Override
	public Long getId() {
		return idPrecoConsulta;
	}
	
	private String categoria;
	
	private String observacao;
	
	private Double valor;
	
	private String ativo = "A";

	public Long getIdPrecoConsulta() {
		return idPrecoConsulta;
	}

	public void setIdPrecoConsulta(Long idPrecoConsulta) {
		this.idPrecoConsulta = idPrecoConsulta;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}
	
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idPrecoConsulta == null) ? 0 : idPrecoConsulta.hashCode());
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
		PrecoConsulta other = (PrecoConsulta) obj;
		if (idPrecoConsulta == null) {
			if (other.idPrecoConsulta != null)
				return false;
		} else if (!idPrecoConsulta.equals(other.idPrecoConsulta))
			return false;
		return true;
	}

}