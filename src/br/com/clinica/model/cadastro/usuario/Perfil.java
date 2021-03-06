package br.com.clinica.model.cadastro.usuario;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import br.com.clinica.converters.EntityBase;

@Audited
@Entity

@Table(name="perfil")
@SequenceGenerator(name="perfil_seq", sequenceName="perfil_seq", initialValue = 1, allocationSize = 1)
public class Perfil implements Serializable, EntityBase {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long idPerfil;

	private String perfilNome;
	
	private String perfilSigla;

	private String categoria;
	
	public Perfil() {}
	
	public Perfil(Long idPerfil) {
		this.idPerfil = idPerfil;
	}

	public Perfil(String perfilNome, String perfilSigla) {
		this.perfilNome = perfilNome;
		this.perfilSigla = perfilSigla;
	}
	
	//GETTERS E SETTERS -----------------------------------------
	public Long getIdPerfil() {
		return idPerfil;
	}
	public void setIdPerfil(Long idPerfil) {
		this.idPerfil = idPerfil;
	}
	public String getPerfilNome() {
		return perfilNome;
	}
	public void setPerfilNome(String perfilNome) {
		this.perfilNome = perfilNome;
	}
	public String getPerfilSigla() {
		return perfilSigla;
	}
	public void setPerfilSigla(String perfilSigla) {
		this.perfilSigla = perfilSigla;
	}

	@Override
	public Long getId() {
		return idPerfil;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	
}
