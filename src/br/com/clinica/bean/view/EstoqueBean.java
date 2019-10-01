package br.com.clinica.bean.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.EstoqueController;
import br.com.clinica.controller.geral.MaterialController;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.estoque.Estoque;
import br.com.clinica.model.cadastro.estoque.Material;
import br.com.clinica.model.cadastro.estoque.Pedido;

@Controller
@ViewScoped
@ManagedBean(name = "estoqueBean")
public class EstoqueBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;
	private Estoque estoqueModel;
	private Material materialModel;
	private Pedido pedidoModel;
	private List<Estoque> lstEstoque;
	List<Map<Object, Object>> lstEstoqueMap;
	private String url = "/estoque/gereciadorEstoque.jsf?faces-redirect=true";
	private String urlFind = "/estoque/findEstoque.jsf?faces-redirect=true";
	private String campoBusca = "";
	private String campoBuscaAtivo = "EM";

	@Autowired
	private EstoqueController estoqueController;

	@Autowired
	private MaterialController materialController;

	// @Autowired
	// private PedidoControllerpedidoController;

	public EstoqueBean() {
		estoqueModel = new Estoque();
		materialModel = new Material();
		pedidoModel = new Pedido();
		lstEstoque = new ArrayList<Estoque>();
	}

	public void busca() throws Exception {

		lstEstoque = new ArrayList<Estoque>();
		StringBuilder str = new StringBuilder();
		str.append("from Estoque a where 1=1");

		if (!campoBusca.equals("")) {
			str.append(" and upper(a.material.nomeMaterial) like upper('%" + campoBusca + "%')");
		}
		
		if (!campoBuscaAtivo.equals("")) {
			str.append(" and status = '" +campoBuscaAtivo+"'");
		}
		

		lstEstoque = estoqueController.findListByQueryDinamica(str.toString());

		/*
		 * // Nova lista StringBuilder sql = new StringBuilder(); //lstEstoqueMap =
		 * estoqueController.
		 * getSqlListMap("select * from estoque e where idestoque is null");
		 * sql.append(" select "); if (!campoBusca.equals("")) { sql.
		 * append(" (select m.nomematerial from material m where m.idmaterial = e.idmaterial and m.nomematerial like '%"
		 * + campoBusca + "%') as material, "); sql.
		 * append(" (select f.nomefornecedor  from material m , fornecedor f where m.idmaterial = e.idmaterial and f.idfornecedor = m.idfornecedor and m.nomematerial like '%"
		 * + campoBusca + "%') as fornecedor, "); sql.
		 * append(" (select m.nomematerial    from material m where m.idmaterial = e.idmaterial and m.nomematerial like '%"
		 * + campoBusca + "%') as material, "); sql.
		 * append(" (select e.valorunitario   from material m where m.idmaterial = e.idmaterial and m.nomematerial like '%"
		 * + campoBusca + "%') as valor, "); sql.
		 * append(" (select e.quantidade 	   from material m where m.idmaterial = e.idmaterial and m.nomematerial like '%"
		 * + campoBusca + "%') as quantidade, "); sql.
		 * append(" (select e.ativo	       from material m where m.idmaterial = e.idmaterial and m.nomematerial like '%"
		 * + campoBusca + "%') as status "); } else { sql.
		 * append(" (select m.nomematerial    from material m where m.idmaterial = e.idmaterial) as material, "
		 * ); sql.
		 * append(" (select f.nomefornecedor  from material m , fornecedor f where m.idmaterial  = e.idmaterial and f.idfornecedor = m.idfornecedor) as fornecedor, "
		 * ); sql.
		 * append(" (select m.nomematerial    from material m where m.idmaterial = e.idmaterial) as material, "
		 * ); sql.
		 * append(" (select e.valorunitario   from material m where m.idmaterial = e.idmaterial) as valor, "
		 * ); sql.
		 * append(" (select e.quantidade 	   from material m where m.idmaterial = e.idmaterial) as quantidade, "
		 * ); sql.
		 * append(" (select e.ativo	       from material m where m.idmaterial = e.idmaterial) as status "
		 * ); } sql.append(" from estoque e ");
		 * 
		 * lstEstoqueMap = estoqueController.getSqlListMap(sql.toString());
		 */
	}

	/*
	 * public void inativar() {
	 * 
	 * if (estoqueModel.getAtivo().equals("I")) { estoqueModel.setAtivo("A"); } else
	 * { estoqueModel.setAtivo("I"); }
	 * 
	 * try { estoqueController.saveOrUpdate(estoqueModel); } catch (Exception e) {
	 * System.out.println("Erro ao ativar/inativar"); e.printStackTrace(); }
	 * this.estoqueModel = new Estoque(); try { busca(); } catch (Exception e) {
	 * System.out.println("Erro ao buscar"); e.printStackTrace(); } }
	 */
	
	public void limpar() {
		this.estoqueModel = new Estoque();
	}

	public void onRowSelect(SelectEvent event) {
		estoqueModel = (Estoque) event.getObject();
	}

	public EstoqueController getEstoqueController() {
		return estoqueController;
	}

	// Gera o Relatório
	@Override
	public StreamedContent getArquivoReport() throws Exception {
		super.setNomeRelatorioJasper("report_estoquista");
		super.setNomeRelatorioSaida("report_estoquista");
		super.setListDataBeanCollectionReport(estoqueController.findList(getClassImp()));
		return super.getArquivoReport();
	}

	private void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	@Override
	public String save() throws Exception {

		estoqueModel = estoqueController.merge(estoqueModel);
		estoqueModel = new Estoque();
		return "";
	}

	@Override
	public void saveNotReturn() throws Exception {
		System.out.println("Entrou no saveNotReturn()");
		estoqueController.merge(estoqueModel); // salvar estoque
		materialModel = new Material();
		sucesso();
	}

	public void solicitacaoPedido() throws Exception {
		System.out.println("Entrou no saveNotReturn()");
		materialController.merge(materialModel); // salvar material
		materialModel = new Material();
		estoqueModel = new Estoque();
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
	public void setarVariaveisNulas() throws Exception {
		estoqueModel = new Estoque();
	}

	public String editar() throws Exception {
		return getUrl();
	}

	@Override
	public void excluir() throws Exception {
		estoqueModel = (Estoque) estoqueController.getSession().get(getClassImp(), estoqueModel.getIdEstoque());
		estoqueController.delete(estoqueModel);
		estoqueModel = new Estoque();
		sucesso();
		busca();
	}

	@Override
	protected Class<Estoque> getClassImp() {
		return Estoque.class;
	}

	@Override
	public String redirecionarFindEntidade() throws Exception {
		setarVariaveisNulas();
		return getUrlFind();
	}

	@Override
	protected InterfaceCrud<Estoque> getController() {
		return estoqueController;
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
}
