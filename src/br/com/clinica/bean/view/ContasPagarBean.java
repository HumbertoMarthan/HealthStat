package br.com.clinica.bean.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.CaixaController;
import br.com.clinica.controller.geral.ContasPagarController;
import br.com.clinica.controller.geral.EstoqueController;
import br.com.clinica.controller.geral.FornecedorController;
import br.com.clinica.controller.geral.MaterialController;
import br.com.clinica.controller.geral.ParcelaContasPagarController;
import br.com.clinica.controller.geral.PedidoController;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.estoque.Estoque;
import br.com.clinica.model.cadastro.estoque.Fornecedor;
import br.com.clinica.model.cadastro.estoque.Material;
import br.com.clinica.model.cadastro.estoque.Pedido;
import br.com.clinica.model.financeiro.Caixa;
import br.com.clinica.model.financeiro.ContasPagar;
import br.com.clinica.model.financeiro.ParcelaContasPagar;
import br.com.clinica.utils.DatasUtils;

@Controller
@ViewScoped
@ManagedBean(name = "contasPagarBean")
public class ContasPagarBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private ContasPagar contasPagarModel;
	private ParcelaContasPagar parcelaContasPagarModel;
	private Caixa caixaModel;
	private Pedido pedidoModel;
	private Estoque estoqueModel;
	private String url = "/financeiro/pagar/contasPagar.jsf?faces-redirect=true";
	private String urlFind = "/financeiro/pagar/findContasPagar.jsf?faces-redirect=true";
	private List<ContasPagar> lstContasPagar;
	private List<ParcelaContasPagar> lstParcelaPagarPendentes;
	private List<Pedido> lstPedido;
	private List<Estoque> lstEstoque;
	List<Map<Object, Object>> lstTotalPedido;
	private String campoBuscaFornecedor = "";
	private String campoBuscaStatus = "P";
	private String tipoConta = "";
	private Fornecedor fornecedorSelecionado;
	private Double valorPedido = 0.0D;
	Long parcelas;
	private List<ParcelaContasPagar> lstParcelaPagar;

	@Autowired
	private ContasPagarController contasPagarController;

	@Autowired
	private CaixaController caixaController;

	@Autowired
	private MaterialController materialController;

	@Autowired
	private FornecedorController fornecedorController;

	@Autowired
	private ParcelaContasPagarController parcelaContasPagarController;

	@Autowired
	private PedidoController pedidoController;

	@Autowired
	private EstoqueController estoqueController;

	public ContasPagarBean() {
		lstParcelaPagar = new ArrayList<>();
		parcelaContasPagarModel = new ParcelaContasPagar();
		contasPagarModel = new ContasPagar();
		lstContasPagar = new ArrayList<ContasPagar>();
		lstEstoque = new ArrayList<Estoque>();
		caixaModel = new Caixa();
		pedidoModel = new Pedido();
		estoqueModel = new Estoque();
		try {
			busca();
		} catch (Exception e) {
			System.out.println("Erro ao buscar contas a pagar");
			e.printStackTrace();
		}
	}

	public void abrirPedido() {
		try {
			System.out.println("Numero do pedido>>>" + pedidoModel.getNumPedido());
			lstEstoque = estoqueController
					.findListByQueryDinamica("from Estoque e where e.numPedido = " + pedidoModel.getNumPedido() +" and pedido.status = 'EM'");
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
		calcularTotalPedido();
	}

	public void onCellEdit(CellEditEvent event) {
		try {
			Object oldValue = event.getOldValue();
			Object newValue = event.getNewValue();
			Estoque estoqueSelecionado = new Estoque();
			DataTable table = (DataTable) event.getSource();
			estoqueSelecionado = (Estoque) table.getRowData();

			System.out.println("Valor Antigo> " + oldValue);
			System.out.println("Valor Editado> " + newValue);
			if (newValue != null && !newValue.equals(oldValue)) {
				estoqueSelecionado.setValorUnitario((Double) newValue); // quem ta nulo é o estoque
				try {
					estoqueController.merge(estoqueSelecionado);
					addMsg("Valor Alterado para :" + newValue);
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
		pedidoModel.setTotal(valorPedido); //pegar valor total do sum
		pedidoModel.setStatus("A"); //aprova o pedido 
		
		
		//setar valor para contas a pagar
		contasPagarModel.setDataLancamento(new Date());//data atual
		contasPagarModel.setStatus("P");
		contasPagarModel.setValorTotalConta(valorPedido);
		contasPagarModel.setMaterial(pedidoModel.getMaterial());
		contasPagarModel.setFornecedor(pedidoModel.getMaterial().getFornecedor());

		pedidoController.merge(pedidoModel);
		contasPagarController.merge(contasPagarModel);
		}catch(Exception e){
			e.getMessage();
			e.printStackTrace();
		}
	}
	
	public void reprovarPedido() {
		pedidoModel.setTotal(valorPedido); //pegar valor total do sum
		pedidoModel.setStatus("R"); //reprova o pedido 
		
	}	
		
	public void calcularTotalPedido() {
		try {
		StringBuilder str = new StringBuilder();
		str.append("select sum(c.valorUnitario) as total from Estoque c where numpedido = 9");
		try {
			lstTotalPedido = contasPagarController.getSqlListMap(str.toString());
			System.out.println("Tamanho da lista calcular"+lstContasPagar.size());
			valorPedido = Double.parseDouble(String.valueOf(lstTotalPedido.get(0).get("total")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		}catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}

	public void busca() throws Exception {
		lstContasPagar = new ArrayList<ContasPagar>();
		StringBuilder str = new StringBuilder();
		str.append("from ContasPagar a where 1=1");

		if (!campoBuscaStatus.equals("")) {
			str.append(" and a.status = '" + campoBuscaStatus.toUpperCase() + "'");
		}
		System.out.println("PESQUISA...........>>> " + campoBuscaFornecedor);
		if (!campoBuscaFornecedor.equals("")) {
			str.append(" and a.fornecedor.nomeFornecedor = '" + campoBuscaFornecedor.toUpperCase() + "'");
		}

		lstContasPagar = contasPagarController.findListByQueryDinamica(str.toString());
	}

	public void buscaPedido() throws Exception {
		lstPedido = new ArrayList<Pedido>();
		StringBuilder str = new StringBuilder();
		str.append("from Pedido a where 1=1");

		lstPedido = pedidoController.findListByQueryDinamica(str.toString());
	}

	public void selecionaFornecedor(Long cod) {
		fornecedorSelecionado = new Fornecedor();
		fornecedorSelecionado.setIdFornecedor(cod);
	}

	// gera valor das parcelas
	public void fazerParcelas(Long idForma) {
		try {
			if (idForma == 1) {
				System.out.println("ENTROU 1");
				Double valorParcela = contasPagarModel.getValorTotalConta();
				contasPagarModel.setValorConta(valorParcela);
				System.out.println("VALOR DA PARCELA POR 1" + valorParcela);
			}

			if (idForma == 2) {
				System.out.println("ENTROU 2");
				Double valorParcela = contasPagarModel.getValorTotalConta();
				valorParcela = valorParcela / 2;
				System.out.println("VALOR DA PARCELA POR 2: " + valorParcela);
				contasPagarModel.setValorConta(valorParcela);
			}
			if (idForma == 3) {
				System.out.println("ENTROU 3");
				Double valorParcela = contasPagarModel.getValorTotalConta();
				valorParcela = valorParcela / 3;
				System.out.println("VALOR DA PARCELA POR 2: " + valorParcela);
				contasPagarModel.setValorConta(valorParcela);
			}
		} catch (Exception e) {
			System.out.println("Erro ao gerar numero e valor de parcelas");
			e.getMessage();
			e.printStackTrace();
		}

		// Passa o numero da parcela para a condição de quantas vezes parcelas
		parcelas = idForma;
	}

	public void editarPagamento() {
		/*
		 * if (contasPagarModel.getIdContasPagar() != null) { Long idContaPagar =
		 * contasPagarModel.getIdContasPagar(); // Deleta a parcela referente a conta,
		 * para que eu // possa relancar parcelas novas try { List<ParcelaContasPagar>
		 * lstParcelas = new ArrayList<ParcelaContasPagar>(); lstParcelas =
		 * parcelaContasPagarController.
		 * findListByQueryDinamica("from ParcelaContasPagar"); for (ParcelaContasPagar
		 * parcelas : lstParcelas) { if (parcelas.getContasPagar().getIdContasPagar() ==
		 * idContaPagar && contasPagarModel.getStatus().equals("L") // Se o status da
		 * conta for Lançadp && parcelas.getSituacao().equals("P")) { // Se as parcelas
		 * estiverem pendentes parcelaContasPagarController.delete(parcelas); // Deleta
		 * as parcelas e relançam } } } catch (Exception e) {
		 * System.out.println("Erro ao executar Delete"); e.printStackTrace(); } } else
		 * {
		 */
		try {
			contasPagarModel = contasPagarController.merge(contasPagarModel);
		} catch (Exception e) {
			System.out.println("Erro ao salvar contas a pagar ");
			e.printStackTrace();
		}

		/*
		 * try {
		 * 
		 * fazerPagamento(); busca(); buscaParcelas(); } catch (Exception e) {
		 * System.out.println("Erro ao salvar parcelas!"); e.printStackTrace(); }
		 * 
		 * try { addMsg("Operação Realizada Com Sucesso!"); } catch (Exception e) {
		 * e.getMessage(); e.printStackTrace(); }
		 */
		contasPagarModel = new ContasPagar();
	}

	public void buscaParcelas() throws Exception {
		try {
			StringBuilder str = new StringBuilder();
			System.out.println("ID CONTAS A PAGAR PARA A PARCELA " + contasPagarModel.getIdContasPagar());
			str.append(" select idparcela, situacao, valorbruto, numeroparcela, ");
			str.append(" valordesconto, datapagamento, datavencimento, pagamentoEspecial,");
			str.append(" idcontaspagar from parcelacontaspagar   where 1=1 and idcontascontaspagar = "
					+ contasPagarModel.getIdContasPagar());

			lstParcelaPagarPendentes = (List<ParcelaContasPagar>) parcelaContasPagarController
					.getSQLListParam(str.toString(), ParcelaContasPagar.class);

		} catch (Exception e) {

			System.out.println("LISTA COM 0");
			e.printStackTrace();
		}
	}

	/**
	 * Lanca as parcelas que o atendente selecionou
	 *
	 * @throws Exception
	 */
	public void fazerPagamento() throws Exception {
		Long idContaPagar = contasPagarModel.getIdContasPagar();
		if (parcelas == 1) {
			parcelaContasPagarModel = new ParcelaContasPagar();
			parcelaContasPagarModel.setNumeroParcela(1);
			parcelaContasPagarModel.setDataVencimento(contasPagarModel.getDataVencimento());
			parcelaContasPagarModel.setValorBruto(contasPagarModel.getValorConta());
			parcelaContasPagarModel.setContasPagar(new ContasPagar(contasPagarModel.getIdContasPagar()));

			System.out.println(" LISTA PARCELAS ANTES DO CLEAN " + lstParcelaPagar);
			// Limpa a lista, pois se não ela armazena os resultados anteriores e salva
			// junto
			lstParcelaPagar.clear();
			System.out.println(" LISTA PARCELAS DEPOIS DO CLEAN " + lstParcelaPagar);

			// Adiciona os dados setados a lista
			lstParcelaPagar.add(parcelaContasPagarModel);

			// Percorre a lista e salva no banco cada parcela
			for (ParcelaContasPagar parcelaPagar : lstParcelaPagar) {
				parcelaContasPagarController.merge(parcelaPagar);
			}
			// Pesquisa
			contasPagarModel = contasPagarController.findByPorId(ContasPagar.class, idContaPagar);
			// Seta como conta já lançada
			contasPagarModel.setStatus("L");

			// Limpa a parcela e prepara para outra inserção
			parcelaContasPagarModel = new ParcelaContasPagar();
			contasPagarController.merge(contasPagarModel);

			// Seta como conta já lançada
			contasPagarModel.setStatus("L");
			contasPagarController.merge(contasPagarModel);
			limpar();
		}

		if (parcelas == 2) {
			for (int i = 1; i <= 2; i++) {
				parcelaContasPagarModel = new ParcelaContasPagar();
				parcelaContasPagarModel.setNumeroParcela(i);

				if (i == 1) {
					parcelaContasPagarModel.setDataVencimento(contasPagarModel.getDataVencimento());
				}
				if (i == 2) {
					Date dataSegundaParcela = DatasUtils.addMonth(contasPagarModel.getDataVencimento(), 1);
					parcelaContasPagarModel.setDataVencimento(dataSegundaParcela);
				}

				parcelaContasPagarModel.setValorBruto(contasPagarModel.getValorConta());
				parcelaContasPagarModel.setContasPagar(new ContasPagar(contasPagarModel.getIdContasPagar()));

				System.out.println(" LISTA PARCELAS ANTES DO CLEAN " + lstParcelaPagar);
				// Limpa a lista, pois se não ela armazena os resultados anteriores e salva
				// junto
				lstParcelaPagar.clear();
				System.out.println(" LISTA PARCELAS DEPOIS DO CLEAN " + lstParcelaPagar);

				// Adiciona os dados setados a lista
				lstParcelaPagar.add(parcelaContasPagarModel);

				// Percorre a lista e salva no banco cada parcela
				for (ParcelaContasPagar parcelaPagar : lstParcelaPagar) {
					parcelaContasPagarController.merge(parcelaPagar);
				}
				// Limpa a parcela e prepara para outra inserção
				parcelaContasPagarModel = new ParcelaContasPagar();
			}
			// Pesquisa
			// Seta como conta já lançada
			contasPagarModel.setStatus("L");
			contasPagarController.merge(contasPagarModel);
			limpar();
		}

		if (parcelas == 3) {
			for (int i = 1; i <= 3; i++) {
				parcelaContasPagarModel = new ParcelaContasPagar();
				parcelaContasPagarModel.setNumeroParcela(i);
				if (i == 1) {
					parcelaContasPagarModel.setDataVencimento(contasPagarModel.getDataVencimento());
				}
				if (i == 2) {
					Date dataSegundaParcela = DatasUtils.addMonth(contasPagarModel.getDataVencimento(), 1);
					parcelaContasPagarModel.setDataVencimento(dataSegundaParcela);
				}
				if (i == 3) {
					Date dataTerceiraParcela = DatasUtils.addMonth(contasPagarModel.getDataVencimento(), 2);
					parcelaContasPagarModel.setDataVencimento(dataTerceiraParcela);
				}
				parcelaContasPagarModel.setValorBruto(contasPagarModel.getValorConta());
				parcelaContasPagarModel.setContasPagar(new ContasPagar(contasPagarModel.getIdContasPagar()));

				System.out.println(" LISTA PARCELAS ANTES DO CLEAN " + lstParcelaPagar);
				// Limpa a lista, pois se não ela armazena os resultados anteriores e salva
				// junto

				lstParcelaPagar.clear();
				System.out.println(" LISTA PARCELAS DEPOIS DO CLEAN " + lstParcelaPagar);

				// Adiciona os dados setados a lista
				lstParcelaPagar.add(parcelaContasPagarModel);

				// Percorre a lista e salva no banco cada parcela
				for (ParcelaContasPagar parcelaPagar : lstParcelaPagar) {
					parcelaContasPagarController.merge(parcelaPagar);
				}
				// Limpa a parcela e prepara para outra inserção
				parcelaContasPagarModel = new ParcelaContasPagar();
			}
			// Seta como conta já lançada
			contasPagarModel.setStatus("L");
			contasPagarController.merge(contasPagarModel);
			limpar();
		}
		busca();
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
	public String save() throws Exception {
		contasPagarModel = contasPagarController.merge(contasPagarModel);
		contasPagarModel = new ContasPagar();
		return "";
	}

	public void pagarConta() {
		try {
			System.out.println("Conta atual sem pesquisa " + contasPagarModel.getIdContasPagar());
			contasPagarModel.setStatus("PA");
			contasPagarModel.setDataPagamento(new Date());
			contasPagarController.merge(contasPagarModel);

			ContasPagar contaAtual = contasPagarController.findByPorId(ContasPagar.class,
					contasPagarModel.getIdContasPagar());
			System.out.println("Conta atual com pesquisa " + contaAtual.getIdContasPagar());
			caixaModel.setFornecedor(contaAtual.getFornecedor());
			caixaModel.setDataLancamento(new Date());
			caixaModel.setContasPagar(new ContasPagar(contaAtual.getIdContasPagar()));
			caixaModel.setValorRetirado(contaAtual.getValorConta());
			caixaModel.setTipo("CP");

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
		contasPagarModel = new ContasPagar();
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
		contasPagarModel = (ContasPagar) contasPagarController.getSession().get(getClassImp(),
				contasPagarModel.getIdContasPagar());
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

}
