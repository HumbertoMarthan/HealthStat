package br.com.clinica.bean.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.EstoqueController;
import br.com.clinica.controller.geral.PedidoController;
import br.com.clinica.controller.geral.PedidoMaterialController;
import br.com.clinica.model.cadastro.estoque.Estoque;
import br.com.clinica.model.cadastro.estoque.Material;
import br.com.clinica.model.cadastro.estoque.Pedido;
import br.com.clinica.model.cadastro.estoque.PedidoMaterial;

@Controller
@ViewScoped
@ManagedBean(name = "pedidoBean")
public class PedidoBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;
	private Pedido pedidoModel = new Pedido();
	private PedidoMaterial pedidoMaterialModel = new PedidoMaterial();;
	private Estoque estoqueModel = new Estoque();;
	private Material materialModel; // materialModel = new Material();
	private List<Pedido> lstPedido = new ArrayList<Pedido>();
	private List<Pedido> lstPedidoCarrinho = new ArrayList<>();
	private String url = "/estoque/gereciadorPedido.jsf?faces-redirect=true";
	private String urlFind = "/estoque/findPedido.jsf?faces-redirect=true";
	private String campoBuscaAtivo = "T";
	
	@Autowired
	private PedidoController pedidoController;

	@Autowired
	private EstoqueController estoqueController;

	@Autowired
	private PedidoMaterialController pedidoMaterialController;


	@PostConstruct
	public void init() {
			busca();
	}

	public void geraPedido() {
		String sql = "select MAX(numPedido)+1 as num FROM Pedido";
		List<Map<Object, Object>> lst = pedidoController.getSqlListMap(sql);
		lst.get(0).get("num");
		System.out.println(lst.get(0).get("num"));
		if (lst.get(0).get("num") == null) {
			pedidoModel.setNumPedido(1);
			System.out.println("Numero do Pedido :>" + pedidoModel);
		} else {
			pedidoModel.setNumPedido((Integer) lst.get(0).get("num"));
			System.out.println("Numero do Pedido :>" + pedidoModel);
		}
	}

	public void busca() {
		lstPedido = new ArrayList<Pedido>();
		StringBuilder str = new StringBuilder();
		str.append("from Pedido a where 1=1");
		try {
			lstPedido = pedidoController.findListByQueryDinamica(str.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void limpar() {
		this.pedidoModel = new Pedido();
	}

	public void onRowSelect(SelectEvent event) {
		pedidoModel = (Pedido) event.getObject();
	}

	@Override
	public String save() {

		try {
			pedidoModel = pedidoController.merge(pedidoModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		pedidoModel = new Pedido();
		return "";
	}

	@Override
	public void saveNotReturn()  {
		System.out.println("Entrou no saveNotReturn()");
		try {
			pedidoController.merge(pedidoModel);
		} catch (Exception e) {
			e.printStackTrace();
		} // salvar estoque
		pedidoModel = new Pedido();
		sucesso();
	}

	public void addLista() {
		//pedidoModel.setMaterial(new Material(materialModel.getIdMaterial()));
		pedidoModel.setMaterial(materialModel);
		System.out.println("Pedido :>" + pedidoModel.getMaterial().getIdMaterial());
		lstPedidoCarrinho.add(pedidoModel);
		pedidoModel = new Pedido();
	}

	public void removerLista(Pedido pedido) {
		lstPedidoCarrinho.remove(pedido);
	}

	public void salvarLista() {
		int num = lstPedidoCarrinho.get(0).getNumPedido();
		boolean salvo = false;
		for (Pedido carrinho : lstPedidoCarrinho) {

			StringBuilder sql = new StringBuilder();
			sql.append("from Estoque where idMaterial = " + carrinho.getMaterial().getIdMaterial());
			List<Estoque> armazenado = new ArrayList<Estoque>();

			try {
				armazenado = estoqueController.findListByQueryDinamica(sql.toString());
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			Long idMaterial = carrinho.getMaterial().getIdMaterial();
			Integer qtd = carrinho.getQuantidade();

			if (salvo == false) {
				try {
					pedidoController.merge(carrinho); // salva pedido
					salvo = true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (!armazenado.isEmpty()) { // Se o material ja tiver no estoque entra nesta condição

				if (armazenado.get(0).getMaterial().getIdMaterial() == idMaterial) {
					Estoque estoque = armazenado.get(0);
					Integer soma = qtd + armazenado.get(0).getQuantidade();

					estoque.setNumPedido(num);
					estoque.setMaterial(new Material(idMaterial));
					estoque.setQuantidade(soma);
					
					try {
						estoqueController.merge(estoque);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else { // se o item for novo

				estoqueModel.setNumPedido(num);
				estoqueModel.setMaterial(new Material(idMaterial));
				estoqueModel.setQuantidade(qtd);

				try {
					estoqueController.merge(estoqueModel);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		try {
			addMsg("Pedido Realizado com sucesso!");
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
		// pedidoModel = new Pedido();
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
	public void saveEdit()  {
		saveNotReturn();
	}

	@Override
	public String novo()  {
		setarVariaveisNulas();
		return getUrl();
	}

	public String edita()  {
		return getUrl();
	}

	public String editar()  {
		return getUrl();
	}

	@Override
	public void excluir()  {
		try {
			pedidoModel = (Pedido) pedidoController.getSession().get(Pedido.class, pedidoModel.getIdPedido());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			pedidoController.delete(pedidoModel);
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