package br.com.clinica.model.cadastro.pessoa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import br.com.clinica.converters.EntityBase;
import br.com.clinica.model.cadastro.outro.Especialidade;

/**
 * @author Humberto
 *
 */
@Audited
@Entity
@Table(name = "medico")
@SequenceGenerator(name = "medico_seq", sequenceName = "medico_seq", initialValue = 1, allocationSize = 1)
public class Medico  implements EntityBase ,Serializable {

	private static final long serialVersionUID = 1L;
	
	@JoinColumn(unique=true , referencedColumnName="idPessoa")
	@OneToOne(cascade = CascadeType.ALL)
	private Pessoa pessoa = new Pessoa();
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,	generator = "medico_seq")
	private Long idMedico;
	
	@Override
	public Long getId() {
			return idMedico;
		}
	
	@Column(name="numeroCrm")
	private String numeroCrm;
	
	@Column(name="valorConsulta")
	private Double valorConsulta;
	
	@Temporal(TemporalType.DATE)
	private Date dataInscricaoCrm;
	
	private String ativo = "A";
	
	@ManyToMany(cascade = CascadeType.MERGE,  fetch = FetchType.EAGER)
	@JoinTable(name="medico_especialidade",
	joinColumns={
			@JoinColumn(name="medicoId", referencedColumnName="idMedico")},
	inverseJoinColumns={
			@JoinColumn(name="especialidadeId",	referencedColumnName="idEspecialidade")}) 
	private List<Especialidade>  especialidades = new ArrayList<>();
	
	public Medico(Long idMedico) {
		this.idMedico = idMedico;
	}

	public Medico() {
	}

	
	//GETTERS E SETTERS-------------------------
	public Long getIdMedico() {
		return idMedico;
	}
	

	public List<Especialidade> getEspecialidades() {
		return especialidades;
	}


	public void setEspecialidades(List<Especialidade> especialidades) {
		this.especialidades = especialidades;
	}


	public void setIdMedico(Long idMedico) {
		this.idMedico = idMedico;
	}
	
	public Date getDataInscricaoCrm() {
		return dataInscricaoCrm;
	}

	public void setDataInscricaoCrm(Date dataInscricaoCrm) {
		this.dataInscricaoCrm = dataInscricaoCrm;
	}

	public Pessoa getPessoa() { 
		return pessoa; 
	}
	 
	public void setPessoa(Pessoa pessoa) { 
		 this.pessoa = pessoa; 
	}
	
	public String getNumeroCrm() {
		return numeroCrm;
	}
	
	public void setNumeroCrm(String numeroCrm) {
		this.numeroCrm = numeroCrm;
	}
	
	public String getAtivo() {
		return ativo;
	}


	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}


	public Double getValorConsulta() {
		return valorConsulta;
	}

	public void setValorConsulta(Double valorConsulta) {
		this.valorConsulta = valorConsulta;
	}

	// HASH CODE & EQUALS
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idMedico == null) ? 0 : idMedico.hashCode());
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
		Medico other = (Medico) obj;
		if (idMedico == null) {
			if (other.idMedico != null)
				return false;
		} else if (!idMedico.equals(other.idMedico))
			return false;
		return true;
	}

	@Override
	public String toString() {
		System.out.println("><><><><><><><><><><><><><><><>< "+especialidades);
		return  especialidades + ",";
	}
	

	
}
