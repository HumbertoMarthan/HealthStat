package br.com.clinica.bean.view;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.ContasPagarController;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.financeiro.ContasPagar;


@Controller
@ViewScoped
@ManagedBean(name = "contasPagarBean")
public class ContasPagarBean extends BeanManagedViewAbstract {
	
	private static final long serialVersionUID = 1L;
	
	private ContasPagar contasPagarModel;
	
	private String url = "/financeiro/contasPagar.jsf?faces-redirect=true";
	private String urlFind = "/financeiro/findContasPagar.jsf?faces-redirect=true";
	private List<ContasPagar> lstContasPagar;
	private String campoBuscaContasPagar = "";
	
	public ContasPagarBean() {
		contasPagarModel = new ContasPagar();
	}
	
	@Autowired
	private ContasPagarController contasPagarController;
	
	public void busca() throws Exception {
		StringBuilder str = new StringBuilder();
		str.append("from ContasPagar a where 1=1");
		//if (!campoBuscaContasPagar.equals("")) {
			//str.append(" and upper(a.nomeContasPagar) like upper('%" + campoBuscaContasPagar + "%')");
		//}

		lstContasPagar = contasPagarController.findListByQueryDinamica(str.toString());
	}
	
	@Override
	public String save() throws Exception {
		contasPagarModel = contasPagarController.merge(contasPagarModel);
		contasPagarModel = new ContasPagar();
		return "";
	}
	
	@Override
	public void saveNotReturn() throws Exception {
		contasPagarModel = contasPagarController.merge(contasPagarModel);
		contasPagarModel = new ContasPagar();
		sucesso();
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
		contasPagarModel = new ContasPagar();
	}
	
	@Override
	public String editar() throws Exception {
		return getUrl();
	}
	
	@Override
	public void excluir() throws Exception {
		contasPagarModel = (ContasPagar) contasPagarController.getSession().get(getClassImp(),  contasPagarModel.getIdContasPagar());
		contasPagarController.delete(contasPagarModel);	
		contasPagarModel = new ContasPagar();
		sucesso();
		busca();
	}

	@Override
	protected Class<ContasPagar> getClassImp() {
		return ContasPagar.class;
	}
	
	@Override
	public String redirecionarFindEntidade() throws Exception {
		setarVariaveisNulas();
		return getUrlFind();
	}

	@Override
	protected InterfaceCrud<ContasPagar> getController() {
		return contasPagarController;
	}
	@Override
	public void consultarEntidade() throws Exception {
		 contasPagarModel = new ContasPagar();
	}
	
	public void onRowSelect(SelectEvent event) {
		contasPagarModel = (ContasPagar) event.getObject();
	}

	@Override
	public String condicaoAndParaPesquisa() throws Exception {
		return "";
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

	public ContasPagar getContasPagarModel() {
		return contasPagarModel;
	}

	public void setContasPagarModel(ContasPagar contasPagarModel) {
		this.contasPagarModel = contasPagarModel;
	}

	public List<ContasPagar> getLstContasPagar() {
		return lstContasPagar;
	}

	public void setLstContasPagar(List<ContasPagar> lstContasPagar) {
		this.lstContasPagar = lstContasPagar;
	}

	public String getCampoBuscaContasPagar() {
		return campoBuscaContasPagar;
	}

	public void setCampoBuscaContasPagar(String campoBuscaContasPagar) {
		this.campoBuscaContasPagar = campoBuscaContasPagar;
	}

	public ContasPagarController getContasPagarController() {
		return contasPagarController;
	}

	public void setContasPagarController(ContasPagarController contasPagarController) {
		this.contasPagarController = contasPagarController;
	}



	
	
	
	
	
}
