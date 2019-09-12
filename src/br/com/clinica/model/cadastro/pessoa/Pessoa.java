package br.com.clinica.model.cadastro.pessoa;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import br.com.clinica.converters.EntityBase;
import br.com.clinica.model.cadastro.usuario.Login;

/**
 * @author Humberto
 *
 */
@Audited
@Entity
@Table(name = "pessoa")
@SequenceGenerator(name = "pessoa_seq", sequenceName = "pessoa_seq", initialValue = 1, allocationSize = 1)
public class Pessoa implements  EntityBase,Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pessoa_seq")
	private Long idPessoa;

	@Override
	public Long getId() {
		return idPessoa;
	}
	
	@Column(name = "pessoaNome")
	private String pessoaNome;

	@Column(name = "pessoaObservacao")
	private String pessoaObservacao;

	@Column(name = "pessoaSexo")
	private String pessoaSexo;
	
	@Column(name = "pessoaCPF")
	private String pessoaCPF;

	@Column(name = "pessoaRG")
	private String pessoaRG;

	@Temporal(TemporalType.DATE)
	@Column(name = "pessoaDataNascimento")
	private Date pessoaDataNascimento;

	@Column(name = "pessoaEmail")
	private String pessoaEmail;
	
	@Column(name = "pessoaAltura")
	private String pessoaAltura;
	
	@Column(name = "pessoaPeso")
	private String pessoaPeso;
	
	//celular
	@Column(name = "pessoaTelefonePrimario")
	private String pessoaTelefonePrimario;
	//fixo not obrg
	@Column(name = "pessoaTelefoneSecundario")
	private String pessoaTelefoneSecundario;
	//Registro Inicial
	@Column(name = "pessoaDataRegistro")
	private Date pessoaDataRegistro;

	@OneToOne
	@JoinColumn(name="idLogin")
	private Login pessoaUsuario;

	@Column(name ="cep")
	private String cep;
	
	@Column(name="logradouro")
	private String logradouro;
	
	@Column(name="bairro")
	private String bairro;
	
	/*
	 * @Column(name="ibge") private String ibge;
	 */
	
	@Column(name="complemento")
	private String complemento;

	@Column(name="uf")
	private String uf;
	
	@Column(name="localidade")
	private String localidade;
	
	// GETTERS E SETTERS

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getPessoaNome() {
		return pessoaNome;
	}
	
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	
	public String getComplemento() {
		return complemento;
	}
	
	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	/*
	 * public String getIbge() { return ibge; }
	 * 
	 * public void setIbge(String ibge) {
	 
		this.ibge = ibge;
	}*/

	public void setCep(String cep) {
		this.cep = cep;
	}
	
	public String getCep() {
		return cep;
	}
	
	public String getPessoaTelefoneSecundario() {
		return pessoaTelefoneSecundario;
	}

	public void setPessoaTelefoneSecundario(String pessoaTelefoneSecundario) {
		this.pessoaTelefoneSecundario = pessoaTelefoneSecundario.replace(".", "").replace("-", "").replace("/", "")
				.replace("(", "").replace(")", "");
		// this.pessoaTelefoneSecundario = pessoaTelefoneSecundario;
	}

	public String getPessoaTelefonePrimario() {
		return pessoaTelefonePrimario;
	}

	public void setPessoaTelefonePrimario(String pessoaTelefonePrimario) {
		this.pessoaTelefonePrimario = pessoaTelefonePrimario.replace(".", "").replace("-", "").replace("/", "")
				.replace("(", "").replace(")", "");
		// this.pessoaTelefonePrimario = pessoaTelefonePrimario;
	}

	public void setPessoaNome(String pessoaNome) {
		this.pessoaNome = pessoaNome;
	}

	public String getPessoaObservacao() {
		return pessoaObservacao;
	}

	public void setPessoaObservacao(String pessoaObservacao) {
		this.pessoaObservacao = pessoaObservacao;
	}

	public String getPessoaSexo() {
		return pessoaSexo;
	}

	public void setPessoaSexo(String pessoaSexo) {
		this.pessoaSexo = pessoaSexo;
	}

	public String getPessoaCPF() {
		return pessoaCPF;
	}

	public void setPessoaCPF(String pessoaCPF) {
		this.pessoaCPF = pessoaCPF.replace(".", "").replace("-", "").replace("/", "");
		// this.pessoaCPF = pessoaCPF;
	}

	public String getPessoaRG() {
		return pessoaRG;
	}

	public void setPessoaRG(String pessoaRG) {
		this.pessoaRG = pessoaRG.replace(".", "").replace("-", "").replace("/", "");
		// this.pessoaRG = pessoaRG;
	}

	public Date getPessoaDataNascimento() {
		return pessoaDataNascimento;
	}

	public void setPessoaDataNascimento(Date pessoaDataNascimento) {
		this.pessoaDataNascimento = pessoaDataNascimento;
	}

	public String getPessoaEmail() {
		return pessoaEmail;
	}

	public void setPessoaEmail(String pessoaEmail) {
		this.pessoaEmail = pessoaEmail;
	}

	public Long getIdPessoa() {
		return idPessoa;
	}

	public void setIdPessoa(Long idPessoa) {
		this.idPessoa = idPessoa;
	}

	public Date getPessoaDataRegistro() {

		return pessoaDataRegistro = new Date();
	}

	public void setPessoaDataRegistro(Date pessoaDataRegistro) {

		this.pessoaDataRegistro = pessoaDataRegistro;
	}
	
	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}
	
	public String getLocalidade() {
		return localidade;
	}
	
	
	// HASH E EQUALS

	public Login getPessoaUsuario() {
		return pessoaUsuario;
	}

	public void setPessoaUsuario(Login pessoaUsuario) {
		this.pessoaUsuario = pessoaUsuario;
	}

	public String getPessoaAltura() {
		return pessoaAltura;
	}

	public void setPessoaAltura(String pessoaAltura) {
		this.pessoaAltura = pessoaAltura;
	}

	public String getPessoaPeso() {
		return pessoaPeso;
	}

	public void setPessoaPeso(String pessoaPeso) {
		this.pessoaPeso = pessoaPeso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idPessoa == null) ? 0 : idPessoa.hashCode());
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
		Pessoa other = (Pessoa) obj;
		if (idPessoa == null) {
			if (other.idPessoa != null)
				return false;
		} else if (!idPessoa.equals(other.idPessoa))
			return false;
		return true;
	}

}
