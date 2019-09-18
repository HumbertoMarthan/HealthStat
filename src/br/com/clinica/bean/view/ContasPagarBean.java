package br.com.clinica.bean.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.CaixaController;
import br.com.clinica.controller.geral.ContasPagarController;
import br.com.clinica.controller.geral.FornecedorController;
import br.com.clinica.controller.geral.MaterialController;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.estoque.Fornecedor;
import br.com.clinica.model.cadastro.estoque.Material;
import br.com.clinica.model.financeiro.Caixa;
import br.com.clinica.model.financeiro.ContasPagar;

@Controller
@ViewScoped
@ManagedBean(name = "contasPagarBean")
public class ContasPagarBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private ContasPagar contasPagarModel;
	private Caixa caixaModel;
	private String url = "/financeiro/pagar/contasPagar.jsf?faces-redirect=true";
	private String urlFind = "/financeiro/pagar/findContasPagar.jsf?faces-redirect=true";
	private List<ContasPagar> lstContasPagar;
	private String campoBuscaFornecedor = "";
	private String campoBuscaStatus = "P";
	private String tipoConta = "";
	private Fornecedor fornecedorSelecionado;
	
	@Autowired
	private ContasPagarController contasPagarController;

	@Autowired
	private CaixaController caixaController;

	@Autowired
	private MaterialController materialController;
	
	@Autowired
	private FornecedorController fornecedorController;

	public ContasPagarBean() {
		contasPagarModel = new ContasPagar();
		lstContasPagar = new ArrayList<ContasPagar>();
		caixaModel = new Caixa();
		try {
			busca();
		} catch (Exception e) {
			System.out.println("Erro ao buscar contas a pagar");
			e.printStackTrace();
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

	public void selecionaFornecedor(Long cod) {
		fornecedorSelecionado = new Fornecedor();
		fornecedorSelecionado.setIdFornecedor(cod);
	}

	public List<Material> completeMaterial(String q) throws Exception {
		try {
		return materialController.findListByQueryDinamica(" from Material where ativo='A' and nomeMaterial like '%"
				+ q.toUpperCase() + "%' and fornecedor.idFornecedor =  " + fornecedorSelecionado.getIdFornecedor() +"order by nomeMaterial ASC");
		}catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
		return materialController.findListByQueryDinamica(" from Material where ativo='A' and nomeMaterial like '%"
				+ q.toUpperCase() + "%' order by nomeMaterial ASC");
		}

	public List<Fornecedor> completeFornecedor(String q) throws Exception {
		contasPagarModel.setMaterial(new Material());
		return fornecedorController
				.findListByQueryDinamica(" from Fornecedor where ativo='A' and nomeFornecedor like '%" + q.toUpperCase() + "%' order by nomeFornecedor ASC");
	}

	@Override
	public String save() throws Exception {
		contasPagarModel = contasPagarController.merge(contasPagarModel);
		contasPagarModel = new ContasPagar();
		return "";
	}

	public void pagarContaReceber() {
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

	public void setCaixaController(CaixaController caixaController) {
		this.caixaController = caixaController;
	}

}
