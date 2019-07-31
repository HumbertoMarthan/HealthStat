package br.com.clinica.model.cadastro.usuario;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.primefaces.json.JSONObject;

@Audited
@Entity
public class Login implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	
	private Long idLogin;
	
	//private Long idLogin;
	
	//private String ent_login = null;
	
	private String login = null;
	private String senha = null;
	private boolean inativo = false;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date ultimoAcesso;
	
	public Perfil getPerfil() {
		return perfil;
	}
	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

	@ManyToOne
	@JoinColumn(name ="idPerfil")
	Perfil perfil;
	
	/*
	 * @CollectionOfElements
	 * 
	 * @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
	 * 
	 * @JoinTable(name = "perfil", uniqueConstraints = {@UniqueConstraint(name =
	 * "unique_perfil_key", columnNames = { "idLogin", "tipoAcesso" }) },
	 * joinColumns = { @JoinColumn(name = "idLogin") })
	 * 
	 * @Column(name = "tipoAcesso", length = 20) private Set<String> acessos = new
	 * HashSet<String>();
	 */
	
	
	//GETTERS E SETTERS -----------------------------------------

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idLogin == null) ? 0 : idLogin.hashCode());
		return result;
	}
	public JSONObject getJson() {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("idLogin", idLogin);
		map.put("login", login);
	
		return new JSONObject(map);
	}
	public Long getIdLogin() {
		return idLogin;
	}

	public void setIdLogin(Long idLogin) {
		this.idLogin = idLogin;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Login other = (Login) obj;
		if (idLogin == null) {
			if (other.idLogin != null)
				return false;
		} else if (!idLogin.equals(other.idLogin))
			return false;
		return true;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public boolean isInativo() {
		return inativo;
	}
	public void setInativo(boolean inativo) {
		this.inativo = inativo;
	}

	public Date getUltimoAcesso() {
		return ultimoAcesso;
	}

	public void setUltimoAcesso(Date ultimoAcesso) {
		this.ultimoAcesso = ultimoAcesso;
	}
	
	
}
