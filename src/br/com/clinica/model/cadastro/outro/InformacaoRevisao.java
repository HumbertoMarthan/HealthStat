package br.com.clinica.model.cadastro.outro;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import br.com.clinica.model.cadastro.usuario.Login;

@Entity
@Table(name = "revinfo")
@RevisionEntity()
public class InformacaoRevisao extends DefaultRevisionEntity implements Serializable {

	
	private static final long serialVersionUID = 1L;
	@ManyToOne
	@ForeignKey(name="login_fk")
	@JoinColumn(nullable=false, name="login")
	private Login login;

	public void setEntidade(Login login) {
		this.login = login;
	}
	
	public Login getEntidade() {
		return login;
	}
}
