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
import br.com.clinica.controller.geral.EventoController;
import br.com.clinica.controller.geral.PacienteController;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.agendamento.CustomScheduleEvent;
import br.com.clinica.model.cadastro.agendamento.Evento;
import br.com.clinica.model.cadastro.agendamento.TipoEvento;
import br.com.clinica.model.cadastro.pessoa.Paciente;

@ManagedBean(name = "scheduleBean")
@Controller
@ViewScoped
public class ScheduleBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private ScheduleModel model;
	private Evento evento;
	private ScheduleEvent event;
	private List<ScheduleEvent> scheduleEvents;
	private Date dataAtual;
	private Calendar dataAtualSchedule;
	private Evento selectedEvento;

	String campoBusca;
	private List<Evento> listaEvento;

	@Autowired
	private EventoController eventoController; /* Injetando */
	
	@Autowired
	private PacienteController pacienteController;
	
	public List<Paciente>  completePaciente(String q) throws Exception {
		return pacienteController.
				findListByQueryDinamica(" from Paciente where pessoa.pessoaNome like '%" + q.toUpperCase()  + "%' order by pessoa.pessoaNome ASC");
	}
	
	public ScheduleBean() throws Exception {
		event = new CustomScheduleEvent();
		model = new DefaultScheduleModel();
		setDataAtual(new Date());
		evento = new Evento();
		listaEvento = new ArrayList<Evento>();
		//busca();
	
	}
	//Busca lista de Agendamento
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

	/* Atualiza valor do Campo DataFim Com a Data Inicial + 30 Minutos */
	public void atualizaValorDataFim() {
		DateTime dataSelecionadaJoda = new DateTime(evento.getDataInicio().getTime());
		evento.setDataFim(dataSelecionadaJoda.plusMinutes(30).toDate());
	}

	/* Seta os valores dos Combo para etiqueta e tooltip */
	public void pacienteEtiqueta() {
		if (evento.getPaciente().getPessoa().getPessoaNome() != null) {
			String etiqueta = evento.getPaciente().getPessoa().getPessoaNome();
			evento.setTitulo(etiqueta);

		} else {
			System.out.println("Erro ao gravar paciente");
		}
	}
	public void medicoEtiqueta() {
		if (evento.getPaciente().getPessoa().getPessoaNome() != null
				|| evento.getMedico().getPessoa().getPessoaNome() != null) {
			String tooltip =
					"Paciente: " + evento.getPaciente().getPessoa().getPessoaNome() 
					+ "<br/>" 	 + 
					"Médico: "	 + evento.getMedico().getPessoa().getPessoaNome();
			evento.setDescricao(tooltip);
		} else {
			System.out.println("Erro ao gravar medico");
		}
	}

	public void onRowSelect(SelectEvent event) {
		evento = (Evento) event.getObject();
	}

	public void buscaRegistro() throws Exception {
		listaEvento =  eventoController.findListByQueryDinamica
				(" FROM Evento WHERE upper(titulo) like upper('%"+campoBusca+"%')");
		campoBusca="";
	}

	/* Verifica se há agendamentos */
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
	
	/* Verifica se há agendamentos */
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
	

	/* Salva */
	public void salvar() throws Exception {
		// Salva o construtor que implementa a interface (Custom) do Schedule com os
		// atributos.
		ScheduleEvent newEvent = new CustomScheduleEvent(this.evento.getTitulo(), this.evento.getDataInicio(),
				this.evento.getDataFim(), this.evento.getTipoEvento().getCss(), this.evento.isDiaInteiro(),
				this.evento.getDescricao(), this.evento.getMedico(), this.evento.getPaciente(),
				this.evento.getConfirmaConsulta(), this.evento);

		/* Verifica se a Datafim está vindo antes da DataInicio */
		if (evento.getDataFim().before(evento.getDataInicio())){
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Data Final não pode ser maior que a Data Inicial", "");
			addMessage(message);

		} else if (validarMedico() && validarPaciente()) {
			/* Se o Evento for novo */
			if (evento.getId() == null) {
				model.addEvent(newEvent);
				eventoController.persist(evento);
				/* Se o Evento já existir */
			} else {
				
				newEvent.setId(event.getId());
				// informacoesEtiqueta();
				model.updateEvent(newEvent);
				eventoController.merge(evento);
			}
			
			  FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
			  "Agendamento Salvo para: " +
			  evento.getPaciente().getPessoa().getPessoaNome(), "Agendamento para: " +
			  evento.getTitulo()); addMessage(message);
			 
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Já existe um agendamento cadastrado neste horário para este paciente ou médico", "Revise o calendário");
			addMessage(message);
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
		System.out.println("Evento vindo do banco" +evento);
	}

	private void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public TipoEvento[] getTiposEventos() {
		return TipoEvento.values();
	}

	// GETTERS E SETTERS

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

	@Override
	public String condicaoAndParaPesquisa() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
