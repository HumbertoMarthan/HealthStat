package br.com.clinica.bean.view;

import java.io.IOException;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.PacienteController;
import br.com.clinica.controller.geral.ProntuarioController;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.prontuario.Prontuario;

/**
 * @author Humberto
 *
 */
@Controller
@ViewScoped
@ManagedBean(name = "prontuarioBean")
public class ProntuarioBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private Prontuario prontuarioModel;

	private List<Prontuario> listaProntuario;
	
	String estado = "C";
	String campoBusca ="";
	private String url = "/prontuario/prontuarioMedico.jsf?faces-redirect=true";

	@Autowired
	private ProntuarioController prontuarioController; // Injeta o Prontuario Controller

	@Autowired
	private PacienteController pacienteController;


	@Override
	public StreamedContent getArquivoReport() throws Exception {
		super.setNomeRelatorioJasper("report_Prontuario");
		super.setNomeRelatorioSaida("report_Prontuario");
		super.setListDataBeanCollectionReport(prontuarioController.findList(getClassImp()));
		return super.getArquivoReport();
	}
	
	public void mudaEstadoHistorico() {
		setEstado("H");
	}
	public void mudaEstadoAtestado() {
		setEstado("A");
	}
	public void mudaEstadoEncaminhamento() {
		setEstado("E");
	}
	public void mudaEstadoGuiaConsulta() {
		setEstado("C");
	}

	public ProntuarioBean() {
		prontuarioModel = new Prontuario();
		setEstado("C");
	}
	
	// Insere nome do Paciente em Tabela Prontuário
	public void etiquetaNome() {
		if (prontuarioModel.getPaciente().getId() != null) {
			String tooltip = prontuarioModel.getPaciente().getPessoa().getPessoaNome() ;
			prontuarioModel.setNomePaciente(tooltip);
		} else {
			System.out.println("Sem Descricao");
		}
	}

	public void onRowSelect(SelectEvent event) throws IOException {
		
		prontuarioModel = (Prontuario) event.getObject();
}
	
	public void busca() throws Exception {
		StringBuilder str = new StringBuilder();
		str.append("from Prontuario a where 1=1");
		if (!campoBusca.equals("")) {
			str.append(" and upper(a.paciente.pessoa.pessoaNome) like'%" + campoBusca.toUpperCase() + "%'");
		}
		listaProntuario = 
				prontuarioController.findListByQueryDinamica
				(str.toString());
	}
	
    /*Persistência ---------------------------------*/
	@Override
	public String save() throws Exception {
		/* Seta Paciente no Prontuário */
		prontuarioModel = prontuarioController.merge(prontuarioModel);
		prontuarioModel = new Prontuario();

		return "";
	}

	@Override
	public void saveNotReturn() throws Exception {

		prontuarioModel = prontuarioController.merge(prontuarioModel);
		sucesso();
		prontuarioModel = new Prontuario();
	}

	@Override
	public void saveEdit() throws Exception {
		saveNotReturn();
	}

	@Override
	public String novo() throws Exception {
		setarVariaveisNulas();
		return getUrl();
	}

	@Override
	public void setarVariaveisNulas() throws Exception {
		// list.clean();
		prontuarioModel = new Prontuario();
	}

	@Override
	public String editar() throws Exception {
		// list.clean();
		return getUrl();
	}

	@Override
	public void excluir() throws Exception {
		prontuarioModel = (Prontuario) prontuarioController.getSession().get(getClassImp(),
				prontuarioModel.getIdProntuario());
		prontuarioController.delete(prontuarioModel);
		// list.remove(prontuarioModel);
		prontuarioModel = new Prontuario();
		sucesso();
	}

	@Override
	protected Class<Prontuario> getClassImp() {
		return Prontuario.class;
	}

	@Override
	public String redirecionarFindEntidade() throws Exception {
		setarVariaveisNulas();
		return getUrl();
	}

	@Override
	protected InterfaceCrud<Prontuario> getController() {
		return prontuarioController;
	}

	@Override
	public void consultarEntidade() throws Exception {
		prontuarioModel = new Prontuario();
	}

	/*Persistência -------------------------------------------*/
	
	/*ENCAMINHAMENTO*/
	
	
	
	/*ENCAMINHAMENTO*/
	
	
	/*GETTERS E SETTERS*/
	public Prontuario getprontuarioModel() {
		return prontuarioModel;
	}

	public void setprontuarioModel(Prontuario prontuarioModel) {

		this.prontuarioModel = prontuarioModel;
	}

	public String getUrl() {
		return url;
	}

	public Prontuario getProntuarioModel() {
		return prontuarioModel;
	}

	public void setProntuarioModel(Prontuario prontuarioModel) {
		this.prontuarioModel = prontuarioModel;
	}

	public List<Prontuario> getListaProntuario() {
		return listaProntuario;
	}

	public void setListaProntuario(List<Prontuario> listaProntuario) {
		this.listaProntuario = listaProntuario;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public ProntuarioController getProntuarioController() {
		return prontuarioController;
	}

	public void setProntuarioController(ProntuarioController prontuarioController) {
		this.prontuarioController = prontuarioController;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCampoBusca() {
		return campoBusca;
	}

	public void setCampoBusca(String campoBusca) {
		this.campoBusca = campoBusca;
	}

	public PacienteController getPacienteController() {
		return pacienteController;
	}

	public void setPacienteController(PacienteController pacienteController) {
		this.pacienteController = pacienteController;
	}

	
}
