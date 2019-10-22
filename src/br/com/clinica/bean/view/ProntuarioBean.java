package br.com.clinica.bean.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.hibernate.HibernateException;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.PacienteController;
import br.com.clinica.controller.geral.ProntuarioController;
import br.com.clinica.model.cadastro.pessoa.Paciente;
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

	private Prontuario prontuarioModel = new Prontuario();
	private List<Prontuario> lstDadosPaciente = new ArrayList<>();
	private List<Prontuario> listaProntuario = new ArrayList<>();

	private String url = "/prontuario/listaProntuario.jsf?faces-redirect=true";
	private String urlGuia = "/prontuario/prontuarioMedico.jsf?faces-redirect=true";
	
	String campoBuscaAtivo = "T";
	String estado = "C";
	String campoBusca = "";

	@Autowired
	private ProntuarioController prontuarioController; // Injeta o Prontuario Controller

	@Autowired
	private PacienteController pacienteController;

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

	@PostConstruct
	public void init() {
		setEstado("C");
		busca();
	}

	public List<Paciente> completePaciente(String q)  {
		try {
			return pacienteController.findListByQueryDinamica(
					" from Paciente p where EXISTS( from Evento e where e.paciente.idPaciente = p.idPaciente) and p.status = 'P' and pessoa.pessoaNome like '%"
							+ q.toUpperCase() + "%' order by pessoa.pessoaNome ASC");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Insere nome do Paciente em Tabela Prontuário
	public void etiquetaNome() {
		if (prontuarioModel.getPaciente().getId() != null) {
			String tooltip = prontuarioModel.getPaciente().getPessoa().getPessoaNome();
			prontuarioModel.setNomePaciente(tooltip);
		} else {
			System.out.println("Sem Descricao");
		}
	}

	public void onRowSelect(SelectEvent event) throws IOException {
		prontuarioModel = (Prontuario) event.getObject();
	}

	public void busca() {
		try {
			listaProntuario = new ArrayList<Prontuario>();
			StringBuilder str = new StringBuilder();
			str.append("from Prontuario a where 1=1");
			if (!campoBusca.equals("")) {
				str.append(" and (upper(a.paciente.pessoa.pessoaNome) like upper('%" + campoBusca.toUpperCase() + "%'))");
			}
			if (campoBuscaAtivo.equals("P") || campoBuscaAtivo.equals("F")) {
				str.append(" and a.status = '" + campoBuscaAtivo.toUpperCase() + "'");
			}
			if (campoBuscaAtivo.equals("T")) {
				str.append(" and (a.status = 'P' or a.status = 'F') ");
			}
			listaProntuario = prontuarioController.findListByQueryDinamica(str.toString());
		} catch (Exception e) {
			System.out.println("Erro ao buscar Prontuario");
			e.printStackTrace();
		}
	}

	public void buscaDadosPaciente()  {
		try {
			lstDadosPaciente = new ArrayList<Prontuario>();
			StringBuilder str = new StringBuilder();
			str.append("from Prontuario a where 1=1");
			str.append(" and a.paciente.idPaciente = " + prontuarioModel.getPaciente().getIdPaciente());
			lstDadosPaciente = prontuarioController.findListByQueryDinamica(str.toString());
		} catch (Exception e) {
			System.out.println("Erro ao buscar dados do Paciente");
			e.printStackTrace();
		}
	}

	@Override
	public String save() {
		try {

			prontuarioModel = prontuarioController.merge(prontuarioModel);
			prontuarioModel = new Prontuario();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public void saveNotReturn() {
		try {
			System.out.println("Salvar Prontuario --------------------------------");
			prontuarioModel.setStatus("F");
			prontuarioModel = prontuarioController.merge(prontuarioModel);
			sucesso();
			limpar();

			FacesContext.getCurrentInstance().getExternalContext().redirect("/clinica/prontuario/listaProntuario.jsf");
			return;
		} catch (Exception e) {
			System.out.println("Erro ao salvar usuário");
		}
	}

	@Override
	public void saveEdit() {
		try {
			saveNotReturn();
		} catch (Exception e) {
			System.out.println("Erro ao Atualizar Prontuario");
			e.printStackTrace();
		}
	}

	@Override
	public String novo()  {
		setarVariaveisNulas();
		return getUrl();
	}

	public void limpar()  {
		prontuarioModel = new Prontuario();
	}

	@Override
	public String editar()  {
		return getUrl();
	}

	@Override
	public void excluir()  {
		try {
			prontuarioModel = (Prontuario) prontuarioController.getSession().get(Prontuario.class,
					prontuarioModel.getIdProntuario());
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			prontuarioController.delete(prontuarioModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		limpar();
		sucesso();
	}


	@Override
	public String redirecionarFindEntidade()  {
		setarVariaveisNulas();
		return getUrl();
	}

	public String redirecionarGuia()  {
		buscaDadosPaciente();
		return getUrlGuia();
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

	public String getUrlGuia() {
		return urlGuia;
	}

	public void setUrlGuia(String urlGuia) {
		this.urlGuia = urlGuia;
	}

	public void setPacienteController(PacienteController pacienteController) {
		this.pacienteController = pacienteController;
	}

	public String getCampoBuscaAtivo() {
		return campoBuscaAtivo;
	}

	public void setCampoBuscaAtivo(String campoBuscaAtivo) {
		this.campoBuscaAtivo = campoBuscaAtivo;
	}

	public List<Prontuario> getLstDadosPaciente() {
		return lstDadosPaciente;
	}

	public void setLstDadosPaciente(List<Prontuario> lstDadosPaciente) {
		this.lstDadosPaciente = lstDadosPaciente;
	}

}
