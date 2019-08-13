package br.com.clinica.bean.view;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.ContasReceberController;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.financeiro.ContasReceber;

@Controller
@ViewScoped
@ManagedBean(name = "contasReceberBean")
public class ContasReceberBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private ContasReceber contasReceberModel;

	private String url = "/financeiro/receber/contasReceber.jsf?faces-redirect=true";
	private String urlFind = "/financeiro/receber/receberConsulta.jsf?faces-redirect=true";
	private List<ContasReceber> lstContasReceber;
	private String campoBuscaContasReceber = "";

	String estado = "P";

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public ContasReceberBean() {
		contasReceberModel = new ContasReceber();
	}

	@Autowired
	private ContasReceberController contasReceberController;

	public void mudaEstadoPendencia() {
		setEstado("P");
	}

	public void mudaEstadoEditarPagamento() {
		setEstado("E");
	}
	public void mudaEstadoTabelaPreco() {
		setEstado("T");
	}

	public void mudaEstadoPagamento() {
		setEstado("PA");
	}

	public void busca() throws Exception {
		StringBuilder str = new StringBuilder();
		str.append("from ContasReceber a where 1=1");

		if (!campoBuscaContasReceber.isEmpty()) {
			str.append(" and paciente.pessoa.pessoaNome like '%" + campoBuscaContasReceber + "%'");
		}

		lstContasReceber = contasReceberController.findListByQueryDinamica(str.toString());
	}

	public ContasReceber getContasReceberModel() {
		return contasReceberModel;
	}

	public void setContasReceberModel(ContasReceber contasReceberModel) {
		this.contasReceberModel = contasReceberModel;
	}

	@Override
	public String save() throws Exception {
		contasReceberModel = contasReceberController.merge(contasReceberModel);
		contasReceberModel = new ContasReceber();
		return "";
	}

	@Override
	public void saveNotReturn() throws Exception {
		contasReceberModel = contasReceberController.merge(contasReceberModel);
		contasReceberModel = new ContasReceber();
		sucesso();
		busca();
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
		contasReceberModel = new ContasReceber();
	}

	@Override
	public String editar() throws Exception {
		return getUrl();
	}

	@Override
	public void excluir() throws Exception {
		contasReceberModel = (ContasReceber) contasReceberController.getSession().get(getClassImp(),
				contasReceberModel.getIdContasReceber());
		contasReceberController.delete(contasReceberModel);
		contasReceberModel = new ContasReceber();
		sucesso();
		busca();
	}

	@Override
	protected Class<ContasReceber> getClassImp() {
		return ContasReceber.class;
	}

	@Override
	public String redirecionarFindEntidade() throws Exception {
		setarVariaveisNulas();
		mudaEstadoPendencia();
		return getUrlFind();
	}

	@Override
	protected InterfaceCrud<ContasReceber> getController() {
		return contasReceberController;
	}

	@Override
	public void consultarEntidade() throws Exception {
		contasReceberModel = new ContasReceber();
	}

	public void onRowSelect(SelectEvent event) {
		contasReceberModel = (ContasReceber) event.getObject();
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

	public ContasReceber getcontasReceberModel() {
		return contasReceberModel;
	}

	public void setcontasReceberModel(ContasReceber contasReceberModel) {
		this.contasReceberModel = contasReceberModel;
	}

	public List<ContasReceber> getLstContasReceber() {
		return lstContasReceber;
	}

	public void setLstContasReceber(List<ContasReceber> lstContasReceber) {
		this.lstContasReceber = lstContasReceber;
	}

	public String getCampoBuscaContasReceber() {
		return campoBuscaContasReceber;
	}

	public void setCampoBuscaContasReceber(String campoBuscaContasReceber) {
		this.campoBuscaContasReceber = campoBuscaContasReceber;
	}

	public ContasReceberController getContasReceberController() {
		return contasReceberController;
	}

	public void setContasReceberController(ContasReceberController contasReceberController) {
		this.contasReceberController = contasReceberController;
	}
}
