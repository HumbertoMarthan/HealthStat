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
import br.com.clinica.controller.geral.ContasReceberController;
import br.com.clinica.controller.geral.ParcelaPagarController;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.pessoa.Paciente;
import br.com.clinica.model.financeiro.Caixa;
import br.com.clinica.model.financeiro.ContasReceber;
import br.com.clinica.model.financeiro.FormaPagamento;
import br.com.clinica.model.financeiro.ParcelaPagar;
import br.com.clinica.utils.DatasUtils;

@Controller
@ViewScoped
@ManagedBean(name = "contasReceberBean")
public class ContasReceberBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private ContasReceber contasReceberModel;
	private ParcelaPagar parcelaPagarModel;
	private Caixa caixaModel;
	private String url = "/financeiro/receber/contasReceber.jsf?faces-redirect=true";
	private String urlFind = "/financeiro/receber/receberConsulta.jsf?faces-redirect=true";
	private List<ContasReceber> lstContasReceber;
	private List<ParcelaPagar> lstParcelaPagar;
	private List<ParcelaPagar> lstParcelaPagarPendentes;
	private String campoBuscaContasReceber = "";
	private String campoBuscaTipoConta = "P";
	String estado = "P";
	Double valorComDesconto;
	// É valor de quantas parcelas sera feita o valor total
	Long parcelas;

	@Autowired
	private ContasReceberController contasReceberController;

	@Autowired
	private ParcelaPagarController parcelaPagarController;

	@Autowired
	private CaixaController caixaController;

	public ContasReceberBean() {
		parcelaPagarModel = new ParcelaPagar();
		contasReceberModel = new ContasReceber();
		lstContasReceber = new ArrayList<ContasReceber>();
		lstParcelaPagar = new ArrayList<ParcelaPagar>();
		lstParcelaPagarPendentes = new ArrayList<ParcelaPagar>();
		caixaModel = new Caixa();
		try {
			busca();
		} catch (Exception e) {
			System.out.println("Erro ao buscar listas de contas pendentes (lista principal)");

			e.printStackTrace();
		}
	}

	public void mudaEstadoPendencia() {
		setEstado("P");
	}

	public void mudaEstadoEditarPagamento() {
		setEstado("E");
	}

	public void mudaEstadoTabelaPreco() {
		setEstado("T");
	}

	public void mudaEstadoPagamento() {
		setEstado("PA");
	}

	public void busca() {
		try {
			lstContasReceber = new ArrayList<ContasReceber>();
			StringBuilder str = new StringBuilder();

			str.append("from ContasReceber a where 1=1");

			if (!campoBuscaContasReceber.isEmpty()) {
				str.append(" and paciente.pessoa.pessoaNome like '%" + campoBuscaContasReceber + "%'");
			}
			if (!campoBuscaTipoConta.toUpperCase().isEmpty()) {
				str.append(" and a.status = '" + campoBuscaTipoConta.toUpperCase() + "'");
			}
			lstContasReceber = contasReceberController.findListByQueryDinamica(str.toString());

		} catch (Exception e) {
			System.out.println("Erro ao fazer busca");
			e.printStackTrace();
		}
	}

	public void buscaParcelas() throws Exception {
		try {
			StringBuilder str = new StringBuilder();
			System.out.println("ID CONTAS A RECEBER PARA A PARCELA " + contasReceberModel.getIdContasReceber());
			str.append(
					"select idparcela, acrescimodesconto, situacao, valorbruto, numeroparcela, valordesconto, datapagamento, datavencimento,"
							+ " idcontasreceber from parcelapagar   where 1=1 and idcontasreceber = "
							+ contasReceberModel.getIdContasReceber());

			lstParcelaPagarPendentes = (List<ParcelaPagar>) parcelaPagarController.getSQLListParam(str.toString(),
					ParcelaPagar.class);

		} catch (Exception e) {

			System.out.println("LISTA COM 0");
			e.printStackTrace();
		}
	}

	/**
	 * Verifica se todas as parcelas foram pagar, se foram a conta tem o status
	 * alterada como paga
	 */
	public void verificaParcelasPendentes() {
		List<ParcelaPagar> total = new ArrayList<>();
		try {
			total = (List<ParcelaPagar>) parcelaPagarController
					.findListByQueryDinamica("from ParcelaPagar where contasReceber.idContasReceber = "
							+ parcelaPagarModel.getContasReceber().getIdContasReceber());
			List<ParcelaPagar> paga = new ArrayList<>();

			paga = (List<ParcelaPagar>) parcelaPagarController
					.findListByQueryDinamica("from ParcelaPagar where contasReceber.idContasReceber = "
							+ parcelaPagarModel.getContasReceber().getIdContasReceber() + " and situacao = 'PA' ");

			System.out.println("QUANTIDADE DE PARCELAS " + total.size());
			System.out.println("QUANTIDADE DE PARCELAS PAGAS " + paga.size());

			if (total.size() == paga.size()) {
				ContasReceber contaAtualModel = new ContasReceber();
				contaAtualModel = (ContasReceber) contasReceberController.findById(ContasReceber.class,
						parcelaPagarModel.getContasReceber().getIdContasReceber());

				contaAtualModel.setStatus("PA");
				contaAtualModel.setDataPagamento(new Date());
				contasReceberController.merge(contaAtualModel);
			}
		} catch (Exception e) {
			System.out.println("Erro ao verificar parcelas ");
			e.printStackTrace();
		}

	}

	public void pagarParcela() {
		try {
			ContasReceber conta = (ContasReceber) contasReceberController.findById(ContasReceber.class,
					parcelaPagarModel.getContasReceber().getIdContasReceber());
			// Muda a situação da parcela para paga
			parcelaPagarModel.setSituacao("PA");
			// Muda da
			parcelaPagarModel.setDataPagamento(new Date());

			// adiciona valor da parcela ao CAIXA DA EMPRESA
			// ----------------------------------------------------------
			caixaModel.setPaciente(new Paciente(conta.getPaciente().getIdPaciente()));
			caixaModel.setContasReceber(new ContasReceber(parcelaPagarModel.getContasReceber().getIdContasReceber()));
			caixaModel.setParcelaPagar(new ParcelaPagar(parcelaPagarModel.getIdParcela()));
			caixaModel.setDataLancamento(new Date());
			caixaModel.setTipo("CR");
			caixaModel.setValorInserido(parcelaPagarModel.getValorBruto());
			caixaController.merge(caixaModel);
			// adiciona valor da parcela ao CAIXA DA EMPRESA
			// ----------------------------------------------------------

			// salva
			parcelaPagarController.merge(parcelaPagarModel);

			verificaParcelasPendentes();
			busca();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Problema ao pagar parcela");
		}
	}

	/**
	 * Seta o valor do desconto na label ValorComDesconto
	 */
	public void fazerDesconto() {
		try {
			System.out.println(" O Valor da Consulta " + contasReceberModel.getValorConsulta());
			contasReceberModel.setValorComDesconto(contasReceberModel.getValorConsulta());
			System.out.println(" O Valor do Desconto" + contasReceberModel.getValorDesconto());
			Double total = contasReceberModel.getValorConsulta() - (contasReceberModel.getValorDesconto() + contasReceberModel.getValorEntrada());
			if (total < 0 || total > contasReceberModel.getValorComDesconto()) {
				addMsg("Impossível atribuir um Desconto mair que o total da consulta");
			}else {
				System.out.println("TOTAL " + total);
				contasReceberModel.setValorComDesconto(total);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void limpar() {
		contasReceberModel = new ContasReceber();
	}

	/**
	 * Retira as parcelas pendentes no Usuario Adm
	 */
	public void relancarContasReceberAdm() {
		try {
			// Troca o status
			contasReceberModel.setStatus("P");
			// Limpar data de vencimento
			contasReceberController.merge(contasReceberModel);
		} catch (Exception e1) {
			System.out.println("erro ao gravar");
			e1.printStackTrace();
		}
		try {
			// select nas parcelas que são dessa conta e a situacao seja diferente de paga
			// deletar parcelas que estão pendentes
			parcelaPagarController.getListSQLDinamica("delete from parcelapagar where idcontasreceber = "
					+ contasReceberModel.getIdContasReceber() + " and situacao = 'P' ");
		} catch (Exception e) {
			System.out.println("erro ao deletar parcelas no estorno");
			e.printStackTrace();
		}
	}

	/**
	 * Verifica se há somente parcelas pendentes, se houver deixa estornar
	 */
	public void relancarContasReceberAtendente() {
		try {
			List<ParcelaPagar> total = new ArrayList<>();
			total = (List<ParcelaPagar>) parcelaPagarController
					.findListByQueryDinamica("from ParcelaPagar where contasReceber.idContasReceber = "
							+ contasReceberModel.getIdContasReceber());

			List<ParcelaPagar> pendente = new ArrayList<>();
			pendente = (List<ParcelaPagar>) parcelaPagarController
					.findListByQueryDinamica("from ParcelaPagar where contasReceber.idContasReceber = "
							+ contasReceberModel.getIdContasReceber() + " and situacao = 'P' ");

			System.out.println("QUANTIDADE DE PARCELAS " + total.size());
			System.out.println("QUANTIDADE DE PARCELAS PENDENTE " + pendente.size());
			if (total.size() == pendente.size()) {
				try {
					// Troca o status
					contasReceberModel.setStatus("P");
					contasReceberModel.setValorDesconto(null);
					contasReceberModel.setValorParcelado(null);
					contasReceberModel.setFormaPagamento(new FormaPagamento(null));
					contasReceberModel.setValorComDesconto(null);
					contasReceberModel.setQuantidadeParcelas(0);
					contasReceberModel.setDataVencimentoContasReceber(null);
					// Limpar data de vencimento

					contasReceberController.merge(contasReceberModel);
				} catch (Exception e1) {
					System.out.println("erro ao estornar");
					e1.printStackTrace();
				}
				// select nas parcelas que são dessa conta e a situacao seja diferente de paga
				// deletar parcelas que estão pendentes
				parcelaPagarController.getListSQLDinamica("delete from parcelapagar where idcontasreceber = "
						+ contasReceberModel.getIdContasReceber() + " and situacao = 'P' ");
			} else {

			}
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public void estornarParcelas() {
		// Selecionar a parcela
		// Trocar status para Pendente
		parcelaPagarModel.setSituacao("P");
		// Limpar data de pagamento
		parcelaPagarModel.setDataPagamento(null);
		// Limpar data de vencimento
		// parcelaPagarModel.setDataVencimento(null);
		try {
			// Se tiver alguma parcela da conta inserida no caixa ela será deletada
			List<Caixa> lstCaixa = new ArrayList<Caixa>();
			lstCaixa = caixaController.findListByQueryDinamica(
					"from Caixa where 1=1 and parcelaPagar.idParcela = " + parcelaPagarModel.getIdParcela());

			// apagar conta do caixa
			// mudar status da parcela
			// limpar data de pagamento
			for (Caixa caixas : lstCaixa) {
				caixaController.delete(caixas); // Deleta as parcelas e relançam
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// verificar necessidade **************************************************
		try {
			ContasReceber contaAtual = new ContasReceber();
			contaAtual = contasReceberController.findByPorId(ContasReceber.class,
					parcelaPagarModel.getContasReceber().getIdContasReceber());
			// Se a conta for pendente
			if (contaAtual.getStatus().equals("P")) {
				contaAtual.setStatus("P");
			}
			contasReceberController.merge(contaAtual);
			parcelaPagarController.merge(parcelaPagarModel);
			busca();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void estornarParcelasPagas() {

		// Selecionar a parcela
		// Trocar status para Pendente
		parcelaPagarModel.setSituacao("P");
		// Limpar data de pagamento
		parcelaPagarModel.setDataPagamento(null);
		// Limpar data de vencimento
		// parcelaPagarModel.setDataVencimento(null);
		try {
			// Se tiver alguma parcela da conta inserida no caixa ela será deletada
			caixaController.getListSQLDinamica("delete from caixa where idContasReceber = "
					+ parcelaPagarModel.getContasReceber().getIdContasReceber() + " and idParcela = "
					+ parcelaPagarModel.getIdParcela());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			// Faz uma pesquisa no BD - para modificar a conta a receber
			ContasReceber contaAtual = new ContasReceber();
			contaAtual = contasReceberController.findByPorId(ContasReceber.class,
					parcelaPagarModel.getContasReceber().getIdContasReceber());
			// Se a conta for paga
			if (contaAtual.getStatus().equals("PA")) {
				contaAtual.setStatus("L");
			}
			contasReceberController.merge(contaAtual);
			parcelaPagarController.merge(parcelaPagarModel);
			busca();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// gera valor das parcelas
	public void fazerParcelas(Long idForma) {

		if (idForma == 1) {
			System.out.println("ENTROU 1");
			Double valorParcela = contasReceberModel.getValorComDesconto();
			contasReceberModel.setValorParcelado(valorParcela);
			System.out.println("VALOR DA PARCELA POR 1" + valorParcela);
		}

		if (idForma == 2) {
			System.out.println("ENTROU 2");
			Double valorParcela = contasReceberModel.getValorComDesconto();
			valorParcela = valorParcela / 2;
			System.out.println("VALOR DA PARCELA POR 2: " + valorParcela);
			contasReceberModel.setValorParcelado(valorParcela);
		}
		if (idForma == 3) {
			System.out.println("ENTROU 3");
			Double valorParcela = contasReceberModel.getValorComDesconto();
			valorParcela = valorParcela / 3;
			System.out.println("VALOR DA PARCELA POR 2: " + valorParcela);
			contasReceberModel.setValorParcelado(valorParcela);
		}

		// Passa o numero da parcela para a condição de quantas vezes parcelas
		parcelas = idForma;
	}
	
	public void fazerValorAvista() {

			Double valorAvista = contasReceberModel.getValorComDesconto();
			System.out.println("VALOR A VISTA: " + valorAvista);
			contasReceberModel.setValorParcelado(valorAvista);
	}


	/**
	 * Lanca as parcelas que o atendente selecionou
	 *
	 * @throws Exception
	 */
	public void fazerPagamento() throws Exception {
		Long idContaReceber = contasReceberModel.getIdContasReceber();
		if (parcelas == 1) {
			parcelaPagarModel = new ParcelaPagar();
			parcelaPagarModel.setNumeroParcela(1);
			parcelaPagarModel.setValorDesconto(contasReceberModel.getValorDesconto());
			parcelaPagarModel.setDataVencimento(contasReceberModel.getDataVencimentoContasReceber());
			parcelaPagarModel.setValorBruto(contasReceberModel.getValorParcelado());
			parcelaPagarModel.setContasReceber(new ContasReceber(contasReceberModel.getIdContasReceber()));

			System.out.println(" LISTA PARCELAS ANTES DO CLEAN " + lstParcelaPagar);
			// Limpa a lista, pois se não ela armazena os resultados anteriores e salva
			// junto
			lstParcelaPagar.clear();
			System.out.println(" LISTA PARCELAS DEPOIS DO CLEAN " + lstParcelaPagar);

			// Adiciona os dados setados a lista
			lstParcelaPagar.add(parcelaPagarModel);

			// Percorre a lista e salva no banco cada parcela
			for (ParcelaPagar parcelaPagar : lstParcelaPagar) {
				parcelaPagarController.merge(parcelaPagar);
			}
			// Pesquisa
			contasReceberModel = contasReceberController.findByPorId(ContasReceber.class, idContaReceber);
			// Seta como conta já lançada
			contasReceberModel.setStatus("L");

			// Limpa a parcela e prepara para outra inserção
			parcelaPagarModel = new ParcelaPagar();
			contasReceberController.merge(contasReceberModel);

			// Seta como conta já lançada
			contasReceberModel.setStatus("L");
			contasReceberController.merge(contasReceberModel);
			limpar();
		}

		if (parcelas == 2) {
			for (int i = 1; i <= 2; i++) {
				parcelaPagarModel = new ParcelaPagar();
				parcelaPagarModel.setNumeroParcela(i);
				parcelaPagarModel.setValorDesconto(contasReceberModel.getValorDesconto());

				if (i == 1) {
					parcelaPagarModel.setDataVencimento(contasReceberModel.getDataVencimentoContasReceber());
				}
				if (i == 2) {
					Date dataSegundaParcela = DatasUtils.addMonth(contasReceberModel.getDataVencimentoContasReceber(),
							1);
					parcelaPagarModel.setDataVencimento(dataSegundaParcela);
				}

				parcelaPagarModel.setValorBruto(contasReceberModel.getValorParcelado());
				parcelaPagarModel.setContasReceber(new ContasReceber(contasReceberModel.getIdContasReceber()));

				System.out.println(" LISTA PARCELAS ANTES DO CLEAN " + lstParcelaPagar);
				// Limpa a lista, pois se não ela armazena os resultados anteriores e salva
				// junto
				lstParcelaPagar.clear();
				System.out.println(" LISTA PARCELAS DEPOIS DO CLEAN " + lstParcelaPagar);

				// Adiciona os dados setados a lista
				lstParcelaPagar.add(parcelaPagarModel);

				// Percorre a lista e salva no banco cada parcela
				for (ParcelaPagar parcelaPagar : lstParcelaPagar) {
					parcelaPagarController.merge(parcelaPagar);
				}
				// Limpa a parcela e prepara para outra inserção
				parcelaPagarModel = new ParcelaPagar();
			}
			// Pesquisa
			// Seta como conta já lançada
			contasReceberModel.setStatus("L");
			contasReceberController.merge(contasReceberModel);
			limpar();
		}

		if (parcelas == 3) {
			for (int i = 1; i <= 3; i++) {
				parcelaPagarModel = new ParcelaPagar();
				parcelaPagarModel.setNumeroParcela(i);
				parcelaPagarModel.setValorDesconto(contasReceberModel.getValorDesconto());
				if (i == 1) {
					parcelaPagarModel.setDataVencimento(contasReceberModel.getDataVencimentoContasReceber());
				}
				if (i == 2) {
					Date dataSegundaParcela = DatasUtils.addMonth(contasReceberModel.getDataVencimentoContasReceber(),
							1);
					parcelaPagarModel.setDataVencimento(dataSegundaParcela);
				}
				if (i == 3) {
					Date dataTerceiraParcela = DatasUtils.addMonth(contasReceberModel.getDataVencimentoContasReceber(),
							2);
					parcelaPagarModel.setDataVencimento(dataTerceiraParcela);
				}
				parcelaPagarModel.setValorBruto(contasReceberModel.getValorParcelado());
				parcelaPagarModel.setContasReceber(new ContasReceber(contasReceberModel.getIdContasReceber()));

				System.out.println(" LISTA PARCELAS ANTES DO CLEAN " + lstParcelaPagar);
				// Limpa a lista, pois se não ela armazena os resultados anteriores e salva
				// junto

				lstParcelaPagar.clear();
				System.out.println(" LISTA PARCELAS DEPOIS DO CLEAN " + lstParcelaPagar);

				// Adiciona os dados setados a lista
				lstParcelaPagar.add(parcelaPagarModel);

				// Percorre a lista e salva no banco cada parcela
				for (ParcelaPagar parcelaPagar : lstParcelaPagar) {
					parcelaPagarController.merge(parcelaPagar);
				}
				// Limpa a parcela e prepara para outra inserção
				parcelaPagarModel = new ParcelaPagar();
			}
			// Seta como conta já lançada
			contasReceberModel.setStatus("L");
			contasReceberController.merge(contasReceberModel);
			limpar();
		}
		busca();
	}

	/**
	 * Edita uma lançamento de parcelas já feito ao usuario e o STATUS É PENDENTE
	 * 
	 * @throws Exception
	 */
	public void editarPagamento() {
		Long idContaReceber = contasReceberModel.getIdContasReceber();
		// Deleta a parcela referente a conta, para que eu possa relancar parcelas novas
		try {
			List<ParcelaPagar> lstParcelas = new ArrayList<ParcelaPagar>();
			lstParcelas = parcelaPagarController.findListByQueryDinamica("from ParcelaPagar");
			for (ParcelaPagar parcelas : lstParcelas) {
				if (parcelas.getContasReceber().getIdContasReceber() == idContaReceber
						&& contasReceberModel.getStatus().equals("L") // Se o status da conta for Lançadp
						&& parcelas.getSituacao().equals("P")) { // Se as parcelas estiverem pendentes
					parcelaPagarController.delete(parcelas); // Deleta as parcelas e relançam
				}
			}
		} catch (Exception e) {
			System.out.println("Erro ao executar Delete");
			e.printStackTrace();
		}

		try {
			fazerPagamento();
			busca();
			buscaParcelas();
		} catch (Exception e) {
			System.out.println("Erro ao salvar parcelas!");
			e.printStackTrace();
		}

	}

	@Override
	public String save() throws Exception {

		contasReceberModel = contasReceberController.merge(contasReceberModel);
		contasReceberModel = new ContasReceber();
		return "";
	}

	@Override
	public void saveNotReturn() throws Exception {
		contasReceberModel = contasReceberController.merge(contasReceberModel);
		contasReceberModel = new ContasReceber();
		sucesso();
		busca();
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
		contasReceberModel = new ContasReceber();
	}

	@Override
	public String editar() throws Exception {
		return getUrl();
	}

	@Override
	public void excluir() throws Exception {
		contasReceberModel = (ContasReceber) contasReceberController.getSession().get(getClassImp(),
				contasReceberModel.getIdContasReceber());
		contasReceberController.delete(contasReceberModel);
		contasReceberModel = new ContasReceber();
		sucesso();
		busca();
	}

	@Override
	protected Class<ContasReceber> getClassImp() {
		return ContasReceber.class;
	}

	@Override
	public String redirecionarFindEntidade() throws Exception {
		setarVariaveisNulas();
		mudaEstadoPendencia();
		return getUrlFind();
	}

	@Override
	protected InterfaceCrud<ContasReceber> getController() {
		return contasReceberController;
	}

	@Override
	public void consultarEntidade() throws Exception {
		contasReceberModel = new ContasReceber();
	}

	// ---------------------------- GET SET
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

	public ContasReceber getcontasReceberModel() {
		return contasReceberModel;
	}

	public void setcontasReceberModel(ContasReceber contasReceberModel) {
		this.contasReceberModel = contasReceberModel;
	}

	public List<ContasReceber> getLstContasReceber() {
		return lstContasReceber;
	}

	public void setLstContasReceber(List<ContasReceber> lstContasReceber) {
		this.lstContasReceber = lstContasReceber;
	}

	public String getCampoBuscaContasReceber() {
		return campoBuscaContasReceber;
	}

	public void setCampoBuscaContasReceber(String campoBuscaContasReceber) {
		this.campoBuscaContasReceber = campoBuscaContasReceber;
	}

	public ContasReceberController getContasReceberController() {
		return contasReceberController;
	}

	public void setContasReceberController(ContasReceberController contasReceberController) {
		this.contasReceberController = contasReceberController;
	}

	public Double getValorComDesconto() {
		return valorComDesconto;
	}

	public void setValorComDesconto(Double valorComDesconto) {
		this.valorComDesconto = valorComDesconto;
	}

	public List<ParcelaPagar> getLstParcelaPagar() {
		return lstParcelaPagar;
	}

	public void setLstParcelaPagar(List<ParcelaPagar> lstParcelaPagar) {
		this.lstParcelaPagar = lstParcelaPagar;
	}

	public ParcelaPagar getParcelaPagarModel() {
		return parcelaPagarModel;
	}

	public Long getParcelas() {
		return parcelas;
	}

	public void setParcelaPagarModel(ParcelaPagar parcelaPagarModel) {
		this.parcelaPagarModel = parcelaPagarModel;
	}

	public void setParcelas(Long parcelas) {
		this.parcelas = parcelas;
	}

	public ParcelaPagarController getParcelaPagarController() {
		return parcelaPagarController;
	}

	public void setParcelaPagarController(ParcelaPagarController parcelaPagarController) {
		this.parcelaPagarController = parcelaPagarController;
	}

	public List<ParcelaPagar> getLstParcelaPagarPendentes() {
		return lstParcelaPagarPendentes;
	}

	public void setLstParcelaPagarPendentes(List<ParcelaPagar> lstParcelaPagarPendentes) {
		this.lstParcelaPagarPendentes = lstParcelaPagarPendentes;
	}

	public ContasReceber getContasReceberModel() {
		return contasReceberModel;
	}

	public void setContasReceberModel(ContasReceber contasReceberModel) {
		this.contasReceberModel = contasReceberModel;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCampoBuscaTipoConta() {
		return campoBuscaTipoConta;
	}

	public void setCampoBuscaTipoConta(String campoBuscaTipoConta) {
		this.campoBuscaTipoConta = campoBuscaTipoConta;
	}

	public void onRowSelect(SelectEvent event) {
		contasReceberModel = (ContasReceber) event.getObject();
	}

	public void onRowSelectParcelas(SelectEvent event) {
		parcelaPagarModel = (ParcelaPagar) event.getObject();
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

	public void setCaixaController(CaixaController caixaController) {
		this.caixaController = caixaController;
	}

}
