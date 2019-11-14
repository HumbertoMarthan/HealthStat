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
import br.com.clinica.controller.geral.CaixaController;
import br.com.clinica.controller.geral.ContasPagarController;
import br.com.clinica.controller.geral.EstoqueController;
import br.com.clinica.controller.geral.FornecedorController;
import br.com.clinica.controller.geral.ItemPedidoController;
import br.com.clinica.controller.geral.MaterialController;
import br.com.clinica.controller.geral.ParcelaContasPagarController;
import br.com.clinica.controller.geral.PedidoController;
import br.com.clinica.model.cadastro.estoque.Estoque;
import br.com.clinica.model.cadastro.estoque.Fornecedor;
import br.com.clinica.model.cadastro.estoque.ItemPedido;
import br.com.clinica.model.cadastro.estoque.Material;
import br.com.clinica.model.cadastro.estoque.Pedido;
import br.com.clinica.model.cadastro.usuario.Login;
import br.com.clinica.model.financeiro.Caixa;
import br.com.clinica.model.financeiro.ContasPagar;
import br.com.clinica.model.financeiro.ParcelaContasPagar;
import br.com.clinica.utils.DialogUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;

@Controller
@ViewScoped
@ManagedBean(name = "contasPagarBean")
public class ContasPagarBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private ContasPagar contasPagarModel = new ContasPagar();
	private ParcelaContasPagar parcelaContasPagarModel = new ParcelaContasPagar();
	private Caixa caixaModel = new Caixa();
	private Pedido pedidoModel = new Pedido();
	private Estoque estoqueModel = new Estoque();
	
	private String url = "/financeiro/pagar/contasPagar.jsf?faces-redirect=true";
	private String urlFind = "/financeiro/pagar/findContasPagar.jsf?faces-redirect=true";
	
	private List<ContasPagar> lstContasPagar = new ArrayList<ContasPagar>();
	private List<ParcelaContasPagar>  lstParcelaPagar = new ArrayList<>();
	private List<ParcelaContasPagar> lstParcelaPagarPendentes;
	private List<Pedido> lstPedido;
	private List<Estoque> lstEstoque = new ArrayList<Estoque>();
	private List<ItemPedido> lstItemPedido = new ArrayList<ItemPedido>();
	private List<Map<Object, Object>> lstTotalPedido;
	
	private String campoBuscaFornecedor = "";
	private String campoBuscaStatus = "P";
	private String tipoConta = "";
	
	private Fornecedor fornecedorSelecionado;
	private Double valorPedido = 0.0D;
	
	Long parcelas;
	String temParcela = "N";

	@Autowired
	private ContasPagarController contasPagarController;

	@Autowired
	private CaixaController caixaController;

	@Autowired
	private MaterialController materialController;

	@Autowired
	private FornecedorController fornecedorController;
	
	@Autowired
	private ContextoBean contextoBean;

	@Autowired
	private ParcelaContasPagarController parcelaContasPagarController;

	@Autowired
	private PedidoController pedidoController;

	@Autowired
	private EstoqueController estoqueController;

	@Autowired
	private ItemPedidoController itemPedidoController;
	
	@PostConstruct
	public void init() {
		try {
		busca();
		} catch (Exception e) {
			System.out.println("Erro ao buscar contas a pagar");
			e.printStackTrace();
		}
	}
	
	public void geraRelatorio(){
		JasperPrint  relatorio =  imprimir(lstContasPagar, "pagamento.jrxml");
		try {
			//JasperPrintManager.printReport(print, true);
			JasperPrintManager.printReport(relatorio, true);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	
	public void abrirPedido() {
		try {
			
			System.out.println("Numero do pedido>>>" + pedidoModel.getNumPedido());
			lstItemPedido = itemPedidoController.findListByQueryDinamica("from ItemPedido where numPedido = " + pedidoModel.getNumPedido());
			calcularTotalPedido();
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
		 calcularTotalPedido();
	}

	public void calcularTotalPedido() {
		try {
			StringBuilder str = new StringBuilder();
			str.append("select sum(c.valorUnitario * quantidade) as total from itempedido c where numpedido = "
					+ pedidoModel.getNumPedido());
			try {
				lstTotalPedido = contasPagarController.getSqlListMap(str.toString());
				System.out.println("Tamanho da lista calcular :>" + lstContasPagar.size());
				valorPedido = Double.parseDouble(String.valueOf(lstTotalPedido.get(0).get("total")));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}

	public void alteraValorUnitario(ItemPedido itemSelecionado) {
		try {
			System.out.println("Valor Editado> " + itemSelecionado.getValorUnitario());
			if (itemSelecionado.getValorUnitario() != null ) {
				itemSelecionado.setValorUnitario(itemSelecionado.getValorUnitario()); // quem ta nulo é o estoque
				try {
					itemPedidoController.merge(itemSelecionado);
					addMsg("Valor Alterado para :" + itemSelecionado.getValorUnitario());
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		calcularTotalPedido();
	}

	public void aprovarPedido() {
		try {
			
			Long usuarioSessaoId = 0L;

			try {
				usuarioSessaoId = contextoBean.getEntidadeLogada().getIdLogin();
			} catch (Exception e) {
				System.out.println("Erro ao recuperar da sessao");
				e.printStackTrace();
				e.getMessage();
			}
			
			List<Estoque> lstEstoque = new ArrayList<>();
			Estoque e = new Estoque();
			
			lstItemPedido = itemPedidoController.findListByQueryDinamica("from ItemPedido where numPedido = " + pedidoModel.getNumPedido());
			
			
			for (ItemPedido itemPedido : lstItemPedido) {
				e.setMaterial(itemPedido.getMaterial());
				e.setQuantidade(itemPedido.getQuantidade());
				lstEstoque.add(e);
			}
			
			for (Estoque estoque : lstEstoque) {
				estoque.setStatus("A");
				estoqueController.merge(estoque);
			}
			
			pedidoModel.setTotal(valorPedido); // pegar valor total do sum
			pedidoModel.setStatus("A"); // aprova o pedido

			// setar valor para contas a pagar
			contasPagarModel.setDataLancamento(new Date());// data atual
			contasPagarModel.setStatus("P");
			contasPagarModel.setValorTotalConta(valorPedido);
			contasPagarModel.setMaterial(pedidoModel.getMaterial());
			contasPagarModel.setFornecedor(lstItemPedido.get(0).getMaterial().getFornecedor());
			contasPagarModel.setLogin(new Login(usuarioSessaoId)); //SETEI O LOGIN ---------------------

			pedidoController.merge(pedidoModel);
			contasPagarController.merge(contasPagarModel);
			addMsg("Operação realizada com sucesso");
		} catch (Exception e) {
			try {
				addMsg("Ocorreu um erro ao aprovar");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.getMessage();
			e.printStackTrace();
		}
		buscaPedido();
		busca();
		DialogUtils.closeDialog("pedidosDialog");
	}

	public void reprovarPedido() {
		pedidoModel.setTotal(valorPedido); // pegar valor total do sum
		pedidoModel.setStatus("R"); // reprova o pedido
		try {
			pedidoController.merge(pedidoModel);
			addMsg("Operação realizada com sucesso");
		} catch (Exception e) {
			try {
				addMsg("Ocorreu um erro ao aprovar");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		buscaPedido();
		busca();
		DialogUtils.closeDialog("pedidosDialog");
	}

	public void busca()  {
		lstContasPagar = new ArrayList<ContasPagar>();
		StringBuilder str = new StringBuilder();
		str.append("from ContasPagar a where 1=1");

		if (!campoBuscaStatus.equals("")) {
			str.append(" and a.status = '" + campoBuscaStatus.toUpperCase() + "'");
		}
		if (!campoBuscaFornecedor.equals("")) {
			str.append(" and a.fornecedor.nomeFornecedor = '" + campoBuscaFornecedor.toUpperCase() + "'");
		}

		try {
			lstContasPagar = contasPagarController.findListByQueryDinamica(str.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void buscaPedido()  {
		lstPedido = new ArrayList<Pedido>();
		StringBuilder str = new StringBuilder();
		str.append("from Pedido a where 1=1");

		try {
			lstPedido = pedidoController.findListByQueryDinamica(str.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selecionaFornecedor(Long cod) {
		fornecedorSelecionado = new Fornecedor();
		fornecedorSelecionado.setIdFornecedor(cod);
	}

	public void editarPagamento() {
		try {
			
			Long usuarioSessaoId = 0L;

			try {
				usuarioSessaoId = contextoBean.getEntidadeLogada().getIdLogin();
			} catch (Exception e) {
				System.out.println("Erro ao recuperar da sessao");
				e.printStackTrace();
				e.getMessage();
			}

			
			contasPagarModel.setStatus("L");
			contasPagarModel.setDataLancamento(new Date());
			contasPagarModel.setLogin(new Login(usuarioSessaoId));
			contasPagarController.merge(contasPagarModel);

			addMsg("Operação Realizada Com Sucesso!");
			DialogUtils.closeDialog("contaFixa");
			busca();
		} catch (Exception e) {
			e.printStackTrace();
		}

		limpar();
	}

	public void limpar() {
		contasPagarModel = new ContasPagar();
	}

	public void fazerValorAvista() {

		Double valorAvista = contasPagarModel.getValorTotalConta();
		System.out.println("VALOR A VISTA: " + valorAvista);
		contasPagarModel.setValorConta(valorAvista);
	}

	public List<Material> completeMaterial(String q) throws Exception {
		try {
			return materialController.findListByQueryDinamica(" from Material where ativo='A' and nomeMaterial like '%"
					+ q.toUpperCase() + "%' and fornecedor.idFornecedor =  " + fornecedorSelecionado.getIdFornecedor()
					+ "order by nomeMaterial ASC");
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
		return materialController.findListByQueryDinamica(" from Material where ativo='A' and nomeMaterial like '%"
				+ q.toUpperCase() + "%' order by nomeMaterial ASC");
	}

	public List<Fornecedor> completeFornecedor(String q) throws Exception {
		contasPagarModel.setMaterial(new Material());
		return fornecedorController
				.findListByQueryDinamica(" from Fornecedor where ativo='A' and nomeFornecedor like '%" + q.toUpperCase()
						+ "%' order by nomeFornecedor ASC");
	}

	@Override
	public String save() {
		try {
			contasPagarModel = contasPagarController.merge(contasPagarModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		limpar();
		return "";
	}

	public void pagarConta() {
		try {
			
			contasPagarModel.setStatus("PA");
			contasPagarModel.setDataPagamento(new Date());
			contasPagarModel = contasPagarController.merge(contasPagarModel);

			ContasPagar contaAtual = contasPagarController.findByPorId(ContasPagar.class, contasPagarModel.getIdContasPagar());
			System.out.println("Conta Atual Valor >>"+ contaAtual.getValorConta());
			
			System.out.println("Conta atual com pesquisa " + contaAtual.getIdContasPagar());
			
			if(contaAtual.getFornecedor() == null ) {
				caixaModel.setFornecedor(contaAtual.getMaterial().getFornecedor());	
			}else {
				caixaModel.setFornecedor(contaAtual.getFornecedor());
			}
			caixaModel.setDataLancamento(new Date());
			caixaModel.setContasPagar(new ContasPagar(contaAtual.getIdContasPagar()));
			caixaModel.setValorRetirado(contaAtual.getValorTotalConta());
			caixaModel.setTipo("CP");
			
			limpar();
		} catch (Exception e1) {
			System.out.println("Erro ao busca conta para adicionar ao caixa");
			e1.printStackTrace();
		}

		try {
			caixaController.merge(caixaModel);
			addMsg("Conta foi Adicionada ao Caixa");
		} catch (Exception e) {
			System.out.println("Erro ao salvar no caixa (metodo pagar)");
			e.printStackTrace();
		}
		try {
			busca();
		} catch (Exception e) {
			System.out.println("Erro ao buscar no caixa (metodo pagar)");
			e.printStackTrace();
		}
		contasPagarModel = new ContasPagar();
	}

	@Override
	public void saveNotReturn() {
		contasPagarModel.setDataLancamento(new Date());

		try {
			contasPagarModel = contasPagarController.merge(contasPagarModel);
			sucesso();
			busca();
		} catch (Exception e) {
			System.out.println("Erro ao salvar contas a pagar ");
			e.printStackTrace();
		}
		limpar();
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
			contasPagarModel = (ContasPagar) contasPagarController.getSession().get(ContasPagar.class,
					contasPagarModel.getIdContasPagar());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			contasPagarController.delete(contasPagarModel);
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

	public void onRowSelect(SelectEvent event) {
		contasPagarModel = (ContasPagar) event.getObject();
	}

	public void onRowSelectPedido(SelectEvent event) {
		pedidoModel = (Pedido) event.getObject();
		// DialogUtils.openDialog("pedidosDialog");
		// DialogUtils.updateForm("formDialogPedidos");
	}

	public void onRowSelectEstoque(SelectEvent event) {
		estoqueModel = (Estoque) event.getObject();
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

	public ContasPagarController getContasPagarController() {
		return contasPagarController;
	}

	public String getCampoBuscaStatus() {
		return campoBuscaStatus;
	}

	public void setCampoBuscaStatus(String campoBuscaStatus) {
		this.campoBuscaStatus = campoBuscaStatus;
	}

	public void setContasPagarController(ContasPagarController contasPagarController) {
		this.contasPagarController = contasPagarController;
	}

	public Caixa getCaixaModel() {
		return caixaModel;
	}

	public void setCaixaModel(Caixa caixaModel) {
		this.caixaModel = caixaModel;
	}

	public CaixaController getCaixaController() {
		return caixaController;
	}

	public String getCampoBuscaFornecedor() {
		return campoBuscaFornecedor;
	}

	public void setCampoBuscaFornecedor(String campoBuscaFornecedor) {
		this.campoBuscaFornecedor = campoBuscaFornecedor;
	}

	public String getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(String tipoConta) {
		this.tipoConta = tipoConta;
	}

	public Long getParcelas() {
		return parcelas;
	}

	public void setParcelas(Long parcelas) {
		this.parcelas = parcelas;
	}

	public Fornecedor getFornecedorSelecionado() {
		return fornecedorSelecionado;
	}

	public void setFornecedorSelecionado(Fornecedor fornecedorSelecionado) {
		this.fornecedorSelecionado = fornecedorSelecionado;
	}

	public FornecedorController getFornecedorController() {
		return fornecedorController;
	}

	public void setFornecedorController(FornecedorController fornecedorController) {
		this.fornecedorController = fornecedorController;
	}

	public MaterialController getMaterialController() {
		return materialController;
	}

	public void setMaterialController(MaterialController materialController) {
		this.materialController = materialController;
	}

	public ParcelaContasPagar getParcelaContasPagarModel() {
		return parcelaContasPagarModel;
	}

	public void setParcelaContasPagarModel(ParcelaContasPagar parcelaContasPagarModel) {
		this.parcelaContasPagarModel = parcelaContasPagarModel;
	}

	public ParcelaContasPagarController getParcelaContasPagarController() {
		return parcelaContasPagarController;
	}

	public void setParcelaContasPagarController(ParcelaContasPagarController parcelaContasPagarController) {
		this.parcelaContasPagarController = parcelaContasPagarController;
	}

	public List<ParcelaContasPagar> getLstParcelaPagar() {
		return lstParcelaPagar;
	}

	public void setLstParcelaPagar(List<ParcelaContasPagar> lstParcelaPagar) {
		this.lstParcelaPagar = lstParcelaPagar;
	}

	public List<ParcelaContasPagar> getLstParcelaPagarPendentes() {
		return lstParcelaPagarPendentes;
	}

	public void setLstParcelaPagarPendentes(List<ParcelaContasPagar> lstParcelaPagarPendentes) {
		this.lstParcelaPagarPendentes = lstParcelaPagarPendentes;
	}

	public void setCaixaController(CaixaController caixaController) {
		this.caixaController = caixaController;
	}

	public List<Pedido> getLstPedido() {
		return lstPedido;
	}

	public void setLstPedido(List<Pedido> lstPedido) {
		this.lstPedido = lstPedido;
	}

	public PedidoController getPedidoController() {
		return pedidoController;
	}

	public void setPedidoController(PedidoController pedidoController) {
		this.pedidoController = pedidoController;
	}

	public Pedido getPedidoModel() {
		return pedidoModel;
	}

	public void setPedidoModel(Pedido pedidoModel) {
		this.pedidoModel = pedidoModel;
	}

	public Estoque getEstoqueModel() {
		return estoqueModel;
	}

	public List<Estoque> getLstEstoque() {
		return lstEstoque;
	}

	public void setLstEstoque(List<Estoque> lstEstoque) {
		this.lstEstoque = lstEstoque;
	}

	public EstoqueController getEstoqueController() {
		return estoqueController;
	}

	public void setEstoqueController(EstoqueController estoqueController) {
		this.estoqueController = estoqueController;
	}

	public List<Map<Object, Object>> getLstTotalPedido() {
		return lstTotalPedido;
	}

	public void setLstTotalPedido(List<Map<Object, Object>> lstTotalPedido) {
		this.lstTotalPedido = lstTotalPedido;
	}

	public Double getValorPedido() {
		return valorPedido;
	}

	public void setValorPedido(Double valorPedido) {
		this.valorPedido = valorPedido;
	}

	public void setEstoqueModel(Estoque estoqueModel) {
		this.estoqueModel = estoqueModel;
	}

	public String getTemParcela() {
		return temParcela;
	}

	public void setTemParcela(String temParcela) {
		this.temParcela = temParcela;
	}

	public ItemPedidoController getItemPedidoController() {
		return itemPedidoController;
	}

	public void setItemPedidoController(ItemPedidoController itemPedidoController) {
		this.itemPedidoController = itemPedidoController;
	}

	public List<ItemPedido> getLstItemPedido() {
		return lstItemPedido;
	}

	public void setLstItemPedido(List<ItemPedido> lstItemPedido) {
		this.lstItemPedido = lstItemPedido;
	}

	public ContextoBean getContextoBean() {
		return contextoBean;
	}

	public void setContextoBean(ContextoBean contextoBean) {
		this.contextoBean = contextoBean;
	}
}


/*
 * public void onCellEdit(CellEditEvent event) { try { Object oldValue =
 * event.getOldValue(); Object newValue = event.getNewValue(); Estoque
 * estoqueSelecionado = new Estoque(); DataTable table = (DataTable)
 * event.getSource(); estoqueSelecionado = (Estoque) table.getRowData();
 * 
 * System.out.println("Valor Antigo> " + oldValue);
 * System.out.println("Valor Editado> " + newValue); if (newValue != null &&
 * !newValue.equals(oldValue)) { estoqueSelecionado.setValorUnitario((Double)
 * newValue); // quem ta nulo é o estoque try {
 * estoqueController.merge(estoqueSelecionado); addMsg("Valor Alterado para :" +
 * newValue); } catch (Exception e) {
 * 
 * e.printStackTrace(); } } } catch (Exception ex) { ex.printStackTrace(); }
 * calcularTotalPedido(); }
 */
