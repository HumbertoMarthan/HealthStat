package br.com.clinica.bean.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.EstoqueController;
import br.com.clinica.controller.geral.MaterialController;
import br.com.clinica.model.cadastro.estoque.Estoque;
import br.com.clinica.model.cadastro.estoque.Material;
import br.com.clinica.model.cadastro.estoque.Pedido;
import br.com.clinica.utils.DialogUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;

@Controller
@ViewScoped
@ManagedBean(name = "estoqueBean")
public class EstoqueBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;
	
	private Estoque estoqueModel = new Estoque();
	private Material materialModel = new Material();
	private Pedido 	pedidoModel = new Pedido();
	
	private List<Estoque> lstEstoque = new ArrayList<Estoque>();
	private List<Map<Object, Object>> lstEstoqueMap;
	
	private String url = "/estoque/gereciadorEstoque.jsf?faces-redirect=true";
	private String urlFind = "/estoque/findEstoque.jsf?faces-redirect=true";
	
	private String campoBusca = "";
	private String campoBuscaAtivo = "R";
	
	private String setor;
	
	Integer alocado;

	@Autowired
	private EstoqueController estoqueController;

	@Autowired
	private MaterialController materialController;
	
	public void geraRelatorio(){
		JasperPrint  relatorio =  imprimir(lstEstoque, "estoque.jrxml");
		try {
			JasperPrintManager.printReport(relatorio, true);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	public void busca() {

		lstEstoque = new ArrayList<Estoque>();
		StringBuilder str = new StringBuilder();
		str.append("from Estoque a where 1=1");

		if (!campoBusca.equals("")) {
			str.append(" and upper(a.material.nomeMaterial) like upper('%" + campoBusca + "%')");
		}
		
		if (!campoBuscaAtivo.equals("")) {
			str.append(" and status = '" +campoBuscaAtivo+"'");
		}
		

		try {
			lstEstoque = estoqueController.findListByQueryDinamica(str.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void alocarMaterial(){
		Integer atual = null;
		Estoque aux = new Estoque();
		
		if(alocado > 0) {
		try {
			Integer seZero = estoqueModel.getQuantidade() - alocado;
			if(seZero == 0) {
				Integer soma = 0;
				
				aux.setMaterial(estoqueModel.getMaterial());
				aux.setNumPedido(estoqueModel.getNumPedido());
				
				List<Estoque> lstAux = new ArrayList<>();
				lstAux = estoqueController.findListByQueryDinamica("from Estoque where setor = '" +setor+"' and material.idMaterial =" +estoqueModel.getMaterial().getIdMaterial());
				
				if(!lstAux.isEmpty()) {
					soma = 	lstAux.get(0).getQuantidade() + alocado; 
					aux.setIdEstoque(lstAux.get(0).getIdEstoque());
					aux.setQuantidade(soma);
				}else {
					aux.setQuantidade(alocado);
				}
				aux.setStatus("AL");
				aux.setSetor(setor);
				aux.setValorUnitario(estoqueModel.getValorUnitario());
				estoqueController.merge(aux);

				aux = new Estoque();
				
				estoqueController.setExecuteParam("delete from estoque where idestoque = " + estoqueModel.getIdEstoque()); 
				
				
				sucesso();
				busca();
				limpar();
				setor = "";
				alocado = null;
				
				DialogUtils.closeDialog("alocarMaterial");
				
			}else {
			
				Integer soma = 0;
				
				atual = estoqueModel.getQuantidade() - alocado;
				estoqueModel.setQuantidade(atual);
				estoqueModel.setSetor(null);
				estoqueModel.setStatus("A");
				estoqueModel = estoqueController.merge(estoqueModel);
				
				aux.setMaterial(estoqueModel.getMaterial());
				aux.setNumPedido(estoqueModel.getNumPedido());
				List<Estoque> lstAux = new ArrayList<>();
				lstAux = estoqueController.findListByQueryDinamica("from Estoque where setor = '" +setor+"' and material.idMaterial =" +estoqueModel.getMaterial().getIdMaterial());
				
				if(!lstAux.isEmpty()) {
					soma = 	lstAux.get(0).getQuantidade() + alocado;
					aux.setIdEstoque(lstAux.get(0).getIdEstoque());
					aux.setQuantidade(soma);
				}else {
					aux.setQuantidade(alocado);
				}
				
				aux.setStatus("AL");
				aux.setSetor(setor);
				aux.setValorUnitario(estoqueModel.getValorUnitario());
				estoqueController.merge(aux);

				aux = new Estoque();

				sucesso();
				limpar();
				setor = "";
				alocado = null;
				busca();
				
				DialogUtils.closeDialog("alocarMaterial");
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
			} else {
				addMsg("Número negativo é inválido");
			}
		
		
	}	
	
	public void onRowSelect(SelectEvent event) {
		estoqueModel = (Estoque) event.getObject();
	}

	public EstoqueController getEstoqueController() {
		return estoqueController;
	}

	@Override
	public String save() {

		try {
			estoqueModel = estoqueController.merge(estoqueModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		estoqueModel = new Estoque();
		return "";
	}

	@Override
	public void saveNotReturn(){
		System.out.println("Entrou no saveNotReturn()");
		try {
			estoqueController.merge(estoqueModel);
		} catch (Exception e) {
			e.printStackTrace();
		} // salvar estoque
		limpar();
		sucesso();
	}

	public void solicitacaoPedido()  {
		System.out.println("Entrou no saveNotReturn()");

		try {
			materialController.merge(materialModel);
		} catch (Exception e) {
			e.printStackTrace();
		} // salvar material
		
		materialModel = new Material();
		estoqueModel = new Estoque();
		sucesso();
	}

	@Override
	public void saveEdit()  {
		saveNotReturn();
	}

	@Override
	public String novo(){
		limpar();
		return getUrl();
	}

	public void limpar() {
		estoqueModel = new Estoque();
	}
	
	@Override
	public String editar(){
		return getUrl();
	}

	@Override
	public void excluir() {
		try {
			estoqueModel = (Estoque) estoqueController.getSession().get(Estoque.class, estoqueModel.getIdEstoque());
		} catch (Exception e2) {
			e2.printStackTrace();
		}

		try {
			estoqueController.delete(estoqueModel);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		limpar();
		sucesso();
		
		try {
			busca();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String redirecionarFindEntidade() {
		limpar();
			return getUrlFind();
	}


	public Estoque getEstoqueModel() {
		return estoqueModel;
	}

	public void setEstoqueModel(Estoque estoqueModel) {
		this.estoqueModel = estoqueModel;
	}

	public List<Estoque> getLstEstoque() {
		return lstEstoque;
	}

	public void setLstEstoque(List<Estoque> lstEstoque) {
		this.lstEstoque = lstEstoque;
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

	public String getCampoBusca() {
		return campoBusca;
	}

	public void setCampoBusca(String campoBusca) {
		this.campoBusca = campoBusca;
	}

	public Pedido getPedidoModel() {
		return pedidoModel;
	}

	public void setPedidoModel(Pedido pedidoModel) {
		this.pedidoModel = pedidoModel;
	}

	public MaterialController getMaterialController() {
		return materialController;
	}

	public void setMaterialController(MaterialController materialController) {
		this.materialController = materialController;
	}

	public Material getMaterialModel() {
		return materialModel;
	}

	public void setMaterialModel(Material materialModel) {
		this.materialModel = materialModel;
	}

	public void setEstoqueController(EstoqueController estoqueController) {
		this.estoqueController = estoqueController;
	}

	public List<Map<Object, Object>> getLstEstoqueMap() {
		return lstEstoqueMap;
	}

	public void setLstEstoqueMap(List<Map<Object, Object>> lstEstoqueMap) {
		this.lstEstoqueMap = lstEstoqueMap;
	}

	public String getCampoBuscaAtivo() {
		return campoBuscaAtivo;
	}

	public void setCampoBuscaAtivo(String campoBuscaAtivo) {
		this.campoBuscaAtivo = campoBuscaAtivo;
	}

	public Integer getAlocado() {
		return alocado;
	}

	public void setAlocado(Integer alocado) {
		this.alocado = alocado;
	}

	public String getSetor() {
		return setor;
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}
	
	
}
