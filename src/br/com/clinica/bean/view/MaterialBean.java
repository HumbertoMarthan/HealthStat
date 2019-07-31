package br.com.clinica.bean.view;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.MaterialController;
import br.com.clinica.model.cadastro.estoque.Material;


@Controller
@ViewScoped
@ManagedBean(name = "materialBean")
public class MaterialBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private Material materialModel;
	private String url = "/cadastro/cadMaterial.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/findMaterial.jsf?faces-redirect=true";
	private List<Material> lstMaterial;
	private String campoBuscaNome = "";
	private String campoBuscaFornecedor = "";

	@Autowired
	private MaterialController materialController; // Injeta o Material Controller

	public MaterialBean() {
		materialModel = new Material();
		lstMaterial = new ArrayList<Material>();
	}

	public void onRowSelect(SelectEvent event) {
		materialModel = (Material) event.getObject();
	}

	public void busca() throws Exception {
		StringBuilder str = new StringBuilder();
		str.append("from Material a where 1=1");

		if (!campoBuscaNome.equals("")) {
			str.append(" and upper(a.nomeMaterial) like upper('%" + campoBuscaNome + "%')");
		}
		if (!campoBuscaFornecedor.equals("")) {
			str.append(" and upper(a.fornecedor.nomeFornecedor) like upper('%" + campoBuscaFornecedor + "%')");
		}

		lstMaterial = materialController.findListByQueryDinamica(str.toString());
	}

	@Override
	public String save() throws Exception {

		materialModel = materialController.merge(materialModel);
		materialModel = new Material();

		return "";
	}

	@Override
	public void saveNotReturn() throws Exception {
		materialModel = materialController.merge(materialModel);
		materialModel = new Material();
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

	public String edita() throws Exception {
		return getUrl();
	}

	@Override
	public String editar() throws Exception {
		return getUrl();
	}

	@Override
	public void excluir() throws Exception {
		materialModel = (Material) 
				materialController.getSession().get(getClassImp(), 
						materialModel.getIdMaterial());
		materialController.delete(materialModel);
		materialModel = new Material();
		sucesso();
		busca();
	}

	@Override
	protected Class<Material> getClassImp() {
		return Material.class;
	}

	@Override
	public String redirecionarFindEntidade() throws Exception {
		setarVariaveisNulas();
		return getUrlFind();
	}

	@Override
	public void setarVariaveisNulas() throws Exception {
		materialModel = new Material();
	}

	@Override
	protected MaterialController getController() {
		return materialController;
	}

	@Override
	public void consultarEntidade() throws Exception {
		materialModel = new Material();
	}

	@Override
	public String condicaoAndParaPesquisa() throws Exception {
		return "";
	}

	// GETTERS E SETTERS

	public Material getMaterialModel() {
		return materialModel;
	}

	public void setMaterialModel(Material materialModel) {
		this.materialModel = materialModel;
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

	public List<Material> getLstMaterial() {
		return lstMaterial;
	}

	public void setLstMaterial(List<Material> lstMaterial) {
		this.lstMaterial = lstMaterial;
	}

	public String getCampoBuscaNome() {
		return campoBuscaNome;
	}

	public void setCampoBuscaNome(String campoBuscaNome) {
		this.campoBuscaNome = campoBuscaNome;
	}

	public MaterialController getMaterialController() {
		return materialController;
	}

	public void setMaterialController(MaterialController materialController) {
		this.materialController = materialController;
	}

}