package br.com.projeto.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import br.com.projeto.anotacoes.IdentificaCampoPesquisa;
import br.com.projeto.converters.EntityBase;

/**
 * @author Humberto
 *
 */

@Audited
@Entity
@Table(name = "fornecedor")
@SequenceGenerator(name = "fornecedor_seq", sequenceName = "fornecedor_seq", initialValue = 1, allocationSize = 1)
public class Fornecedor  implements EntityBase,Serializable {

	private static final long serialVersionUID = 1L;
	
	@IdentificaCampoPesquisa(descricaoCampo = "C�digo", campoConsulta ="idFornecedor", principal = 1 )
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,	generator = "fornecedor_seq")
	private Long idFornecedor;
	
	@Override
		public Long getId() {
			return idFornecedor;
		}
	
	@Column(name = "nomeFornecedor")
	private String nomeFornecedor;
	
	@Column(name = "cnpj")
	private String cnpj;
	
	@Column(name = "inscricaoEstadual")
	private String inscricaoEstadual;
	
	@Column(name = "razaoSocial")
	private String razaoSocial;
	
	@Column(name = "nomeFantasia")
	private String nomeFantasia;

	@Column(name = "telefone")
	private String telefone;
	
	@Column(name ="cep")
	private String cep;
	
	@Column(name="logradouro")
	private String logradouro;
	
	@Column(name="bairro")
	private String bairro;
	
	@Column(name="complemento")
	private String complemento;

	@Column(name="uf")
	private String uf;
	
	@Column(name="localidade")
	private String localidade;
	
	
	//GETTERS E SETTERS-------------------------
	public Long getIdFornecedor() {
		return idFornecedor;
	}

	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}

	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
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

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getLocalidade() {
		return localidade;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}
	
	
	
}