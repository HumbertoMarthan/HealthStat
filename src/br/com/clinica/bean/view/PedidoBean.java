package br.com.clinica.bean.view;

import java.util.ArrayList;
import java.util.Date;
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
import br.com.clinica.controller.geral.ItemPedidoController;
import br.com.clinica.controller.geral.PedidoController;
import br.com.clinica.model.cadastro.estoque.Estoque;
import br.com.clinica.model.cadastro.estoque.ItemPedido;
import br.com.clinica.model.cadastro.estoque.Material;
import br.com.clinica.model.cadastro.estoque.Pedido;
import br.com.clinica.utils.DialogUtils;

@Controller
@ViewScoped
@ManagedBean(name = "pedidoBean")
public class PedidoBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;
	private Pedido pedidoModel = new Pedido();
	private ItemPedido itemPedidoModel = new ItemPedido();
	private Estoque estoqueModel = new Estoque();
	private Material materialModel; // materialModel = new Material();
	private List<Pedido> lstPedido = new ArrayList<Pedido>();
	private List<Pedido> lstPedidoCarrinho = new ArrayList<>();
	private List<ItemPedido> lstItemPedidoCarrinho = new ArrayList<>();
	private String url = "/estoque/gereciadorPedido.jsf?faces-redirect=true";
	private String urlFind = "/estoque/findPedido.jsf?faces-redirect=true";
	private String campoBuscaAtivo = "T";
	
	@Autowired
	private PedidoController pedidoController;

	@Autowired
	private EstoqueController estoqueController;

	@Autowired
	private ItemPedidoController itemPedidoController;


	@PostConstruct
	public void init() {
			busca();
	}

	public void geraPedido() {
		pedidoModel = new Pedido();
		DialogUtils.openDialog("fazerPedido");
		
		String sql = "select COALESCE(MAX(numPedido)+1,0) as num FROM Pedido";
		List<Map<Object, Object>> lst = pedidoController.getSqlListMap(sql);
		lst.get(0).get("num");
		System.out.println(lst.get(0).get("num"));
		if ((Integer) lst.get(0).get("num") ==  0 || (Integer) lst.get(0).get("num") == null ) {
			pedidoModel.setNumPedido(1);
			System.out.println("Numero do Pedido :>" + pedidoModel.getNumPedido());
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
		sucesso();
		pedidoModel = new Pedido();
	}
	
	public void addLista() {
		
		if(true) {
			try {
				
				int quantidade = pedidoModel.getQuantidade();
				
				if(lstItemPedidoCarrinho.isEmpty()) {
					itemPedidoModel.setMaterial(materialModel);
					itemPedidoModel.setQuantidade(quantidade);

					lstItemPedidoCarrinho.add(itemPedidoModel);
				
				}else {
					System.out.println("Entrou");
					long id_material_novo = (long) materialModel.getIdMaterial();
					System.out.println(" MATERIAL NOVO -> " + id_material_novo);
					boolean adiciona = true;
					
					for(int i=0; i<lstItemPedidoCarrinho.size(); i++) {
						
						ItemPedido item = lstItemPedidoCarrinho.get(i);
						
						long id_material_current = (long) item.getMaterial().getId();
						System.out.println("Current " +id_material_current);
						if(id_material_novo == id_material_current) {
							adiciona = false;
							
							int qtd_anterior = item.getQuantidade();
							item.setQuantidade(qtd_anterior + quantidade);
							
							lstItemPedidoCarrinho.remove(i);
							lstItemPedidoCarrinho.add(i, item);
						}
					}
					
					if(adiciona) {
						itemPedidoModel.setMaterial(materialModel);
						itemPedidoModel.setQuantidade(quantidade);

						lstItemPedidoCarrinho.add(itemPedidoModel);
					}
				}
				
				pedidoModel = new Pedido();
				itemPedidoModel = new ItemPedido();
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}			
}
	
	public void removerLista(ItemPedido itempedido) {
		lstItemPedidoCarrinho.remove(itempedido);
	}

	public void salvarLista() {
		//--------------------SALVAR PEDIDO-----------------
		try {
			pedidoModel.setDataPedido(new Date());
			pedidoModel = pedidoController.merge(pedidoModel);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		//----------------------------------------------------
		for (ItemPedido carrinho : lstItemPedidoCarrinho) {
			
			try {
				
				carrinho.setNumPedido(pedidoModel.getNumPedido());
				carrinho.setDataPedido(pedidoModel.getDataPedido());
				itemPedidoController.merge(carrinho);
			} catch (Exception e) {
				error();
				e.printStackTrace();
			}
		}
		
		addMsg("Pedido Realizado com sucesso!");
		limparListaItem();
		busca();
	}

	public void limparCarrinho() {
		lstItemPedidoCarrinho = new ArrayList<ItemPedido>();
	}

	public void limparLista() {
		lstPedido = new ArrayList<>();
		//pedidoModel = new Pedido();
		materialModel = null;

	}
	
	public void limparListaItem() {
		lstItemPedidoCarrinho = new ArrayList<>();
		pedidoModel = new Pedido();
		//materialModel = null;

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

	public EstoqueController getEstoqueController() {
		return estoqueController;
	}

	public void setEstoqueController(EstoqueController estoqueController) {
		this.estoqueController = estoqueController;
	}

	public ItemPedidoController getItemPedidoController() {
		return itemPedidoController;
	}

	public void setItemPedidoController(ItemPedidoController itemPedidoController) {
		this.itemPedidoController = itemPedidoController;
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

	public ItemPedido getItemPedidoModel() {
		return itemPedidoModel;
	}

	public void setItemPedidoModel(ItemPedido itemPedidoModel) {
		this.itemPedidoModel = itemPedidoModel;
	}

	public List<ItemPedido> getLstItemPedidoCarrinho() {
		return lstItemPedidoCarrinho;
	}

	public void setLstItemPedidoCarrinho(List<ItemPedido> lstItemPedidoCarrinho) {
		this.lstItemPedidoCarrinho = lstItemPedidoCarrinho;
	}
	
/*	int c = 0;
	try{
		List<ItemPedido> aux = new ArrayList<ItemPedido>();

		if(lstItemPedidoCarrinho.isEmpty()) {

			pedidoModel.setMaterial(materialModel);
			itemPedidoModel.setMaterial(materialModel);
			itemPedidoModel.setQuantidade(pedidoModel.getQuantidade());
			lstItemPedidoCarrinho.add(itemPedidoModel);
			
			pedidoModel = new Pedido();
			itemPedidoModel = new ItemPedido();
	
		}	
			pedidoModel.setMaterial(materialModel);
			itemPedidoModel.setMaterial(materialModel);
			itemPedidoModel.setQuantidade(pedidoModel.getQuantidade());
		
			
			for (int i = 0; i < lstItemPedidoCarrinho.size(); i++) {
				if(lstItemPedidoCarrinho.get(i).getMaterial().getIdMaterial() == itemPedidoModel.getMaterial().getIdMaterial()) {
					lstItemPedidoCarrinho.get(i).setQuantidade(lstItemPedidoCarrinho.get(i).getQuantidade() + itemPedidoModel.getQuantidade());
				}else {
					c=1;
				}
			}
				
			
			if(c == 1) {
				System.out.println("ADD NOVO");
				lstItemPedidoCarrinho.add(itemPedidoModel);
			}
			pedidoModel = new Pedido();
			itemPedidoModel = new ItemPedido();

	}catch (Exception e) {
		e.printStackTrace();
	}
}
}*/
	
	
}