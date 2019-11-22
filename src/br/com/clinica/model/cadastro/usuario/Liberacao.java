package br.com.clinica.model.cadastro.usuario;

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

@Audited
@Entity

@Table(name="liberacao")
@SequenceGenerator(name="liberacao_seq", sequenceName="liberacao_seq", initialValue = 1, allocationSize = 1)
public class Liberacao implements Serializable, EntityBase {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long idLiberacao;

	@ManyToOne
	@JoinColumn(name = "idLogin" )
	private Login login;
	
	@ManyToOne
	@JoinColumn(name = "idPerfil" )
	private Perfil perfil;

	public Liberacao() {}
	
	@Override
	public Long getId() {
		return idLiberacao;
	}

	public Long getIdLiberacao() {
		return idLiberacao;
	}

	public void setIdLiberacao(Long idLiberacao) {
		this.idLiberacao = idLiberacao;
	}

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	public Perfil getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idLiberacao == null) ? 0 : idLiberacao.hashCode());
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
		Liberacao other = (Liberacao) obj;
		if (idLiberacao == null) {
			if (other.idLiberacao != null)
				return false;
		} else if (!idLiberacao.equals(other.idLiberacao))
			return false;
		return true;
	}
}