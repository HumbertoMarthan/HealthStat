package br.com.clinica.bean.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.CaixaController;
import br.com.clinica.controller.geral.ContasReceberController;
import br.com.clinica.controller.geral.PagamentoEspecialController;
import br.com.clinica.controller.geral.ParcelaPagarController;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.pessoa.Paciente;
import br.com.clinica.model.financeiro.Caixa;
import br.com.clinica.model.financeiro.ContasReceber;
import br.com.clinica.model.financeiro.FormaPagamento;
import br.com.clinica.model.financeiro.PagamentoEspecial;
import br.com.clinica.model.financeiro.ParcelaPagar;
import br.com.clinica.utils.DatasUtils;
import br.com.clinica.utils.DialogUtils;

@Controller
@ViewScoped
@ManagedBean(name = "contasReceberBean")
public class ContasReceberBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private ContasReceber contasReceberModel;
	private FormaPagamento formaPagamentoModel;
	private ParcelaPagar parcelaPagarModel;
	private PagamentoEspecial pagamentoEspecialModel;
	private Caixa caixaModel;
	private String url = "/financeiro/receber/contasReceber.jsf?faces-redirect=true";
	private String urlFind = "/financeiro/receber/receberConsulta.jsf?faces-redirect=true";
	private List<ContasReceber> lstContasReceber;
	private List<ParcelaPagar> lstParcelaPagar;
	private List<ParcelaPagar> lstParcelaPersonalizada;
	private List<ParcelaPagar> lstContaParcela;
	private List<ParcelaPagar> lstParcelaPagarPendentes;
	private List<PagamentoEspecial> lstEspecialPagarPendentes;
	private List<PagamentoEspecial> lstPagamentoEspecial;
	private String campoBuscaContasReceber = "";
	private String campoBuscaTipoConta = "P";
	String estado = "P";
	Double valorComDesconto;
	// É valor de quantas parcelas sera feita o valor total
	Long parcelas;

	private FormaPagamento formaUm;
	
	private Double valorUm;
	private Date vencimentoUm;
	
	private FormaPagamento formaDois;
	private Double valorDois;
	private Date vencimentoDois;
	
	
	
	@Autowired
	private ContasReceberController contasReceberController;

	@Autowired
	private PagamentoEspecialController pagamentoEspecialController;

	@Autowired
	private ParcelaPagarController parcelaPagarController;

	@Autowired
	private CaixaController caixaController;

	@PostConstruct
	public void init() {
		lstPagamentoEspecial = new ArrayList<>();
		try {
			busca();
		} catch (Exception e) {
			System.out.println("Erro ao buscar listas de contas pendentes (lista principal)");

			e.printStackTrace();
		}
	}

	public ContasReceberBean() {
		parcelaPagarModel = new ParcelaPagar();
		contasReceberModel = new ContasReceber();
		lstContasReceber = new ArrayList<ContasReceber>();
		lstParcelaPagar = new ArrayList<ParcelaPagar>();
		lstParcelaPagarPendentes = new ArrayList<ParcelaPagar>();
		lstEspecialPagarPendentes = new ArrayList<>();
		lstPagamentoEspecial = new ArrayList<>();
		caixaModel = new Caixa();
		pagamentoEspecialModel = new PagamentoEspecial();
		
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

	public void addForma() {
		System.out.println(pagamentoEspecialModel.getValorBruto());	
		lstPagamentoEspecial.add(pagamentoEspecialModel);
	}

	public void remForma(int index) {
		lstPagamentoEspecial.remove(index);
	}

	public void busca() {
		limpar();
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
	
	public void selecionarBusca(ContasReceber contas)  {
		try {
			System.out.println("Contas -> "+contas.getIdContasReceber());
			System.out.println("Tipo -> "+ contas.getTipoPagamento());
			if(contas.getTipoPagamento().equals("PAR")) {
				
				System.out.println("Entrou no Par");
				buscaParcelas(contas);
				DialogUtils.openDialog("mostraParcelas");
			
			} else if(contas.getTipoPagamento().equals("ESP")) {
				
				System.out.println("Entrou no Esp");
				buscaPagamentoEspecial(contas);
				DialogUtils.openDialog("mostraPagamentoEspecial");
				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void buscaParcelas(ContasReceber contas) {
		try {
			StringBuilder str = new StringBuilder();
			System.out.println("ID CONTAS A RECEBER PARA A PARCELA " + contasReceberModel.getIdContasReceber());
			str.append( "	select idparcela, situacao, valorbruto, numeroparcela, valordesconto, pagamentoEspecial, datapagamento, datavencimento,"
							+ " idcontasreceber from parcelapagar   where 1=1 and idcontasreceber = "
							+ contas.getIdContasReceber());
			lstParcelaPagarPendentes = (List<ParcelaPagar>) parcelaPagarController.getSQLListParam(str.toString(), ParcelaPagar.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void buscaPagamentoEspecial(ContasReceber contas) {
		try {
			StringBuilder str = new StringBuilder();
			System.out.println("ID CONTAS A RECEBER PARA A PARCELA " + contasReceberModel.getIdContasReceber() );
			str.append( " from PagamentoEspecial where contasReceber.idContasReceber = "+ contas.getIdContasReceber());
			lstEspecialPagarPendentes = (List<PagamentoEspecial>) pagamentoEspecialController.findListByQueryDinamica(str.toString());
			System.out.println("LISTA PAGAMENTO ESPECIAL > "+ lstEspecialPagarPendentes.size());
		} catch (Exception e) {
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
	
	public void recalcularDuasFormas() {
		try {
		Double restante = 0.0D;
		setValorDois(0.0D);
		if(getValorUm() > contasReceberModel.getValorComDesconto() ) {
			addMsg("Valor Digitado é maior que a conta atual");
		}else {
			restante = contasReceberModel.getValorComDesconto() - getValorUm();
		}
		if(getValorDois() > restante ) {
			addMsg("Valor Digitado é maior que a conta atual2");
		}else {
			System.out.println("RESTANTE <>> "+restante);
			setValorDois(restante);
		}
		}catch (Exception e) {
			e.printStackTrace();
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
			Double total = contasReceberModel.getValorConsulta()
					- (contasReceberModel.getValorDesconto() + contasReceberModel.getValorEntrada());
			
			if (total < 0 || total > contasReceberModel.getValorComDesconto()) {
				addMsg("Impossível atribuir um Desconto mair que o total da consulta");
			} else {
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
				parcelaPagarController.setExecuteParam("delete from parcelapagar where idcontasreceber = "
						+ contasReceberModel.getIdContasReceber() + " and situacao = 'P' ");
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
		try {
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
		} catch (Exception e) {
			System.out.println("Erro ao gerar numero e valor de parcelas");
			e.getMessage();
			e.printStackTrace();
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
		try {
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
				// Seta como conta PARCELADA - PAR
				contasReceberModel.setTipoPagamento("PAR");
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
						Date dataSegundaParcela = DatasUtils
								.addMonth(contasReceberModel.getDataVencimentoContasReceber(), 1);
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
				// Seta como conta PARCELADA - PAR
				contasReceberModel.setTipoPagamento("PAR");
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
						Date dataSegundaParcela = DatasUtils
								.addMonth(contasReceberModel.getDataVencimentoContasReceber(), 1);
						parcelaPagarModel.setDataVencimento(dataSegundaParcela);
					}
					if (i == 3) {
						Date dataTerceiraParcela = DatasUtils
								.addMonth(contasReceberModel.getDataVencimentoContasReceber(), 2);
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
				// Seta como conta PARCELADA - PAR
				contasReceberModel.setTipoPagamento("PAR");
				contasReceberController.merge(contasReceberModel);
				limpar();
			}
		} catch (Exception e) {
			System.out.println("Erro ao fazer pagamentos");
			e.printStackTrace();
		}
		busca();
	}
	

	public void fazerPagamentoDuasFormas() throws Exception {
		Long idContaReceber = contasReceberModel.getIdContasReceber();
		try {
			System.out.println("Entrou no fazerPagamentoDuasFormas() ");
			List<PagamentoEspecial> lstPagamento = new ArrayList<PagamentoEspecial>();
			lstPagamento = pagamentoEspecialController.findListByQueryDinamica(
					"from PagamentoEspecial where contasReceber.idContasReceber = " + idContaReceber);

			for (PagamentoEspecial pa : lstPagamento) {
				if (pa.getContasReceber().getIdContasReceber() == idContaReceber
						&& contasReceberModel.getStatus().equals("L") // Se o status da conta for Lançadp
						&& pa.getSituacao().equals("P")) { // Se as parcelas estiverem pendentes
					pagamentoEspecialController.delete(pa); // Deleta as parcelas e relançam
				}
			}
			PagamentoEspecial pgUm = new PagamentoEspecial();
			PagamentoEspecial pgDois = new PagamentoEspecial();
			
			pgUm.setFormaPagamento(formaUm);
			pgUm.setValorBruto(valorUm);
			pgUm.setDataVencimento(vencimentoUm);
			
			
			pgDois.setFormaPagamento(formaDois);
			pgDois.setValorBruto(valorDois);
			pgDois.setDataVencimento(vencimentoDois);

			
			lstPagamentoEspecial.add(pgUm); 
			lstPagamentoEspecial.add(pgDois); 

			for (PagamentoEspecial pe : lstPagamentoEspecial) {
				pagamentoEspecialModel = new PagamentoEspecial();

				pagamentoEspecialModel.setValorBruto(pe.getValorBruto());
				pagamentoEspecialModel.setDataVencimento(pe.getDataVencimento());
				System.out.println("id FORMA DE PAGAMENTOS" + pe.getFormaPagamento().getId());
				System.out.println("objecto FORMA DE PAGAMENTOS" + pe.getFormaPagamento());
				pagamentoEspecialModel.setFormaPagamento(pe.getFormaPagamento());
				pagamentoEspecialModel.setDataLancamento(new Date());
				pagamentoEspecialModel.setContasReceber(new ContasReceber(idContaReceber));

				pagamentoEspecialController.merge(pagamentoEspecialModel);
			}
			contasReceberModel.setStatus("L");
			// Seta como conta ESPECIAL - ESP
			contasReceberModel.setTipoPagamento("ESP");
			contasReceberController.merge(contasReceberModel);
			addMsg("Operação realizada com sucesso");
		} catch (Exception e) {
			System.out.println("Erro ao fazer pagamento duas formas");
			e.printStackTrace();
		}
	}

	/**
	 * Edita uma lançamento de parcelas já feito ao usuario e o STATUS É PENDENTE
	 * 
	 * @throws Exception
	 */
	public void editarPagamento() {
		Long idContaReceber = contasReceberModel.getIdContasReceber();
		if (contasReceberModel.getMaisForma().equals("N")) {
			
			try { //deleta os lancamentos do pagamento especial
				if(contasReceberModel.getStatus().equals("L")) {
					pagamentoEspecialController.setExecuteParam("delete from pagamentoespecial where idcontasreceber = " + idContaReceber +" and situacao = 'P' ");
				}
			} catch (Exception e) {
				System.out.println("Erro ao Deletar Pagamento Especial");
				e.printStackTrace();
			}
			
			try { //deleta os parcelas/pagar do pagamento parcelado 
				if(contasReceberModel.getStatus().equals("L")) {
					parcelaPagarController.setExecuteParam("delete from parcelapagar where idcontasreceber = " + idContaReceber +" and situacao = 'P' ");
				}
			} catch (Exception e) {
				System.out.println("Erro ao Deletar Pagamento Parcelado");
				e.printStackTrace();
			}

			try {
				fazerPagamento();
				busca();
			} catch (Exception e) {
				System.out.println("Erro ao salvar parcelas!");
				e.printStackTrace();
			}

			try {
				addMsg("Operação Realizada Com Sucesso!");
			} catch (Exception e) {
				e.getMessage();
				e.printStackTrace();
			}
			
		} else { // se for igual a 'S'
			System.out.println("Entrou no igual a S ");
			
			try { //deleta os lancamentos do pagamento especial
				if(contasReceberModel.getStatus().equals("L")) {
					pagamentoEspecialController.setExecuteParam("delete from pagamentoespecial where idcontasreceber = " + idContaReceber +" and situacao = 'P' ");
				}
			} catch (Exception e) {
				System.out.println("Erro ao Deletar Pagamento Especial");
				e.printStackTrace();
			}
			
			try { //deleta os parcelas/pagar do pagamento parcelado 
				if(contasReceberModel.getStatus().equals("L")) {
					parcelaPagarController.setExecuteParam("delete from parcelapagar where idcontasreceber = " + idContaReceber +" and situacao = 'P' ");
				}
			} catch (Exception e) {
				System.out.println("Erro ao Deletar Pagamento Parcelado");
				e.printStackTrace();
			}
		
			try {
				fazerPagamentoDuasFormas();
				busca();
			} catch (Exception e) {
				System.out.println("Erro ao fazer Pagamento Duas Formas");
				e.printStackTrace();
			}
		}

	}
	
	public void limparPedido() {
		new ContasReceberBean();
		setValorUm(0.0);
		setValorDois(0.0);
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

	public List<ParcelaPagar> getLstContaParcela() {
		return lstContaParcela;
	}

	public void setLstContaParcela(List<ParcelaPagar> lstContaParcela) {
		this.lstContaParcela = lstContaParcela;
	}

	public List<ParcelaPagar> getLstParcelaPersonalizada() {
		return lstParcelaPersonalizada;
	}

	public void setLstParcelaPersonalizada(List<ParcelaPagar> lstParcelaPersonalizada) {
		this.lstParcelaPersonalizada = lstParcelaPersonalizada;
	}

	public FormaPagamento getFormaPagamentoModel() {
		return formaPagamentoModel;
	}

	public void setFormaPagamentoModel(FormaPagamento formaPagamentoModel) {
		this.formaPagamentoModel = formaPagamentoModel;
	}

	public List<PagamentoEspecial> getLstPagamentoEspecial() {
		return lstPagamentoEspecial;
	}

	public void setLstPagamentoEspecial(List<PagamentoEspecial> lstPagamentoEspecial) {
		this.lstPagamentoEspecial = lstPagamentoEspecial;
	}

	public PagamentoEspecial getPagamentoEspecialModel() {
		return pagamentoEspecialModel;
	}

	public void setPagamentoEspecialModel(PagamentoEspecial pagamentoEspecialModel) {
		this.pagamentoEspecialModel = pagamentoEspecialModel;
	}

	public PagamentoEspecialController getPagamentoEspecialController() {
		return pagamentoEspecialController;
	}

	public void setPagamentoEspecialController(PagamentoEspecialController pagamentoEspecialController) {
		this.pagamentoEspecialController = pagamentoEspecialController;
	}

	public FormaPagamento getFormaUm() {
		return formaUm;
	}

	public void setFormaUm(FormaPagamento formaUm) {
		this.formaUm = formaUm;
	}

	public Double getValorUm() {
		return valorUm;
	}

	public void setValorUm(Double valorUm) {
		this.valorUm = valorUm;
	}

	public Date getVencimentoUm() {
		return vencimentoUm;
	}

	public void setVencimentoUm(Date vencimentoUm) {
		this.vencimentoUm = vencimentoUm;
	}

	public FormaPagamento getFormaDois() {
		return formaDois;
	}

	public void setFormaDois(FormaPagamento formaDois) {
		this.formaDois = formaDois;
	}

	public Double getValorDois() {
		return valorDois;
	}

	public void setValorDois(Double valorDois) {
		this.valorDois = valorDois;
	}

	public Date getVencimentoDois() {
		return vencimentoDois;
	}

	public void setVencimentoDois(Date vencimentoDois) {
		this.vencimentoDois = vencimentoDois;
	}

	public List<PagamentoEspecial> getLstEspecialPagarPendentes() {
		return lstEspecialPagarPendentes;
	}

	public void setLstEspecialPagarPendentes(List<PagamentoEspecial> lstEspecialPagarPendentes) {
		this.lstEspecialPagarPendentes = lstEspecialPagarPendentes;
	}
	
}
