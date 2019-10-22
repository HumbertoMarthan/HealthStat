package br.com.clinica.model.financeiro;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import br.com.clinica.converters.EntityBase;
import br.com.clinica.model.cadastro.pessoa.Paciente;
import br.com.clinica.model.cadastro.usuario.Login;

/**
 * @author Humberto
 *
 */

@Entity
@Audited
@Table(name = "contasreceber")
@SequenceGenerator(name = "contasreceber_seq", sequenceName = "contasreceber_seq", initialValue = 1, allocationSize = 1)
public class ContasReceber implements EntityBase, Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contasreceber_seq")
	private Long idContasReceber;

	@Override
	public Long getId() {
		return idContasReceber;
	}

	@OneToOne
	@JoinColumn(name = "idLogin")
	private Login login;

	private Date dataPagamento;

	private Double valorConsulta;

	private Double valorComDesconto;

	private Double valorDesconto;
	
	private Double valorEntrada;
	
	private String maisForma = "N";
	
	private String tipoPagamento; //PAR OU ESP //AV - CD - CC
	
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
	@JoinColumn(name = "idFormaPagamento")
	private FormaPagamento formaPagamento;

	private Double valorParcelado;

	private int quantidadeParcelas = 0;

	private String status;

	private String observacao;

	@Temporal(TemporalType.DATE)
	private Date dataVencimentoContasReceber;

	// Somente para comparação
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataInicioAgendamento;

	// Somente para comparação
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataFimAgendamento;

	@JoinColumn(name = "idPaciente")
	@ManyToOne
	private Paciente paciente;

	public ContasReceber(Long idContasReceber) {
		this.idContasReceber = idContasReceber;
	}

	public ContasReceber() {
	}

	// ----------------------------------------------------

	public Long getIdContasReceber() {
		return idContasReceber;
	}

	public Double getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(Double valorDesconto) {
		this.valorDesconto = valorDesconto;
	}

	public FormaPagamento getFormaPagamento() {
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public Double getValorParcelado() {
		return valorParcelado;
	}

	public void setValorParcelado(Double valorParcelado) {
		this.valorParcelado = valorParcelado;
	}
	
	public String getMaisForma() {
		return maisForma;
	}

	public void setMaisForma(String maisForma) {
		this.maisForma = maisForma;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Date getDataInicioAgendamento() {
		return dataInicioAgendamento;
	}

	public Date getDataVencimentoContasReceber() {
		return dataVencimentoContasReceber;
	}

	public void setDataVencimentoContasReceber(Date dataVencimentoContasReceber) {
		this.dataVencimentoContasReceber = dataVencimentoContasReceber;
	}

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}

	public void setDataInicioAgendamento(Date dataInicioAgendamento) {
		this.dataInicioAgendamento = dataInicioAgendamento;
	}

	public void setIdContasReceber(Long idContasReceber) {
		this.idContasReceber = idContasReceber;
	}

	public Double getValorConsulta() {
		return valorConsulta;
	}

	public void setValorConsulta(Double valorConsulta) {
		this.valorConsulta = valorConsulta;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getValorComDesconto() {
		return valorComDesconto;
	}

	public void setValorComDesconto(Double valorComDesconto) {
		this.valorComDesconto = valorComDesconto;
	}

	public int getQuantidadeParcelas() {
		return quantidadeParcelas;
	}

	public void setQuantidadeParcelas(int quantidadeParcelas) {
		this.quantidadeParcelas = quantidadeParcelas;
	}

	public Date getDataFimAgendamento() {
		return dataFimAgendamento;
	}

	public void setDataFimAgendamento(Date dataFimAgendamento) {
		this.dataFimAgendamento = dataFimAgendamento;
	}

	public Double getValorEntrada() {
		return valorEntrada;
	}

	public void setValorEntrada(Double valorEntrada) {
		this.valorEntrada = valorEntrada;
	}

	public String getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(String tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

}