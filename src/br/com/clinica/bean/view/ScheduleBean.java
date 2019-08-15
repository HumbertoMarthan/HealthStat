package br.com.clinica.bean.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.ContasReceberController;
import br.com.clinica.controller.geral.EventoController;
import br.com.clinica.controller.geral.PacienteController;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.agendamento.CustomScheduleEvent;
import br.com.clinica.model.cadastro.agendamento.Evento;
import br.com.clinica.model.cadastro.agendamento.TipoEvento;
import br.com.clinica.model.cadastro.pessoa.Paciente;
import br.com.clinica.model.cadastro.usuario.Login;
import br.com.clinica.model.financeiro.ContasReceber;

@ManagedBean(name = "scheduleBean")
@Controller
@ViewScoped
public class ScheduleBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private ScheduleModel model;
	private Evento evento;
	private ContasReceber contasReceber;
	private List<ContasReceber> lstContas;
	private ScheduleEvent event;
	private List<ScheduleEvent> scheduleEvents;
	private Date dataAtual;
	private Calendar dataAtualSchedule;
	private Evento selectedEvento;
	private List<Evento> listaEvento;
	String campoBusca;

	@Autowired
	private EventoController eventoController; /* Injetando */

	@Autowired
	private ContasReceberController contasReceberController; /* Injetando */

	@Autowired
	private PacienteController pacienteController;

	@Autowired
	private ContextoBean contextoBean;

	public ScheduleBean() throws Exception {
		event = new CustomScheduleEvent();
		model = new DefaultScheduleModel();
		setDataAtual(new Date());
		evento = new Evento();
		listaEvento = new ArrayList<Evento>();
		contasReceber = new ContasReceber();
		lstContas = new ArrayList<ContasReceber>();
	}

	/**
	 * Lista agendamentos (eventos)
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Evento> busca() throws Exception {
		listaEvento = eventoController.listarEventos();
		return listaEvento;
	}

	/*
	 * @Override public StreamedContent getArquivoReport() throws Exception {
	 * super.setNomeRelatorioJasper("report_calendario");
	 * super.setNomeRelatorioSaida("report_calendario");
	 * super.setListDataBeanCollectionReport(eventoController.findList(getClassImp()
	 * )); return super.getArquivoReport(); }
	 */
	/**
	 * Report com periodo de agendamentos
	 */

	@PostConstruct
	public void init() throws Exception {

		if (this.model != null) {
			List<Evento> eventos = this.eventoController.listarEventos();
			if (this.scheduleEvents == null) {
				this.scheduleEvents = new ArrayList<ScheduleEvent>();
			}
			for (Evento eventoAtual : eventos) { // lista que popula os eventos e inseri
				ScheduleEvent newEvent = new CustomScheduleEvent(eventoAtual.getTitulo(), eventoAtual.getDataInicio(),
						eventoAtual.getDataFim(), eventoAtual.getTipoEvento().getCss(), eventoAtual.isDiaInteiro(),
						eventoAtual.getDescricao(), eventoAtual.getMedico(), eventoAtual.getPaciente(), eventoAtual);
				if (!this.scheduleEvents.contains(newEvent)) {
					newEvent.setId(eventoAtual.getId().toString());
					this.scheduleEvents.add(newEvent);
					this.model.addEvent(newEvent);
				}
			}
		}
	}

	@Override
	public StreamedContent getArquivoReport() throws Exception {
		super.setNomeRelatorioJasper("report_calendario");
		super.setNomeRelatorioSaida("report_calendario");

		String[] parametros = new String[] { "dataInicio", "dataFim" };
		String hql = "FROM Evento e WHERE e.dataInicio BETWEEN :dataInicio AND :dataFim  "
				+ "AND e.dataFim BETWEEN :dataInicio AND :dataFim)";

		List<Evento> lista = eventoController.findListByQueryDinamica(hql, Arrays.asList(parametros),
				evento.getDataInicioRelatorio(), evento.getDataFimRelatorio());

		super.setListDataBeanCollectionReport(lista);
		return super.getArquivoReport();
	}

	public List<Paciente> completePaciente(String q) throws Exception {
		return pacienteController.findListByQueryDinamica(" from Paciente where pessoa.pessoaNome like '%"
				+ q.toUpperCase() + "%' order by pessoa.pessoaNome ASC");
	}

	/**
	 * Adiciona 30 minutos a data inicial selecionada no agendamento
	 */
	public void atualizaValorDataFim() {
		DateTime dataSelecionadaJoda = new DateTime(evento.getDataInicio().getTime());
		evento.setDataFim(dataSelecionadaJoda.plusMinutes(30).toDate());
	}

	/**
	 * Insere o nome do paciente na descrição do schedule tooltip
	 */
	public void pacienteEtiqueta() {
		if (evento.getPaciente().getPessoa().getPessoaNome() != null) {
			String etiqueta = evento.getPaciente().getPessoa().getPessoaNome();
			evento.setTitulo(etiqueta);

		} else {
			System.out.println("Erro ao gravar paciente");
		}
	}

	/**
	 * Insere o nome do médico na descrição do schedule tooltip
	 */
	public void medicoEtiqueta() {
		if (evento.getPaciente().getPessoa().getPessoaNome() != null
				|| evento.getMedico().getPessoa().getPessoaNome() != null) {
			String tooltip = "Paciente: " + evento.getPaciente().getPessoa().getPessoaNome() + "<br/>" + "Médico: "
					+ evento.getMedico().getPessoa().getPessoaNome();
			evento.setDescricao(tooltip);
		} else {
			System.out.println("Erro ao gravar medico");
		}
	}

	public void buscaRegistro() throws Exception {
		listaEvento = eventoController
				.findListByQueryDinamica(" FROM Evento WHERE upper(titulo) like upper('%" + campoBusca + "%')");
		campoBusca = "";
	}

	/**
	 * Verifica se o Médico contém duas consultas agendadas no mesmo horário
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean validarMedico() throws Exception {
		String[] param = new String[] { "idMedico", "dataInicio", "dataFim" };
		String hql = "FROM Evento e WHERE e.medico.idMedico = "
				+ ":idMedico AND (e.dataInicio BETWEEN :dataInicio AND :dataFim "
				+ "OR e.dataFim BETWEEN :dataInicio AND :dataFim)";

		List<Evento> lista = eventoController.findListByQueryDinamica(hql, Arrays.asList(param),
				evento.getMedico().getIdMedico(), evento.getDataInicio(), evento.getDataFim());

		if (!lista.isEmpty()) {
			if (evento.getId() == null) {
				return false;
			} else {
				for (Evento e : lista) {
					if (evento.getId() != e.getId()) {
						if (e.getDataInicio().after(evento.getDataInicio())
								&& e.getDataInicio().before(evento.getDataFim())) {
							return false;
						} else if (e.getDataFim().before(evento.getDataFim())
								&& e.getDataFim().after(evento.getDataInicio())) {
							return false;
						}
					}
				}
			}
		}

		return true;

	}

	/**
	 * Verifica se o Paciente contém duas consultas agendadas no mesmo horário
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean validarPaciente() throws Exception {
		String[] param = new String[] { "idPaciente", "dataInicio", "dataFim" };
		String hql = "FROM Evento e WHERE e.paciente.idPaciente = "
				+ ":idPaciente AND (e.dataInicio BETWEEN :dataInicio AND :dataFim "
				+ "OR e.dataFim BETWEEN :dataInicio AND :dataFim)";

		List<Evento> lista = eventoController.findListByQueryDinamica(hql, Arrays.asList(param),
				evento.getPaciente().getIdPaciente(), evento.getDataInicio(), evento.getDataFim());

		if (!lista.isEmpty()) {
			if (evento.getId() == null) {
				return false;
			} else {
				for (Evento e : lista) {
					if (evento.getId() != e.getId()) {
						if (e.getDataInicio().after(evento.getDataInicio())
								&& e.getDataInicio().before(evento.getDataFim())) {
							return false;
						} else if (e.getDataFim().before(evento.getDataFim())
								&& e.getDataFim().after(evento.getDataInicio())) {
							return false;
						}
					}
				}
			}
		}

		return true;

	}

	/**
	 * Verifica se data de vencimento do pagamento da consulta é anterior a Data
	 * Atual
	 */
	public void verificaDataVencimentoContasReceber() {
		System.out.println("DATA VENCIMENTO DO AGENDAMENTO>>>>>>>>>>"+evento.getDataVencimentoContasReceber());
		if (evento.getDataVencimentoContasReceber().before(new Date())) {
			contasReceber.setDataVencimentoContasReceber(new Date());
		} else {
			contasReceber.setDataVencimentoContasReceber(evento.getDataVencimentoContasReceber());
		}
	}

	public void convenioUnimed() throws Exception {
		Long usuarioSessaoId = contextoBean.getEntidadeLogada().getIdLogin();
		String usuarioSessaoNome = contextoBean.getEntidadeLogada().getLogin();

		System.out.println("Usuario para ser gravado id :" + usuarioSessaoId + " Nome " + usuarioSessaoNome);
		contasReceber.setLogin(new Login(usuarioSessaoId));
		contasReceber.setPaciente(new Paciente(evento.getPaciente().getIdPaciente()));
		//contasReceber.setDataPagamento(new Date());
		verificaDataVencimentoContasReceber();
		contasReceber.setStatus("C");
		contasReceber.setDataInicioAgendamento(evento.getDataInicio());
		contasReceber.setValorConsulta(200.00D);
		contasReceber.setObservacao("Recolher Assinatura de Paciente!");
		contasReceberController.merge(contasReceber);

	}

	public void convenioPrever() throws Exception {
		Long usuarioSessaoId = contextoBean.getEntidadeLogada().getIdLogin();
		String usuarioSessaoNome = contextoBean.getEntidadeLogada().getLogin();

		System.out.println("Usuario para ser gravado id :" + usuarioSessaoId + " Nome " + usuarioSessaoNome);
		verificaDataVencimentoContasReceber();

		contasReceber.setLogin(new Login(usuarioSessaoId));
		contasReceber.setPaciente(new Paciente(evento.getPaciente().getIdPaciente()));
		//contasReceber.setDataPagamento(new Date());
		verificaDataVencimentoContasReceber();
		contasReceber.setStatus("P");
		contasReceber.setValorConsulta(200.00D);
		contasReceber.setDataInicioAgendamento(evento.getDataInicio());
		contasReceberController.merge(contasReceber);

	}

	public void convenioParticular() throws Exception {
		Long usuarioSessaoId = contextoBean.getEntidadeLogada().getIdLogin();
		String usuarioSessaoNome = contextoBean.getEntidadeLogada().getLogin();

		System.out.println("Usuario para ser gravado id :" + usuarioSessaoId + " Nome " + usuarioSessaoNome);
		verificaDataVencimentoContasReceber();
		contasReceber.setLogin(new Login(usuarioSessaoId));
		contasReceber.setPaciente(new Paciente(evento.getPaciente().getIdPaciente()));
		//contasReceber.setDataPagamento(new Date());
		verificaDataVencimentoContasReceber();
		contasReceber.setStatus("P");
		contasReceber.setValorConsulta(200.00D);
		contasReceber.setDataInicioAgendamento(evento.getDataInicio());
		contasReceberController.merge(contasReceber);

	}

	public void convenioSas() throws Exception {
		Long usuarioSessaoId = contextoBean.getEntidadeLogada().getIdLogin();
		String usuarioSessaoNome = contextoBean.getEntidadeLogada().getLogin();

		System.out.println("Usuario para ser gravado id :" + usuarioSessaoId + " Nome " + usuarioSessaoNome);
		verificaDataVencimentoContasReceber();
		contasReceber.setLogin(new Login(usuarioSessaoId));
		contasReceber.setPaciente(new Paciente(evento.getPaciente().getIdPaciente()));
		verificaDataVencimentoContasReceber();
		//contasReceber.setDataPagamento(new Date());
		contasReceber.setStatus("P");
		contasReceber.setValorConsulta(200.00D);
		contasReceber.setDataInicioAgendamento(evento.getDataInicio());
		contasReceberController.merge(contasReceber);

	}

	public void adicionarContasReceber() throws Exception {
			if (evento.getTipoEvento().getDescricao() == "Confirmar") {
				lstContas = contasReceberController.findListByQueryDinamica("from ContasReceber");
			for (ContasReceber contas : lstContas) {
				if (contas.getPaciente().getIdPaciente() == evento.getPaciente().getIdPaciente()
						&& evento.getDataInicio().compareTo(contas.getDataInicioAgendamento()) == 0) {
					contasReceberController.delete(contas);
				}
			}

			// Verifica se o Convênio é UNIMED
			if (       evento.getPaciente().getConvenio().getNomeConvenio().equals("Unimed")
					|| evento.getPaciente().getConvenio().getNomeConvenio().equals("UNIMED")
					|| evento.getPaciente().getConvenio().getNomeConvenio().equals("unimed")) {

				convenioUnimed();
			}
			// Verifica se o Convênio é PREVER
			if (       evento.getPaciente().getConvenio().getNomeConvenio().equals("Prever")
					|| evento.getPaciente().getConvenio().getNomeConvenio().equals("PREVER")
					|| evento.getPaciente().getConvenio().getNomeConvenio().equals("prever")) {

				convenioPrever();
			}
			// Verifica se o Convênio é SAS
			if (       evento.getPaciente().getConvenio().getNomeConvenio().equals("Sas")
					|| evento.getPaciente().getConvenio().getNomeConvenio().equals("SAS")
					|| evento.getPaciente().getConvenio().getNomeConvenio().equals("sas")) {

				convenioSas();
			}
			// Verifica se o Convênio é PARTICULAR
			if (	   evento.getPaciente().getConvenio().getNomeConvenio().equals("Particular")
					|| evento.getPaciente().getConvenio().getNomeConvenio().equals("PARTICULAR")
					|| evento.getPaciente().getConvenio().getNomeConvenio().equals("particular")) {

				convenioParticular();
			}
		}
	}

	/* Salva */
	public void salvar() throws Exception {
		// Salva o construtor que implementa a interface (Custom) do Schedule com os
		// atributos.
		ScheduleEvent newEvent = new CustomScheduleEvent(this.evento.getTitulo(), this.evento.getDataInicio(),
				this.evento.getDataFim(), this.evento.getTipoEvento().getCss(), this.evento.isDiaInteiro(),
				this.evento.getDescricao(), this.evento.getMedico(), this.evento.getPaciente(),
				this.evento.getConfirmaConsulta(), this.evento);

		/* Verifica se a Datafim está vindo antes da DataInicio */
		if (evento.getDataFim().before(evento.getDataInicio())) {
			addMsg("Data Final do agendamento  não pode ser maior que a Data Inicial do mesmo");
		} else if (validarMedico() && validarPaciente()) { /* Se o Evento for novo */
			if (evento.getId() == null) {
				model.addEvent(newEvent);
				eventoController.persist(evento);
			} else { /* Se o Evento já existir */
				newEvent.setId(event.getId());
				model.updateEvent(newEvent);
				System.out.println("ID DO EDITADO>>>" + evento.getId());
				eventoController.merge(evento);

				// Busca o Evento para comparação no Contas a receber
				Long id = this.evento.getId();
				evento = eventoController.findByPorId(Evento.class, id);

				adicionarContasReceber();

			}
			addMsg(" Agendamento Salvo para: " + evento.getPaciente().getPessoa().getPessoaNome() + "Agendamento para: "
					+ evento.getTitulo());
		} else {
			addMsg("Já existe um agendamento cadastrado neste horário para este paciente ou médico, Revise o calendário!");
		}
	}

	public void remover() throws Exception {
		try {
			/* Deleta evento do agendamento do banco de dados */
			evento = (Evento) eventoController.getSession().get(getClassImp(), evento.getId());
			eventoController.delete(evento);

			/* Deleta etiqueta do Schedule */
			model.deleteEvent(event);

			/* Adiciona Mensagem para o usuario do agendamento removido */
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Agendamento Removido: " + evento.getPaciente().getPessoa().getPessoaNome(),
					"Agendamento Removido :" + evento.getTitulo());
			addMessage(message);
		} catch (Exception e) {
			/* Lança erro ao remover etiqueta */
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Impossivel remover",
					"Há dependencias:" + evento.getTitulo());
			addMessage(message);
		}

	}

	// AO SALVAR SELEÇÃO DE UMA AGENDAMENTO
	public void onDateSelect(SelectEvent selectEvent) {
		this.evento = new Evento();
		Date dataSelecionada = (Date) selectEvent.getObject();
		@SuppressWarnings("unused")
		DateTime dataSelecionadaJoda = new DateTime(dataSelecionada.getTime());
		this.evento.setDataInicio(dataSelecionada);
	}

	// EVENTO DE SELEÇÃO DOS HORARIOS AGENDADOS
	public void onEventSelect(SelectEvent selectEvent) throws Exception {
		System.out.println("Entrou no evento selecionado");
		this.evento = new Evento();
		event = (CustomScheduleEvent) selectEvent.getObject();
		this.evento = (Evento) event.getData();
		Long id = this.evento.getId();
		System.out.println("Evento Selecionado" + id);
		this.evento = eventoController.findByPorId(Evento.class, id);
		System.out.println("Evento vindo do banco" + evento);
	}

	private void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public TipoEvento[] getTiposEventos() {
		return TipoEvento.values();
	}

	// GETTERS E SETTERS

	public void onRowSelect(SelectEvent event) {
		evento = (Evento) event.getObject();
	}

	public ContextoBean getContextoBean() {
		return contextoBean;
	}

	public void setContextoBean(ContextoBean contextoBean) {
		this.contextoBean = contextoBean;
	}

	public ContasReceber getContasReceber() {
		return contasReceber;
	}

	public void setContasReceber(ContasReceber contasReceber) {
		this.contasReceber = contasReceber;
	}

	public List<ContasReceber> getLstContas() {
		return lstContas;
	}

	public void setLstContas(List<ContasReceber> lstContas) {
		this.lstContas = lstContas;
	}

	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(ScheduleEvent event) {
		this.event = event;
	}

	public EventoController getEventoController() {
		return eventoController;
	}

	public void setEventoController(EventoController eventoController) {
		this.eventoController = eventoController;
	}

	public PacienteController getPacienteController() {
		return pacienteController;
	}

	public void setPacienteController(PacienteController pacienteController) {
		this.pacienteController = pacienteController;
	}

	public List<ScheduleEvent> getScheduleEvents() {
		return scheduleEvents;
	}

	public void setScheduleEvents(List<ScheduleEvent> scheduleEvents) {
		this.scheduleEvents = scheduleEvents;
	}

	public String getCampoBusca() {
		return campoBusca;
	}

	public void setCampoBusca(String campoBusca) {
		this.campoBusca = campoBusca;
	}

	@Override
	protected Class<Evento> getClassImp() {
		return Evento.class;
	}

	@Override
	protected InterfaceCrud<Evento> getController() {
		return eventoController;
	}

	public ScheduleModel getModel() {
		return model;
	}

	public void setModel(ScheduleModel model) {
		this.model = model;
	}

	public Evento getEvento() {
		return evento;
	}

	public List<Evento> getListaEvento() {
		return listaEvento;
	}

	public void setListaEvento(List<Evento> listaEvento) {
		this.listaEvento = listaEvento;
	}

	public void setEvento(Evento evento) {
		this.evento = evento;
	}

	public Calendar getDataAtualSchedule() {
		return dataAtualSchedule;
	}

	public void setDataAtualSchedule(Calendar dataAtualSchedule) {
		this.dataAtualSchedule = dataAtualSchedule;
	}

	public Calendar today() {
		return Calendar.getInstance();
	}

	public Evento getSelectedEvento() {
		return selectedEvento;
	}

	public void setSelectedEvento(Evento selectedEvento) {
		this.selectedEvento = selectedEvento;
	}

	public Date getDataAtual() {
		return dataAtual;
	}

	public void setDataAtual(Date dataAtual) {
		LocalDate localDate = new LocalDate();
		dataAtual = localDate.toDate();

		this.dataAtual = dataAtual;
	}

	public ContasReceberController getContasReceberController() {
		return contasReceberController;
	}

	public void setContasReceberController(ContasReceberController contasReceberController) {
		this.contasReceberController = contasReceberController;
	}

}
