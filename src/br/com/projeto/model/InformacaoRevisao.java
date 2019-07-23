package br.com.projeto.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

@Entity
@Table(name = "revinfo")
@RevisionEntity()
public class InformacaoRevisao extends DefaultRevisionEntity implements Serializable {

	
	private static final long serialVersionUID = 1L;
	@ManyToOne
	@ForeignKey(name="entidade_fk")
	@JoinColumn(nullable=false, name="entidade")
	private Login login;

	public void setEntidade(Login entidade) {
		this.login = entidade;
	}
	
	public Login getEntidade() {
		return login;
	}
}
