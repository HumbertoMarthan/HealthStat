package br.com.clinica.bean.view;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.MaterialController;
import br.com.clinica.model.cadastro.estoque.Material;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;

@Controller
@ViewScoped
@ManagedBean(name = "materialBean")
public class MaterialBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private Material materialModel = new Material();

	private String url = "/cadastro/cadMaterial.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/findMaterial.jsf?faces-redirect=true";

	private List<Material> lstMaterial = new ArrayList<Material>();

	private String campoBuscaNome = "";
	private String campoBuscaFornecedor = "";
	private String campoBuscaAtivo = "A";

	@Autowired
	private MaterialController materialController; // Injeta o Material Controller

	@PostConstruct
	public void init() {
		busca();
	}
	
	public void geraRelatorio(){
		JasperPrint  relatorio =  imprimir(lstMaterial, "material.jrxml");
		try {
			//JasperPrintManager.printReport(print, true);
			JasperPrintManager.printReport(relatorio, true);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	public void onRowSelect(SelectEvent event) {
		materialModel = (Material) event.getObject();
	}

	public void busca() {
		try {
			StringBuilder str = new StringBuilder();
			str.append("from Material a where 1=1");

			if (!campoBuscaNome.equals("")) {
				str.append(" and upper(a.nomeMaterial) like upper('%" + campoBuscaNome + "%')");
			}
			if (!campoBuscaFornecedor.equals("")) {
				str.append(" and upper(a.fornecedor.nomeFornecedor) like upper('%" + campoBuscaFornecedor + "%')");
			}
			if (campoBuscaAtivo.equals("A") || campoBuscaAtivo.equals("I")) {
				System.out.println("Entrou no A or I");
				str.append(" and a.ativo = '" + campoBuscaAtivo.toUpperCase() + "'");
			}
			if (campoBuscaAtivo.equals("T")) {
				System.out.println("Entro no T");
				str.append(" and (a.ativo = 'A' or a.ativo = 'I') ");
			}

			lstMaterial = materialController.findListByQueryDinamica(str.toString());
		} catch (Exception e) {
			System.out.println("Erro ao buscar material");
			e.printStackTrace();
		}
	}

	@Override
	public String save() {
		try {
			materialModel = materialController.merge(materialModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		limpar();
		return "";
	}

	private void limpar() {
		materialModel = new Material();
	}

	@Override
	public void saveNotReturn() {
		try {
			if (materialModel == null) {
				limpar();
			}
			materialModel = materialController.merge(materialModel);
			limpar();
			sucesso();
		} catch (Exception e) {
			System.out.println("Erro ao salvar Material");
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
			materialModel = (Material) materialController.getSession().get(Material.class,
					materialModel.getIdMaterial());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			materialController.delete(materialModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		limpar();
		sucesso();
		busca();
	}

	@Override
	public String redirecionarFindEntidade() {
		limpar();
		return getUrlFind();
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

	public String getCampoBuscaFornecedor() {
		return campoBuscaFornecedor;
	}

	public void setCampoBuscaFornecedor(String campoBuscaFornecedor) {
		this.campoBuscaFornecedor = campoBuscaFornecedor;
	}

	public String getCampoBuscaAtivo() {
		return campoBuscaAtivo;
	}

	public void setCampoBuscaAtivo(String campoBuscaAtivo) {
		this.campoBuscaAtivo = campoBuscaAtivo;
	}

}
