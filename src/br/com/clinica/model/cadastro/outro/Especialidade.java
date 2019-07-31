package br.com.clinica.model.cadastro.outro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import br.com.clinica.converters.EntityBase;
import br.com.clinica.model.cadastro.pessoa.Medico;

/**
 * @author Humberto
 *
 */

@Audited
@Entity
@Table(name = "especialidade")
@SequenceGenerator(name = "especialidade_seq", sequenceName = "especialidade_seq", initialValue = 1, allocationSize = 1)
public class Especialidade  implements EntityBase, Serializable {
   
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,	generator = "especialidade_seq")
	private Long idEspecialidade;
	
	private String nomeEspecialidade;
	
	@ManyToMany(mappedBy = "especialidades")
	private List<Medico> medicos = new ArrayList<>();
	
	@Override
	public Long getId() {
		return idEspecialidade;
	}

	//GETTERS E SETTERS-------------------------
	
	public Long getIdEspecialidade() {
		return idEspecialidade;
	}


	public void setIdEspecialidade(Long idEspecialidade) {
		this.idEspecialidade = idEspecialidade;
	}


	public String getNomeEspecialidade() {
		return nomeEspecialidade;
	}


	public void setNomeEspecialidade(String nomeEspecialidade) {
		this.nomeEspecialidade = nomeEspecialidade;
	}

	@Override
	public String toString() {
		return  nomeEspecialidade;
	}
	
	
	
}
