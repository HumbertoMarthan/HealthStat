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
import br.com.clinica.controller.geral.PrecoConsultaController;
import br.com.clinica.model.cadastro.outro.PrecoConsulta;

@Controller
@ViewScoped
@ManagedBean(name = "precoConsultaBean")
public class PrecoConsultaBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;
	private PrecoConsulta precoConsultaModel = new PrecoConsulta();
	private String url = "/cadastro/cadPrecoConsulta.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/findPrecoConsulta.jsf?faces-redirect=true";
	private List<PrecoConsulta> lstPrecoConsulta = new ArrayList<PrecoConsulta>();
	private String campoCategoria = "";

	@Autowired
	private PrecoConsultaController precoConsultaController;

	@PostConstruct
	public void init() {
		busca();
	}

	public void busca() {
		try {
		lstPrecoConsulta = new ArrayList<PrecoConsulta>();
		StringBuilder str = new StringBuilder();
		str.append("from PrecoConsulta a where 1=1");

		if (!campoCategoria.equals("")) {
			str.append(" and upper(a.categoria) like upper('%" + campoCategoria + "%')");
		}
		
		lstPrecoConsulta =  precoConsultaController.findListByQueryDinamica(str.toString());
	}catch (Exception e) {
		System.out.println("Erro ao buscar PrecoConsulta");
		e.printStackTrace();
	}
		}

	public String edita() {
		return getUrl();
	}

	public void onRowSelect(SelectEvent event) {
		precoConsultaModel = (PrecoConsulta) event.getObject();
	}
	

	public void onRowSelectDouble(SelectEvent event) {
		precoConsultaModel = (PrecoConsulta) event.getObject();
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/clinica/cadastro/cadPrecoConsulta.jsf");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public void inativar() {

		if (precoConsultaModel.getAtivo().equals("I")) {
			precoConsultaModel.setAtivo("A");
		} else {
			precoConsultaModel.setAtivo("I");
		}

		try {
			precoConsultaController.saveOrUpdate(precoConsultaModel);
		} catch (Exception e) {
			System.out.println("Erro ao ativar/inativar");
			e.printStackTrace();
		}
		this.precoConsultaModel = new PrecoConsulta();
		try {
			busca();
		} catch (Exception e) {
			System.out.println("Erro ao buscar atendente");
			e.printStackTrace();
		}
	}

	@Override
	public String save()  {
		try {
			precoConsultaModel = precoConsultaController.merge(precoConsultaModel);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}


	@Override
	public void saveNotReturn()  {
		try {
		precoConsultaModel = precoConsultaController.merge(precoConsultaModel);
		precoConsultaModel = new PrecoConsulta();
		sucesso();
		}catch (Exception e) {
			System.out.println("Erro ao Salvar Convênio");
			e.printStackTrace();
		}
	}

	@Override
	public void saveEdit(){
		saveNotReturn();
	}

	@Override
	public String novo()  {
		setarVariaveisNulas();
		return getUrl();
	}

	public void limpar()  {
		precoConsultaModel = new PrecoConsulta();
	}

	@Override
	public String editar()  {
		return getUrl();
	}

	@Override
	public void excluir()  {
		try {
			precoConsultaModel = (PrecoConsulta) precoConsultaController.getSession().get(PrecoConsulta.class, precoConsultaModel.getIdPrecoConsulta());
		} catch (HibernateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			precoConsultaController.delete(precoConsultaModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		limpar();
		sucesso();
		busca();
	}

	@Override
	public String redirecionarFindEntidade()  {
		limpar();
		return getUrlFind();
	}

	public PrecoConsulta getPrecoConsultaModel() {
		return precoConsultaModel;
	}

	public void setPrecoConsultaModel(PrecoConsulta precoConsultaModel) {
		this.precoConsultaModel = precoConsultaModel;
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

	public List<PrecoConsulta> getLstPrecoConsulta() {
		return lstPrecoConsulta;
	}

	public void setLstPrecoConsulta(List<PrecoConsulta> lstPrecoConsulta) {
		this.lstPrecoConsulta = lstPrecoConsulta;
	}

	public String getCampoCategoria() {
		return campoCategoria;
	}

	public void setCampoCategoria(String campoCategoria) {
		this.campoCategoria = campoCategoria;
	}

	public PrecoConsultaController getPrecoConsultaController() {
		return precoConsultaController;
	}

	public void setPrecoConsultaController(PrecoConsultaController precoConsultaController) {
		this.precoConsultaController = precoConsultaController;
	}
	
	

}