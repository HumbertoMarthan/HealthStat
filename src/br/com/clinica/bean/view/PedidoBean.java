package br.com.clinica.bean.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
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
import br.com.clinica.controller.geral.PedidoController;
import br.com.clinica.controller.geral.PedidoMaterialController;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.estoque.Estoque;
import br.com.clinica.model.cadastro.estoque.Material;
import br.com.clinica.model.cadastro.estoque.Pedido;
import br.com.clinica.model.cadastro.estoque.PedidoMaterial;

@Controller
@ViewScoped
@ManagedBean(name = "pedidoBean")
public class PedidoBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;
	private Pedido pedidoModel;
	private PedidoMaterial pedidoMaterialModel;
	private Estoque estoqueModel;
	private Material materialModel;
	private List<Pedido> lstPedido;
	private List<Pedido> lstPedidoCarrinho;
	private String url = "/estoque/gereciadorPedido.jsf?faces-redirect=true";
	private String urlFind = "/estoque/findPedido.jsf?faces-redirect=true";
	private String campoBuscaAtivo = "T";

	@Autowired
	private PedidoController pedidoController;

	@Autowired
	private EstoqueController estoqueController;

	@Autowired
	private PedidoMaterialController pedidoMaterialController;

	// @Autowired
	// private PedidoControllerpedidoController;

	@PostConstruct
	public void init() {
		materialModel = new Material();
		pedidoModel = new Pedido();
		lstPedido = new ArrayList<Pedido>();
		lstPedidoCarrinho = new ArrayList<>();
		pedidoMaterialModel = new PedidoMaterial();
		estoqueModel = new Estoque();
		try {
			busca();
		} catch (Exception e) {
			System.out.println("Erro ao buscar Pedidos");
			e.printStackTrace();
		}
	}

	public void geraPedido() {
		String sql = "select MAX(numPedido)+1 as num FROM Pedido";
		List<Map<Object, Object>> lst = pedidoController.getSqlListMap(sql);
		lst.get(0).get("num");
		System.out.println(lst.get(0).get("num"));
		if (lst.get(0).get("num") == null) {
			pedidoModel.setNumPedido(1);
			System.out.println("Numero do Pedido :>"+pedidoModel);
		} else {
			pedidoModel.setNumPedido((Integer) lst.get(0).get("num"));
			System.out.println("Numero do Pedido :>"+pedidoModel);
		}
	}

	public void busca() throws Exception {
		lstPedido = new ArrayList<Pedido>();
		StringBuilder str = new StringBuilder();
		str.append("from Pedido a where 1=1");
		lstPedido = pedidoController.findListByQueryDinamica(str.toString());
	}

	public void limpar() {
		this.pedidoModel = new Pedido();
	}

	public void onRowSelect(SelectEvent event) {
		pedidoModel = (Pedido) event.getObject();
	}

	// Gera o Relatório
	@Override
	public StreamedContent getArquivoReport() throws Exception {
		super.setNomeRelatorioJasper("report_estoquista");
		super.setNomeRelatorioSaida("report_estoquista");
		super.setListDataBeanCollectionReport(pedidoController.findList(getClassImp()));
		return super.getArquivoReport();
	}

	@Override
	public String save() throws Exception {

		pedidoModel = pedidoController.merge(pedidoModel);
		pedidoModel = new Pedido();
		return "";
	}

	@Override
	public void saveNotReturn() throws Exception {
		System.out.println("Entrou no saveNotReturn()");
		pedidoController.merge(pedidoModel); // salvar estoque
		pedidoModel = new Pedido();
		sucesso();
	}

	public void addLista() {
		lstPedidoCarrinho.add(pedidoModel);
		pedidoModel = new Pedido();
		System.out.println("Pedido :>" + pedidoModel);
	}

	public void removerLista(Pedido pedido) {
		lstPedidoCarrinho.remove(pedido);
	}

	public void salvarLista() {
		Pedido pedido= new Pedido();
		for (Pedido carrinho : lstPedidoCarrinho) {
			StringBuilder sql = new StringBuilder();
			sql.append("from Estoque where idMaterial = " + carrinho.getMaterial().getIdMaterial());
			List<Estoque> armazenado = new ArrayList<Estoque>();

			try {
				armazenado = estoqueController.findListByQueryDinamica(sql.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (!armazenado.isEmpty()) { //Se o material ja tiver no estoque entra nesta condição
				try {
					if (armazenado.get(0).getMaterial().getIdMaterial() == carrinho.getMaterial().getIdMaterial()) { // se
						Estoque estoque = armazenado.get(0);
						Integer soma = carrinho.getQuantidade() + armazenado.get(0).getQuantidade();

						estoque.setNumPedido(carrinho.getNumPedido());
						estoque.setMaterial(new Material(carrinho.getMaterial().getIdMaterial()));
						// estoque.setIdMaterial(carrinho.getMaterial().getIdMaterial());
						estoque.setQuantidade(soma);

						estoqueController.merge(estoque);
						addMsg("Pedido Realizado com sucesso!");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				// se o item for novo
				estoqueModel.setNumPedido(carrinho.getNumPedido());
				estoqueModel.setPedido(new Pedido(carrinho.getIdPedido()));
				estoqueModel.setMaterial(new Material(carrinho.getMaterial().getIdMaterial()));
				// estoqueModel.setIdMaterial(carrinho.getMaterial().getIdMaterial());
				estoqueModel.setQuantidade(carrinho.getQuantidade());

				try {
					estoqueController.merge(estoqueModel);
					addMsg("Pedido Realizado com sucesso!");
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			pedido = carrinho;
		}
		try {
			pedidoController.merge(pedido); // salva pedido
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		limparCarrinho();
		try {
			busca();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void limparCarrinho() {
		pedidoModel = new Pedido();
		lstPedidoCarrinho = new ArrayList<Pedido>();
	}

	public void limparLista() {
		lstPedido = new ArrayList<>();
		pedidoModel = new Pedido();

	}

	public void limparPedido() {
		pedidoModel = new Pedido();
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
		pedidoModel = new Pedido();
	}

	public String editar() throws Exception {
		return getUrl();
	}

	@Override
	public void excluir() throws Exception {
		pedidoModel = (Pedido) pedidoController.getSession().get(getClassImp(), pedidoModel.getIdPedido());
		pedidoController.delete(pedidoModel);
		pedidoModel = new Pedido();
		sucesso();
		busca();
	}

	@Override
	protected Class<Pedido> getClassImp() {
		return Pedido.class;
	}

	@Override
	public String redirecionarFindEntidade() throws Exception {
		setarVariaveisNulas();
		return getUrlFind();
	}

	@Override
	protected InterfaceCrud<Pedido> getController() {
		return pedidoController;
	}

	public Pedido getPedidoModel() {
		return pedidoModel;
	}

	public void setPedidoModel(Pedido pedidoModel) {
		this.pedidoModel = pedidoModel;
	}

	public List<Pedido> getLstPedido() {
		return lstPedido;
	}

	public void setLstPedido(List<Pedido> lstPedido) {
		this.lstPedido = lstPedido;
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

	public String getCampoBuscaAtivo() {
		return campoBuscaAtivo;
	}

	public void setCampoBuscaAtivo(String campoBuscaAtivo) {
		this.campoBuscaAtivo = campoBuscaAtivo;
	}

	public PedidoMaterialController getPedidoMaterialController() {
		return pedidoMaterialController;
	}

	public void setPedidoMaterialController(PedidoMaterialController pedidoMaterialController) {
		this.pedidoMaterialController = pedidoMaterialController;
	}

	public Material getMaterialModel() {
		return materialModel;
	}

	public void setMaterialModel(Material materialModel) {
		this.materialModel = materialModel;
	}

	public PedidoController getPedidoController() {
		return pedidoController;
	}

	public void setPedidoController(PedidoController pedidoController) {
		this.pedidoController = pedidoController;
	}

	public PedidoMaterial getPedidoMaterialModel() {
		return pedidoMaterialModel;
	}

	public List<Pedido> getLstPedidoCarrinho() {
		return lstPedidoCarrinho;
	}

	public void setLstPedidoCarrinho(List<Pedido> lstPedidoCarrinho) {
		this.lstPedidoCarrinho = lstPedidoCarrinho;
	}

	public Estoque getEstoqueModel() {
		return estoqueModel;
	}

	public void setEstoqueModel(Estoque estoqueModel) {
		this.estoqueModel = estoqueModel;
	}

	public void setPedidoMaterialModel(PedidoMaterial pedidoMaterialModel) {
		this.pedidoMaterialModel = pedidoMaterialModel;
	}

}