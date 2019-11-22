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
import br.com.clinica.controller.geral.EspecialidadeController;
import br.com.clinica.model.cadastro.outro.Especialidade;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;

@Controller
@ViewScoped
@ManagedBean(name = "especialidadeBean")
public class EspecialidadeBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private Especialidade especialidadeModel = new Especialidade();
	
	private String url = "/cadastro/cadEspecialidade.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/findEspecialidade.jsf?faces-redirect=true";
	
	private List<Especialidade> lstEspecialidade = new ArrayList<Especialidade>();
	
	private String campoBuscaEspecialidade = "";
	private String campoBuscaAtivo = "T";

	@Autowired
	private EspecialidadeController especialidadeController;

	@PostConstruct
	public void init() {
		busca();
	}
	

	public void geraRelatorio(){
		JasperPrint  relatorio =  imprimir(lstEspecialidade, "especialidades.jrxml");
		try {
			JasperPrintManager.printReport(relatorio, true);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	public void busca() {
		try {
			lstEspecialidade = new ArrayList<Especialidade>();
			StringBuilder str = new StringBuilder();
			str.append("from Especialidade a where 1=1");

			if (!campoBuscaEspecialidade.equals("")) {
				str.append(" and (upper(a.nomeEspecialidade) like upper('%" + campoBuscaEspecialidade + "%'))");
			}
			if (campoBuscaAtivo.equals("A") || campoBuscaAtivo.equals("I")) {
				str.append(" and a.ativo = '" + campoBuscaAtivo.toUpperCase() + "'");
			}
			if (campoBuscaAtivo.equals("T")) {
				str.append(" and (a.ativo = 'A' or a.ativo = 'I') ");
			}

			lstEspecialidade = especialidadeController.findListByQueryDinamica(str.toString());
		} catch (Exception e) {
			System.out.println("Erro ao buscar Especialidades");
			e.printStackTrace();
		}
	}
	
	public void limpar() {
		especialidadeModel = new Especialidade();
	}

	public void onRowSelect(SelectEvent event) {
		especialidadeModel = (Especialidade) event.getObject();
	}
	

	public void onRowSelectDouble(SelectEvent event) {
		especialidadeModel = (Especialidade) event.getObject();
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/clinica/cadastro/cadEspecialidade.jsf");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public String save()  {
		try {
			especialidadeModel = especialidadeController.merge(especialidadeModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		limpar();
		return "";
	}

	@Override
	public void saveNotReturn() {
		try {
			especialidadeModel = especialidadeController.merge(especialidadeModel);
			limpar();
			sucesso();
		} catch (Exception e) {
			System.out.println("Erro ao Salvar Especialidade");
			e.printStackTrace();
		}
		busca();
	}

	@Override
	public void saveEdit() {
		saveNotReturn();
	}

	@Override
	public String novo() {
		limpar();
		return getUrl();
	}


	@Override
	public String editar() {
		return getUrl();
	}

	@Override
	public void excluir() {
		try {
			especialidadeModel = (Especialidade) especialidadeController.getSession().get(Especialidade.class,
					especialidadeModel.getIdEspecialidade());
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			especialidadeController.delete(especialidadeModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		limpar();
		sucesso();
	}

	@Override
	public String redirecionarFindEntidade() {
		limpar();
		return getUrlFind();
	}

	public Especialidade getEspecialidadeModel() {
		return especialidadeModel;
	}

	public void setEspecialidadeModel(Especialidade especialidadeModel) {
		this.especialidadeModel = especialidadeModel;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlFind() {
		return urlFind;
	}

	public void setUrlFind(String urlFind) {
		this.urlFind = urlFind;
	}

	public List<Especialidade> getLstEspecialidade() {
		return lstEspecialidade;
	}

	public void setLstEspecialidade(List<Especialidade> lstEspecialidade) {
		this.lstEspecialidade = lstEspecialidade;
	}

	public String getCampoBuscaEspecialidade() {
		return campoBuscaEspecialidade;
	}

	public void setCampoBuscaEspecialidade(String campoBuscaEspecialidade) {
		this.campoBuscaEspecialidade = campoBuscaEspecialidade;
	}

	public EspecialidadeController getEspecialidadeController() {
		return especialidadeController;
	}

	public void setEspecialidadeController(EspecialidadeController especialidadeController) {
		this.especialidadeController = especialidadeController;
	}

	public String getCampoBuscaAtivo() {
		return campoBuscaAtivo;
	}

	public void setCampoBuscaAtivo(String campoBuscaAtivo) {
		this.campoBuscaAtivo = campoBuscaAtivo;
	}

}
