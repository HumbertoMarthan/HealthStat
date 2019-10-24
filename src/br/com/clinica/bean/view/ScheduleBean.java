package br.com.clinica.bean.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.ContasReceberController;
import br.com.clinica.controller.geral.EventoController;
import br.com.clinica.controller.geral.EventoPacienteController;
import br.com.clinica.controller.geral.MedicoController;
import br.com.clinica.controller.geral.PacienteController;
import br.com.clinica.controller.geral.ProntuarioController;
import br.com.clinica.model.cadastro.agendamento.CustomScheduleEvent;
import br.com.clinica.model.cadastro.agendamento.Evento;
import br.com.clinica.model.cadastro.agendamento.EventoPaciente;
import br.com.clinica.model.cadastro.agendamento.TipoEvento;
import br.com.clinica.model.cadastro.pessoa.Medico;
import br.com.clinica.model.cadastro.pessoa.Paciente;
import br.com.clinica.model.cadastro.usuario.Login;
import br.com.clinica.model.financeiro.ContasReceber;
import br.com.clinica.model.prontuario.Prontuario;
import br.com.clinica.utils.DatasUtils;
import br.com.clinica.utils.EmailUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;

@ManagedBean(name = "scheduleBean")
@Controller
@ViewScoped
public class ScheduleBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;
	
	private ScheduleModel model = new DefaultScheduleModel();
	private Evento evento = new Evento();
	private ContasReceber contasReceber = new ContasReceber();
	private EventoPaciente eventoPacienteModel = new EventoPaciente();
	private Evento selectedEvento;
	
	private ScheduleEvent event = new CustomScheduleEvent();
	private List<ScheduleEvent> scheduleEvents;
	
	private List<ContasReceber> lstContas = new ArrayList<ContasReceber>();
	private ArrayList<Medico> lstPrecos = new ArrayList<Medico>(); 
	private List<Evento> listaEvento = new ArrayList<Evento>();
	
	private Date dataAtual = new Date();
	private Calendar dataAtualSchedule;
	
	String campoBusca = "";
	String campoBuscaNome ="";
	String campoBuscaEspecialidade = ""; 
	String campoBuscaAtivo = "T";

	@Autowired
	private EventoController eventoController; /* Injetando */

	@Autowired
	private ContasReceberController contasReceberController; /* Injetando */

	@Autowired
	private PacienteController pacienteController;
	
	@Autowired
	private MedicoController medicoController;

	@Autowired
	private EventoPacienteController eventoPacienteController;

	@Autowired
	private ProntuarioController  prontuarioController;
	
	@Autowired
	private ContextoBean contextoBean;

	public void buscaPreco(){
		StringBuilder str = new StringBuilder();
		try {
			lstPrecos = new ArrayList<>();
			str.append("from Medico a where 1=1");
			
			if(!campoBuscaNome.equals("")) {
				str.append(" and a.pessoa.pessoaNome LIKE '%"+ campoBuscaNome +"%'");
			}
			
			if(campoBuscaAtivo.equals("A")) {
				str.append(" and a.ativo = 'A'");
			}
			
			if(campoBuscaAtivo.equals("I")) {
				str.append(" and a.ativo = 'I'");
			}
		}catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}

		/*
		 * if(!campoBuscaEspecialidade.equals("")) {
		 * str.append(" and a.especialidades.nomeEspecialidade LIKE '%"+
		 * campoBuscaEspecialidade +"%'"); }
		 */
		
		try {
			lstPrecos = (ArrayList<Medico>) medicoController.findListByQueryDinamica(str.toString());
		} catch (Exception e) {
			System.out.println("Erro ao buscar");
			e.printStackTrace();
		}
	}

	/**
	 * Lista agendamentos (eventos)
	 * 
	 * @return
	 */
	public List<Evento> busca(){
	try {
		listaEvento = eventoController.listarEventos();
	}catch(Exception e) {
		e.printStackTrace();
		e.getMessage();
	}
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
	public void init() {
		buscaEvento();
		
		try {
		buscaPreco();
		}catch (Exception e) {
			System.out.println("Erro no Busca Preço do DashBoard");
			e.printStackTrace();
			e.getMessage();
		}
		try {
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
		}catch (Exception e) {
			System.out.println("Erro ao popular calendário no dashboar");
			e.printStackTrace();
			e.getMessage();
		}
	}

	public List<Paciente> completePaciente(String q)  {
		try {
			return pacienteController.findListByQueryDinamica(" from Paciente where pessoa.pessoaNome like '%"
					+ q.toUpperCase() + "%' order by pessoa.pessoaNome ASC");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Adiciona 30 minutos a data inicial selecionada no agendamento
	 */
	public void atualizaValorDataFim() {
		try {

			DateTime dataSelecionadaJoda = new DateTime(evento.getDataInicio().getTime());
			evento.setDataFim(dataSelecionadaJoda.plusMinutes(30).toDate());
			System.out.println("DATA FIM " + evento.getDataFim());
		} catch (Exception e) {
			System.out.println("Erro ao Adicionar 30 minutos");
		}
	}

	/**
	 * Insere o nome do paciente na descrição do schedule tooltip
	 */
	public void pacienteEtiqueta() {
		try {
			if (evento.getPaciente().getPessoa().getPessoaNome() != null) {
				String etiqueta = evento.getPaciente().getPessoa().getPessoaNome();
				evento.setTitulo(etiqueta);
				System.out.println("Etiqueta Paciente" + etiqueta);
			}
		} catch (Exception e) {

			System.out.println("Erro ao gravar paciente");
		}
	}

	/**
	 * Insere o nome do médico na descrição do schedule tooltip
	 */
	public void medicoEtiqueta() {
		try {
			if (evento.getPaciente().getPessoa().getPessoaNome() != null
					|| evento.getMedico().getPessoa().getPessoaNome() != null) {
				String tooltip = "Paciente: " + evento.getPaciente().getPessoa().getPessoaNome() + "<br/>" + "Médico: "
						+ evento.getMedico().getPessoa().getPessoaNome();
				evento.setDescricao(tooltip);
				System.out.println("Tooltip Médico  paciente" + tooltip);
			}
		} catch (Exception e) {
			System.out.println("Erro ao gravar medico");
		}
	}

	public void buscaRegistro() {
		try {
			listaEvento = eventoController
					.findListByQueryDinamica(" FROM Evento WHERE upper(titulo) like upper('%" + campoBusca + "%')");
			campoBusca = "";
		}catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}

	/**
	 * Verifica se o Médico contém duas consultas agendadas no mesmo horário
	 * 
	 * @return
	 */
	public boolean validarMedico() {
		String[] param = new String[] { "idMedico", "dataInicio", "dataFim" };
		String hql = "FROM Evento e WHERE e.medico.idMedico = "
				+ ":idMedico AND (e.dataInicio BETWEEN :dataInicio AND :dataFim "
				+ "OR e.dataFim BETWEEN :dataInicio AND :dataFim)";

		try {
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
		}catch (Exception e) {
			System.out.println("Erro ao validar Médico");
			e.printStackTrace();
			e.getMessage();
		}

		return true;

	}

	/**
	 * Verifica se o Paciente contém duas consultas agendadas no mesmo horário
	 * 
	 * @return
	 */
	public boolean validarPaciente(){
		String[] param = new String[] { "idPaciente", "dataInicio", "dataFim" };
		String hql = "FROM Evento e WHERE e.paciente.idPaciente = "
				+ ":idPaciente AND (e.dataInicio BETWEEN :dataInicio AND :dataFim "
				+ "OR e.dataFim BETWEEN :dataInicio AND :dataFim)";
		
		try {
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
		}catch (Exception e) {
			System.out.println("Erro ao validar Paciente");
			e.printStackTrace();
			e.getMessage();
		}

		return true;

	}

	public void geraProntuario() {
		Prontuario prontuarioModel = new Prontuario();
		prontuarioModel.setPaciente(new Paciente(evento.getPaciente().getIdPaciente()));
		prontuarioModel.setMedico(new Medico(evento.getMedico().getIdMedico()));
		prontuarioModel.setDataConsulta(evento.getDataInicio());
		try {
			prontuarioController.merge(prontuarioModel);
		} catch (Exception e) {
			System.out.println("Erro ao gerar prontuario na tabela");
			e.printStackTrace();
		}
		
	}


	public void convenioUnimed()  {
		Long usuarioSessaoId = 0L;
		try {
			usuarioSessaoId = contextoBean.getEntidadeLogada().getIdLogin();
		}catch (Exception e) {
			System.out.println("Erro ao recuperar usuario na sessao Unimed");
			e.printStackTrace();
			e.getMessage();
		}
		
		contasReceber.setLogin(new Login(usuarioSessaoId));
		contasReceber.setPaciente(new Paciente(evento.getPaciente().getIdPaciente()));
		contasReceber.setStatus("P");
		contasReceber.setDataInicioAgendamento(evento.getDataInicio());
		contasReceber.setDataFimAgendamento(evento.getDataFim());
		contasReceber.setValorConsulta(evento.getMedico().getPrecoConsulta().getValor());
		contasReceber.setObservacao("Recolher Assinatura de Paciente!");
		
		try {
			contasReceberController.merge(contasReceber);
		}catch (Exception e) {
			System.out.println("Erro ao salvar convenio unimed na contas a pagar");
			e.printStackTrace();
			e.getMessage();
		}
		
		//Gera um Prontuario 
		geraProntuario();

	}

	public void convenioPrever() {
		Long usuarioSessaoId = 0L; 
		
		try {
			 usuarioSessaoId = contextoBean.getEntidadeLogada().getIdLogin();
		}catch (Exception e) {
			System.out.println("Erro ao recuperar usuario no convenio prever na contas a pagar");
			e.printStackTrace();
			e.getMessage();
		}	
		
		try {
			contasReceber.setLogin(new Login(usuarioSessaoId));
		}catch (Exception e) {
			System.out.println("Erro ao salvar convenio unimed na sessao");
			e.printStackTrace();
			e.getMessage();
		}
		
		contasReceber.setPaciente(new Paciente(evento.getPaciente().getIdPaciente()));
		contasReceber.setStatus("P");
		contasReceber.setValorConsulta(evento.getMedico().getPrecoConsulta().getValor());
		contasReceber.setDataInicioAgendamento(evento.getDataInicio());
		contasReceber.setDataFimAgendamento(evento.getDataFim());
		
		try {
			contasReceberController.merge(contasReceber);
		}catch (Exception e) {
			System.out.println("Erro ao salvar convenio prever na contas a pagar");
			e.printStackTrace();
			e.getMessage();
		}
		//Gera um Prontuario 
				geraProntuario();
	}

	public void convenioParticular() {
		Long usuarioSessaoId = 0L;
		try {
			usuarioSessaoId = contextoBean.getEntidadeLogada().getIdLogin();
		}catch (Exception e) {
			System.out.println("Erro ao recuperar usuario na sessaao");
			e.printStackTrace();
			e.getMessage();
		}
		try {
			contasReceber.setLogin(new Login(usuarioSessaoId));
		}catch (Exception e) {
			System.out.println("Erro ao recuperar usuario na sessao");
			e.printStackTrace();
			e.getMessage();
		}
		
		contasReceber.setPaciente(new Paciente(evento.getPaciente().getIdPaciente()));
		contasReceber.setStatus("P");
		contasReceber.setValorConsulta(evento.getMedico().getPrecoConsulta().getValor());
		contasReceber.setDataInicioAgendamento(evento.getDataInicio());
		contasReceber.setDataFimAgendamento(evento.getDataFim());
		
		try{
			contasReceberController.merge(contasReceber);
		}catch (Exception e) {
			System.out.println("Erro ao salvar convenio prever na contas a pagar");
			e.printStackTrace();
			e.getMessage();
		}
		//Gera um Prontuario 
				geraProntuario();
		
	}

	public void convenioSas() {
		Long usuarioSessaoId = 0L;
		
		try {
			usuarioSessaoId = contextoBean.getEntidadeLogada().getIdLogin();
		}catch (Exception e) {
			System.out.println("Erro ao recuperar da sessao");
			e.printStackTrace();
			e.getMessage();
		}
		
		try {
			contasReceber.setLogin(new Login(usuarioSessaoId));
		}catch (Exception e) {
			System.out.println("Erro ao recuperar usuario da sessao");
			e.printStackTrace();
			e.getMessage();
		}
		
		contasReceber.setPaciente(new Paciente(evento.getPaciente().getIdPaciente()));
		contasReceber.setStatus("P");
		contasReceber.setValorConsulta(evento.getMedico().getPrecoConsulta().getValor());
		contasReceber.setDataInicioAgendamento(evento.getDataInicio());
		contasReceber.setDataFimAgendamento(evento.getDataFim());
		
		try {
			contasReceberController.merge(contasReceber);
		}catch (Exception e) {
			System.out.println("Erro ao salvar convenio Sas na contas a pagar");
			e.printStackTrace();
			e.getMessage();
		}
		//Gera um Prontuario 
				geraProntuario();
	}

	public void adicionarContasReceber() {
		if (evento.getTipoEvento().getDescricao() == "Confirmar") {
			try {
				lstContas = contasReceberController.findListByQueryDinamica("from ContasReceber");
			}catch (Exception e) {
				e.getMessage();
				e.printStackTrace();
			}
			for (ContasReceber contas : lstContas) {
				if (contas.getPaciente().getIdPaciente() == evento.getPaciente().getIdPaciente()
						&& evento.getDataInicio().compareTo(contas.getDataInicioAgendamento()) == 0) {
					try {
						contasReceberController.delete(contas);
					}catch (Exception e) {
						e.getMessage();
						e.printStackTrace();
					}
				}
			}

			// Verifica se o Convênio é UNIMED
			if (evento.getPaciente().getConvenio().getNomeConvenio().equals("Unimed")
					|| evento.getPaciente().getConvenio().getNomeConvenio().equals("UNIMED")
					|| evento.getPaciente().getConvenio().getNomeConvenio().equals("unimed")) {

				convenioUnimed();
			}
			// Verifica se o Convênio é PREVER
			if (evento.getPaciente().getConvenio().getNomeConvenio().equals("Prever")
					|| evento.getPaciente().getConvenio().getNomeConvenio().equals("PREVER")
					|| evento.getPaciente().getConvenio().getNomeConvenio().equals("prever")) {

				convenioPrever();
			}
			// Verifica se o Convênio é SAS
			if (evento.getPaciente().getConvenio().getNomeConvenio().equals("Sas")
					|| evento.getPaciente().getConvenio().getNomeConvenio().equals("SAS")
					|| evento.getPaciente().getConvenio().getNomeConvenio().equals("sas")) {

				convenioSas();
			}
			// Verifica se o Convênio é PARTICULAR
			if (evento.getPaciente().getConvenio().getNomeConvenio().equals("Particular")
					|| evento.getPaciente().getConvenio().getNomeConvenio().equals("PARTICULAR")
					|| evento.getPaciente().getConvenio().getNomeConvenio().equals("particular")) {

				convenioParticular();
			}
		}
	}

	/* Salva */
	public void salvar() {
		try {
		// Salva o construtor que implementa a interface (Custom) do Schedule com os
		// atributos.
		ScheduleEvent newEvent = new CustomScheduleEvent(this.evento.getTitulo(), this.evento.getDataInicio(),
				this.evento.getDataFim(), this.evento.getTipoEvento().getCss(), this.evento.isDiaInteiro(),
				this.evento.getDescricao(), this.evento.getMedico(), this.evento.getPaciente(),
				this.evento.getConfirmaConsulta(), this.evento);
		Date dataHoje = new Date();
		System.out.println("DATA><HOJE "+ dataHoje);
		if(evento.getDataInicio().before(dataHoje) || evento.getDataFim().before(dataHoje)) {
			addMsg("O Agendamento tem que ser feito com data/hora superior a de hoje ");
		}else if (evento.getDataFim().before(evento.getDataInicio())) {/* Verifica se a Datafim está vindo antes da DataInicio */
			addMsg("Data Final do agendamento  não pode ser maior que a Data Inicial do mesmo");
			
		} else if (validarMedico() && validarPaciente()) { /* Se o Evento for novo */
			if (evento.getId() == null) {
				model.addEvent(newEvent);
				// persistirEventoPaciente();
				eventoController.persist(evento);

				// Envio de Email para o paciente
				eventoController.findById(Evento.class, evento.getId());
				try {
					sendEmailAgendamento(evento);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else { /* Se o Evento já existir */
				newEvent.setId(event.getId());
				model.updateEvent(newEvent);
				// atualizarEventoPaciente();
				eventoController.merge(evento);

				// Envio de Email para o paciente
				evento = (Evento) eventoController.findById(Evento.class, evento.getId());
				try {
				sendEmailAgendamento(evento);
				} catch(Exception e) {
					e.printStackTrace();
				}
				// Busca o Evento para comparação no Contas a receber
				Long id = this.evento.getId();
				evento = eventoController.findByPorId(Evento.class, id);

				adicionarContasReceber();

			}

			addMsg(" Agendamento Salvo para: " + evento.getPaciente().getPessoa().getPessoaNome() + "Agendamento para: " + evento.getTitulo());
		} else {
			addMsg("Já existe um agendamento cadastrado neste horário para este paciente ou médico, Revise o calendário!");
		}
		}catch (Exception e) {
			System.out.println("Erro Ao Fazer um agendamento");
			e.printStackTrace();
		}
	}

	public void remover(){
		try {
			/* Deleta evento do agendamento do banco de dados */
			evento = (Evento) eventoController.getSession().get(Evento.class, evento.getId());
			eventoController.delete(evento);

			/* Deleta etiqueta do Schedule */
			model.deleteEvent(event);

			/* Adiciona Mensagem para o usuario do agendamento removido */
			addMsg(	"Agendamento Removido: " + evento.getPaciente().getPessoa().getPessoaNome());
		} catch (Exception e) {
			/* Lança erro ao remover etiqueta */
			addMsg("Impossivel remover" +	"Há dependencias:" + evento.getTitulo());
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
	public void onEventSelect(SelectEvent selectEvent)  {
		System.out.println("Entrou no evento selecionado");
		this.evento = new Evento();
		event = (CustomScheduleEvent) selectEvent.getObject();
		this.evento = (Evento) event.getData();
		Long id = this.evento.getId();
		System.out.println("Evento Selecionado" + id);
		try {	
			this.evento = eventoController.findByPorId(Evento.class, id);
		}catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
		System.out.println("Evento vindo do banco" + evento);
	}

	public TipoEvento[] getTiposEventos() {
		return TipoEvento.values();
	}

	public void sendEmailAgendamento(Evento t)  {

		long codigoPaciente = t.getPaciente().getIdPaciente();
		List<Evento> emails = new ArrayList<>();
		
		try {	
			emails = eventoController.findListByQueryDinamica("from Evento where paciente.idPaciente =" + codigoPaciente);
		}catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
		
		String email = "";

		for (Evento e : emails) {
			email = e.getPaciente().getEmail();
		}

		String emailTitulo = "";
		emailTitulo += "Um Evento foi Agendado/Atualizada para o paciente: '"
				+ t.getPaciente().getPessoa().getPessoaNome() + "'";

		String emailConteudo = "";

		emailConteudo += "<b>INFORMAÇÕES DO AGENDAMENTO</b><br />";
		emailConteudo += "<b>NUMERO DO AGENDAMENTO: </b> " + t.getId() + "<br />";
		emailConteudo += "<b>PACIENTE:</b> " + t.getPaciente().getPessoa().getPessoaNome() + "<br />";
		emailConteudo += "<b>MÉDICO:</b> " + t.getMedico().getPessoa().getPessoaNome() + "<br /><br /><br />";
		emailConteudo += "<b>DATA/HORA DO INICIO DO AGENDAMENTO :</b> " + DatasUtils.formatDate(t.getDataInicio())
				+ "<br /><br /><br />";
		emailConteudo += "<b>DATA/HORA DO FIM DO AGENDAMENTO :</b> " + DatasUtils.formatDate(t.getDataFim())
				+ "<br /><br /><br />";
		String tipoAgendamento = "";
		if (t.getTipoEvento().ordinal() == 2) {
			tipoAgendamento = "Consulta Confirmada com sucesso";
		}
		if (t.getTipoEvento().ordinal() == 1) {
			tipoAgendamento = "Retorno Confirmado com sucesso";
		}
		if (t.getTipoEvento().ordinal() == 0) {
			tipoAgendamento = "Consulta Agendada com sucesso";
		}

		emailConteudo += "<b>TIPO DE CONSULTA :</b>" + tipoAgendamento;
		emailConteudo += "<b>EMAIL ENVIADO DE FORMA AUTOMATICA POR CLINICA HEALTSTAT, POR FAVOR NÃO RESPONDA O EMAIL</b><br />";
		// Enviar Email com o dados coletados dentro desse método
		EmailUtils.enviarEmail(email, emailTitulo, emailConteudo, true);
	}
	
	//-------------------------LISTA
	private List<Evento> lstEvento = new ArrayList<>();
	private String campoBuscaPaciente = "";
	private String campoBuscaMedico = "";
	private String campoBuscaTipoAgendamento = "CA";
	private Date campoDataInicio;
	private Date campoDataFim;
	
	public void geraRelatorio(){
		JasperPrint  relatorio =  imprimir(lstEvento, "agendamento.jrxml");
		try {
			JasperPrintManager.printReport(relatorio, true);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
	
	public void buscaEvento() {
		lstEvento = new ArrayList<Evento>();
		StringBuilder str = new StringBuilder();
		str.append("from Evento a where 1=1");

		if (!campoBuscaPaciente.equals("")) {
			str.append(" and upper(a.paciente.pessoa.pessoaNome) like upper('%" + campoBuscaPaciente + "%')");
		}
		if (!campoBuscaMedico.equals("")) {
			str.append(" and upper(a.medico.pessoa.pessoaNome) like upper('%" + campoBuscaMedico + "%')");
		}
		if (campoBuscaTipoAgendamento.equals("CA") ) { //Agendada
			str.append(" and a.tipoEvento = 0 ");
		}
		if (campoBuscaTipoAgendamento.equals("CC") ) { //Agendada Confirmada
			str.append(" and a.tipoEvento = 2 ");
		}
		if (campoBuscaTipoAgendamento.equals("RE") ) { //Retornada
			str.append(" and a.tipoEvento = 1 ");
		}
		
		if (campoDataInicio != null) { 
			String aux = "";
			
			if(campoDataFim != null) { aux =  DatasUtils.formatDateSql(campoDataFim);} else { aux =  DatasUtils.formatDateSql(DatasUtils.addDays(campoDataFim, 2));}
			 str.append(" and a.dataInicio BETWEEN '"+DatasUtils.formatDateSql(campoDataInicio) +" 00:00:00' AND '"+ aux +" 23:59:59'");
		}
		if (campoDataFim != null) {
			String aux = "";
			if(campoDataInicio != null) { aux =  DatasUtils.formatDateSql(campoDataInicio);}// else { aux =  DatasUtils.formatDateSql(campoDataInicio = new Date());}
			str.append(" and a.dataFim BETWEEN '"+ aux   +" 00:00:00'  AND '"+  DatasUtils.formatDateSql(campoDataFim) +" 23:59:59'" );
			System.out.println("Calendário >>>>>>"+str);
		}
		
	
		try {
			lstEvento = eventoController.findListByQueryDinamica(str.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
		  
		  

	// GETTERS E SETTERS
	
	

	public void onRowSelect(SelectEvent event) {
		evento = (Evento) event.getObject();
	}
	
	public List<Evento> getLstEvento() {
		return lstEvento;
	}

	public void setLstEvento(List<Evento> lstEvento) {
		this.lstEvento = lstEvento;
	}

	public String getCampoBuscaPaciente() {
		return campoBuscaPaciente;
	}

	public void setCampoBuscaPaciente(String campoBuscaPaciente) {
		this.campoBuscaPaciente = campoBuscaPaciente;
	}

	public String getCampoBuscaMedico() {
		return campoBuscaMedico;
	}

	public void setCampoBuscaMedico(String campoBuscaMedico) {
		this.campoBuscaMedico = campoBuscaMedico;
	}

	public String getCampoBuscaTipoAgendamento() {
		return campoBuscaTipoAgendamento;
	}

	public void setCampoBuscaTipoAgendamento(String campoBuscaTipoAgendamento) {
		this.campoBuscaTipoAgendamento = campoBuscaTipoAgendamento;
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

	public EventoPacienteController getEventoPacienteController() {
		return eventoPacienteController;
	}

	public void setEventoPacienteController(EventoPacienteController eventoPacienteController) {
		this.eventoPacienteController = eventoPacienteController;
	}

	public EventoPaciente getEventoPacienteModel() {
		return eventoPacienteModel;
	}

	public void setEventoPacienteModel(EventoPaciente eventoPacienteModel) {
		this.eventoPacienteModel = eventoPacienteModel;
	}

	public ArrayList<Medico> getLstPrecos() {
		return lstPrecos;
	}

	public void setLstPrecos(ArrayList<Medico> lstPrecos) {
		this.lstPrecos = lstPrecos;
	}

	public ProntuarioController getProntuarioController() {
		return prontuarioController;
	}

	public void setProntuarioController(ProntuarioController prontuarioController) {
		this.prontuarioController = prontuarioController;
	}

	public MedicoController getMedicoController() {
		return medicoController;
	}

	public void setMedicoController(MedicoController medicoController) {
		this.medicoController = medicoController;
	}

	public String getCampoBuscaNome() {
		return campoBuscaNome;
	}

	public void setCampoBuscaNome(String campoBuscaNome) {
		this.campoBuscaNome = campoBuscaNome;
	}

	public String getCampoBuscaEspecialidade() {
		return campoBuscaEspecialidade;
	}

	public void setCampoBuscaEspecialidade(String campoBuscaEspecialidade) {
		this.campoBuscaEspecialidade = campoBuscaEspecialidade;
	}

	public String getCampoBuscaAtivo() {
		return campoBuscaAtivo;
	}

	public void setCampoBuscaAtivo(String campoBuscaAtivo) {
		this.campoBuscaAtivo = campoBuscaAtivo;
	}

	public Date getCampoDataInicio() {
		return campoDataInicio;
	}

	public void setCampoDataInicio(Date campoDataInicio) {
		this.campoDataInicio = campoDataInicio;
	}

	public Date getCampoDataFim() {
		return campoDataFim;
	}

	public void setCampoDataFim(Date campoDataFim) {
		this.campoDataFim = campoDataFim;
	}
}
