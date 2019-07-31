package br.com.clinica.model.prontuario;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import br.com.clinica.converters.EntityBase;
import br.com.clinica.model.cadastro.pessoa.Paciente;

/**
 * @author Humberto
 *
 */

@Audited
@Entity
@Table(name = "prontuario")
@SequenceGenerator(name = "prontuario_seq", sequenceName = "prontuario_seq", initialValue = 1, allocationSize = 1)
public class Prontuario implements EntityBase, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prontuario_seq")
	private Long idProntuario;
	
	@ManyToOne(cascade = CascadeType.REFRESH)
	@JoinColumn
	private Paciente paciente;
	
	@Column
	private String nomePaciente;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataConsulta;

	private String problemaPaciente;

	/* Exame fisico */ //
	private String exameFisico;

	/* Diagnostico do paciente feito pelo medico */
	@Column(length=2000)
	private String diagnostico;

	/* Prescrição medica */
	@Column(length=2000)
	private String prescricao;

	/*ENCAMINHAMENTO */
	@Column(length=2000)
	private String encaminhamento = "ENCAMINHAMENTO MÉDICO \n\n"+
			"Prezado(a) Dr.(a):  \n" + 
			"Encaminho o(a) paciente [nome completo]  que informa/apresenta " + 
			" informe os seguintes dados subjetivos e objetivos mais relevantes para justificar o encaminhamento.\n \n" + 
			"À disposição para quaisquer esclarecimentos.\r\n" + 
			"Atenciosamente, Dr.(a) ";
	/*ENCAMINHAMENTO */
	
	/*ATESTADO*/
	@Column(length=2000)
	private String atestado = "ATESTADO MÉDICO\n\n" + 
			"\n" + 
			"Atesto para os devidos fins, a pedido, que o(a) Sr(a). (nome), inscrito(a) no CPF sob o nº (informar), paciente sob meus cuidados, foi atendido(a) no dia (data) às (horário), apresentando quadro de (informar) e necessitando de 05 (cinco) dias de repouso.\n" + 
			"\n" + 
			"localidade, dia/mês/ano.\n" + 
			"\n" + 
			"Nome do Médico\n" + 
			"CRM:  " + 
			" (assinatura e carimbo)";

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return idProntuario;
	}

	public Long getIdProntuario() {
		return idProntuario;
	}

	public void setIdProntuario(Long idProntuario) {
		this.idProntuario = idProntuario;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public String getNomePaciente() {
		return nomePaciente;
	}

	public void setNomePaciente(String nomePaciente) {
		this.nomePaciente = nomePaciente;
	}

	public Date getDataConsulta() {
		dataConsulta = new Date();
		return dataConsulta;
	}

	public void setDataConsulta(Date dataConsulta) {
		this.dataConsulta = dataConsulta;
	}

	public String getProblemaPaciente() {
		return problemaPaciente;
	}

	public void setProblemaPaciente(String problemaPaciente) {
		this.problemaPaciente = problemaPaciente;
	}

	public String getExameFisico() {
		return exameFisico;
	}

	public void setExameFisico(String exameFisico) {
		this.exameFisico = exameFisico;
	}

	public String getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}

	public String getPrescricao() {
		return prescricao;
	}

	public void setPrescricao(String prescricao) {
		this.prescricao = prescricao;
	}

	public String getEncaminhamento() {
		return encaminhamento;
	}

	public void setEncaminhamento(String encaminhamento) {
		this.encaminhamento = encaminhamento;
	}

	public String getAtestado() {
		return atestado;
	}

	public void setAtestado(String atestado) {
		this.atestado = atestado;
	}
}