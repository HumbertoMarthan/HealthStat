package br.com.projeto.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.envers.Audited;
import org.primefaces.json.JSONObject;

import br.com.projeto.anotacoes.IdentificaCampoPesquisa;

@Audited
@Entity
@Table(name = "mensagem")
@SequenceGenerator(name="mensagem_seq", sequenceName="mensagem_seq" , allocationSize = 1, initialValue = 1)
public class Mensagem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@IdentificaCampoPesquisa(descricaoCampo = "Código", campoConsulta = "men_codigo")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mensagem_seq")
	private Long men_codigo;

	@IdentificaCampoPesquisa(descricaoCampo = "Origem", campoConsulta = "usr_origem.ent_nomefantasia", principal = 2)
	@ManyToOne(fetch = FetchType.EAGER, cascade= {CascadeType.ALL})
	@ForeignKey(name = "usr_origem_fk")
	@JoinColumn(name = "usr_origem")
	private Login usr_origem = new Login();

	@IdentificaCampoPesquisa(descricaoCampo = "Destino", campoConsulta = "usr_origem.ent_nomefantasia", principal = 3)
	@ManyToOne(fetch = FetchType.EAGER, cascade= CascadeType.ALL )
	@ForeignKey(name = "usr_destino_fk")
	@JoinColumn(name = "usr_destino")
	private Login usr_destino = new Login();

	@Column(nullable = false)
	private Boolean men_lido = Boolean.FALSE;

	@IdentificaCampoPesquisa(descricaoCampo = "Data", campoConsulta = "men_datahora")
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date men_datahora = new Date();

	@IdentificaCampoPesquisa(descricaoCampo = "Assunto", campoConsulta = "men_assunto")
	@Column(length = 80, nullable = false)
	private String men_assunto;

	@IdentificaCampoPesquisa(descricaoCampo = "Mensagem", campoConsulta = "men_mensagem", principal = 1)
	@Column(length = 1000, nullable = false)
	private String men_mensagem;

	private Boolean men_exigirresposta = false;

	public Long getMen_codigo() {
		return men_codigo;
	}

	public void setMen_codigo(Long men_codigo) {
		this.men_codigo = men_codigo;
	}

	public Login getUsr_origem() {
		return usr_origem;
	}

	public void setUsr_origem(Login usr_origem) {
		this.usr_origem = usr_origem;
	}

	public Login getUsr_destino() {
		return usr_destino;
	}

	public void setUsr_destino(Login usr_destino) {
		this.usr_destino = usr_destino;
	}

	public Boolean getMen_lido() {
		return men_lido;
	}

	public void setMen_lido(Boolean men_lido) {
		this.men_lido = men_lido;
	}

	public Date getMen_datahora() {
		return men_datahora;
	}

	public void setMen_datahora(Date men_datahora) {
		this.men_datahora = men_datahora;
	}

	public String getMen_assunto() {
		return men_assunto;
	}

	public void setMen_assunto(String men_assunto) {
		this.men_assunto = men_assunto;
	}

	public String getMen_mensagem() {
		return men_mensagem;
	}

	public void setMen_mensagem(String men_mensagem) {
		this.men_mensagem = men_mensagem;
	}

	public Boolean getMen_exigirresposta() {
		return men_exigirresposta;
	}

	public void setMen_exigirresposta(Boolean men_exigirresposta) {
		this.men_exigirresposta = men_exigirresposta;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((men_codigo == null) ? 0 : men_codigo.hashCode());
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
		Mensagem other = (Mensagem) obj;
		if (men_codigo == null) {
			if (other.men_codigo != null)
				return false;
		} else if (!men_codigo.equals(other.men_codigo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Mensagem [men_codigo=" + men_codigo + ", men_assunto="
				+ men_assunto + ", men_mensagem=" + men_mensagem + "]";
	}
	
	
	public JSONObject getJson() {
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("men_codigo", men_codigo);
		map.put("men_lido", men_lido);
		map.put("men_assunto", men_assunto);
		return new JSONObject(map);
	}


}