package br.com.clinica.bean.view;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.DoencaController;
import br.com.clinica.model.cadastro.outro.Doenca;

@Controller
@ViewScoped
@ManagedBean(name = "doencaBean")
public class DoencaBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private Doenca doencaModel = new Doenca();
	
	private List<Doenca> lstDoenca = new ArrayList<>();
	
	private String url = "/cadastro/cadDoenca.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/findDoenca.jsf?faces-redirect=true";

	@Autowired
	private DoencaController doencaController;

	@Override
	public String save(){
		try {
			doencaModel = doencaController.merge(doencaModel);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	@Override
	public void saveNotReturn() {
		try {
			doencaModel = doencaController.merge(doencaModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		limpar();
		sucesso();
	}

	@Override
	public void saveEdit() {
		saveNotReturn();
	}

	@Override
	public String novo(){
		limpar();
		return getUrl();
	}

	public void limpar() {
		doencaModel = new Doenca();
	}

	@Override
	public String editar() {
		return getUrl();
	}

	@Override
	public void excluir() {
		try {
			doencaModel = (Doenca) doencaController.getSession().get(Doenca.class, doencaModel.getIdDoenca());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			doencaController.delete(doencaModel);
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
	
	public Doenca getObjetoSelecionado() {
		return doencaModel;
	}

	public void setObjetoSelecionado(Doenca doencaModel) {

		this.doencaModel = doencaModel;
	}

	public String getUrl() {
		return url;
	}

	public String getUrlFind() {
		return urlFind;
	}

	public void setUrlFind(String urlFind) {
		this.urlFind = urlFind;
	}

	public Doenca getDoencaModel() {
		return doencaModel;
	}

	public void setDoencaModel(Doenca doencaModel) {
		this.doencaModel = doencaModel;
	}

	public List<Doenca> getLstDoenca() {
		return lstDoenca;
	}

	public void setLstDoenca(List<Doenca> lstDoenca) {
		this.lstDoenca = lstDoenca;
	}

	public DoencaController getDoencaController() {
		return doencaController;
	}

	public void setDoencaController(DoencaController doencaController) {
		this.doencaController = doencaController;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
