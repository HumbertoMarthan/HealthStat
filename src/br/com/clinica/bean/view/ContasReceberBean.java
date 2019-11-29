package br.com.clinica.bean.view;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

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
import br.com.clinica.model.cadastro.pessoa.Paciente;
import br.com.clinica.model.cadastro.usuario.Login;
import br.com.clinica.model.financeiro.Caixa;
import br.com.clinica.model.financeiro.ContasReceber;
import br.com.clinica.model.financeiro.FormaPagamento;
import br.com.clinica.model.financeiro.PagamentoEspecial;
import br.com.clinica.model.financeiro.ParcelaPagar;
import br.com.clinica.utils.DatasUtils;
import br.com.clinica.utils.DialogUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;

@Controller
@ViewScoped
@ManagedBean(name = "contasReceberBean")
public class ContasReceberBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private ContasReceber contasReceberModel = new ContasReceber();
	private FormaPagamento formaPagamentoModel;
	private ParcelaPagar parcelaPagarModel = new ParcelaPagar();
	private PagamentoEspecial pagamentoEspecialModel = new PagamentoEspecial();
	private Caixa caixaModel = new Caixa();
	
	private String url = "/financeiro/receber/contasReceber.jsf?faces-redirect=true";
	private String urlFind = "/financeiro/receber/receberConsulta.jsf?faces-redirect=true";
	
	private List<ContasReceber> lstContasReceber = new ArrayList<ContasReceber>();
	private List<ParcelaPagar> 	lstParcelaPagar = new ArrayList<ParcelaPagar>();

	private List<ParcelaPagar> lstParcelaPagarPendentes = new ArrayList<ParcelaPagar>();
	private List<PagamentoEspecial> lstEspecialPagarPendentes = new ArrayList<>();
	private List<PagamentoEspecial> lstPagamentoEspecial = new ArrayList<>();
	
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
	
	@Autowired
	private ContextoBean contextoBean;


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

	public void geraRelatorio(){
		try {
			JasperPrint  relatorio =  imprimir(lstContasReceber, "receberconsulta.jrxml");
			//JasperPrintManager.printReport(print, true);
			JasperPrintManager.printReport(relatorio, true);
		} catch (JRException e) {
			e.printStackTrace();
		}
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
	
	//Direciona qual dialogo abrir no parcelas dataTable
	public void selecionarBusca(ContasReceber contas) {
		try {
			System.out.println("Contas -> " + contas.getIdContasReceber());
			System.out.println("Tipo -> " + contas.getTipoPagamento());
			if (contas.getTipoPagamento().equals("PAR")) {

				System.out.println("Escolheu Parcela Pagar");
				buscaParcelas(contas);
				DialogUtils.openDialog("mostraParcelas");

			} else if (contas.getTipoPagamento().equals("ESP")) {

				System.out.println("Entrou no Especial");
				buscaPagamentoEspecial(contas);
				DialogUtils.openDialog("mostraPagamentoEspecial");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//Direciona qual dialogo abrir no resumo de contas
		public void selecionarResumo(ContasReceber contas) {
				System.out.println("Contas -> " + contas.getIdContasReceber());
				System.out.println("Tipo -> " + contas.getTipoPagamento());
				if (contas.getTipoPagamento().equals("PAR")) {

					try {
						System.out.println("Escolheu Parcela Pagar");
						buscaParcelas(contas);
						DialogUtils.openDialog("mostraParcelaVisualizacao");
				
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				} else if (contas.getTipoPagamento().equals("ESP")) {

					try {
						System.out.println("Entrou no Especial");
						buscaPagamentoEspecial(contas);
						DialogUtils.openDialog("mostraPagamentoEspecialVisualizacao");

					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}else if(contasReceberModel.getTipoPagamento().equals("CC")) {
					try {
						System.out.println("Entrou no CC");
						buscaCredito(contas);
						DialogUtils.openDialog("mostrarCreditoVisualizacao");
					}catch (Exception e) {
						e.printStackTrace();
					}
				}else if(contasReceberModel.getTipoPagamento().equals("CD")) {
					try {
						System.out.println("Entrou no CD");
						buscaDebito(contas);
						DialogUtils.openDialog("mostrarDebitoVisualizacao");
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			
		}
	
	
	// dialogo de pagamento convencional
	public void buscaParcelas(ContasReceber contas) {
		try {
			StringBuilder str = new StringBuilder();
			System.out.println("ID CONTAS A RECEBER PARA A PARCELA " + contasReceberModel.getIdContasReceber());
			str.append(" from ParcelaPagar where contasReceber.idContasReceber = " + contas.getIdContasReceber());
			str.append(" order by idParcela ");
			lstParcelaPagarPendentes =  parcelaPagarController.findListByQueryDinamica(str.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// dialogo de pagamento especial
	public void buscaPagamentoEspecial(ContasReceber contas) {
		try {
			StringBuilder str = new StringBuilder();
			System.out.println("ID CONTAS A RECEBER PARA A PARCELA " + contasReceberModel.getIdContasReceber());
			str.append(" from PagamentoEspecial where contasReceber.idContasReceber = " + contas.getIdContasReceber());
			lstEspecialPagarPendentes = (List<PagamentoEspecial>) pagamentoEspecialController.findListByQueryDinamica(str.toString());
			System.out.println("LISTA PAGAMENTO ESPECIAL > " + lstEspecialPagarPendentes.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	List<ContasReceber> lstCredito = new ArrayList<ContasReceber>();
	public void buscaCredito(ContasReceber contas) {
		try {
			StringBuilder str = new StringBuilder();
			System.out.println("ID CONTAS A RECEBER PARA A PARCELA " + contasReceberModel.getIdContasReceber());
			str.append(" from ContasReceber where idContasReceber = " + contas.getIdContasReceber());
			lstCredito = (List<ContasReceber>) contasReceberController.findListByQueryDinamica(str.toString());
			System.out.println("LISTA CREDITO > " + lstCredito.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	List<ContasReceber> lstDebito = new ArrayList<ContasReceber>();
	public void buscaDebito(ContasReceber contas) {
		try {
			StringBuilder str = new StringBuilder();
			System.out.println("ID CONTAS A RECEBER PARA A PARCELA " + contasReceberModel.getIdContasReceber());
			str.append(" from ContasReceber where idContasReceber = " + contas.getIdContasReceber());
			lstDebito = (List<ContasReceber>) contasReceberController.findListByQueryDinamica(str.toString());
			System.out.println("LISTA DEBITO > " + lstDebito.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void verificaParcelasPendentes() {
	
		List<ParcelaPagar> total = new ArrayList<>();
		try {
			total = (List<ParcelaPagar>) parcelaPagarController
					.findListByQueryDinamica("from ParcelaPagar where contasReceber.idContasReceber = "	+ parcelaPagarModel.getContasReceber().getIdContasReceber());
			List<ParcelaPagar> paga = new ArrayList<>();

			paga = (List<ParcelaPagar>) parcelaPagarController
					.findListByQueryDinamica("from ParcelaPagar where contasReceber.idContasReceber = "	+ parcelaPagarModel.getContasReceber().getIdContasReceber() + " and situacao = 'PA' ");

			System.out.println("QUANTIDADE DE PARCELAS " + total.size());
			System.out.println("QUANTIDADE DE PARCELAS PAGAS " + paga.size());

			if (total.size() == paga.size()) {
				ContasReceber contaAtualModel = new ContasReceber();
				contaAtualModel = (ContasReceber) contasReceberController.findById(ContasReceber.class, parcelaPagarModel.getContasReceber().getIdContasReceber());

				contaAtualModel.setStatus("PA");
				contaAtualModel.setDataPagamento(new Date());
				contasReceberController.merge(contaAtualModel);
			}
		} catch (Exception e) {
			System.out.println("Erro ao verificar parcelas ");
			e.printStackTrace();
		}

	}

	
	public void verificaPagamentoEspecialPendentes() {
		List<PagamentoEspecial> total = new ArrayList<>();
		try {
			total = (List<PagamentoEspecial>) pagamentoEspecialController
					.findListByQueryDinamica("from PagamentoEspecial where contasReceber.idContasReceber = " + pagamentoEspecialModel.getContasReceber().getIdContasReceber());
			List<PagamentoEspecial> paga = new ArrayList<>();

			paga = (List<PagamentoEspecial>) pagamentoEspecialController
					.findListByQueryDinamica("from PagamentoEspecial where contasReceber.idContasReceber = " + pagamentoEspecialModel.getContasReceber().getIdContasReceber() + " and situacao = 'PA' ");

			System.out.println("QUANTIDADE DE PARCELAS " + total.size());
			System.out.println("QUANTIDADE DE PARCELAS PAGAS " + paga.size());

			if (total.size() == paga.size()) {
				ContasReceber contaAtualModel = new ContasReceber();
				contaAtualModel = (ContasReceber) contasReceberController.findById(ContasReceber.class,pagamentoEspecialModel.getContasReceber().getIdContasReceber());

				contaAtualModel.setStatus("PA");
				contaAtualModel.setDataPagamento(new Date());
				contasReceberController.merge(contaAtualModel);
			}
		} catch (Exception e) {
			System.out.println("Erro ao verificar parcelas ");
			e.printStackTrace();
		}

	}
	
	public void estornarParcelas() {
		
		try {
			caixaController.setExecuteParam("delete from caixa where idParcela = " + parcelaPagarModel.getIdParcela() );

			ContasReceber contaAtual = new ContasReceber();
			contaAtual = contasReceberController.findByPorId(ContasReceber.class, parcelaPagarModel.getContasReceber().getIdContasReceber());
			
			List<ParcelaPagar> lst = new ArrayList<>();
			
			lst = parcelaPagarController.findListByQueryDinamica(" from ParcelaPagar where contasReceber.idContasReceber = "+contaAtual.getIdContasReceber());
			
			int contLista = lst.size();
			int contAtual = 0;
			
			for (ParcelaPagar pa : lst) {// se todos foram pendentes troca o status da conta
				contAtual = 0;
				if(pa.getSituacao().equals("P")) {
					contAtual++;
				}
			}

			if(contAtual == contLista ) {
				System.out.println("Duas parcelas pendentes da conta atual -----");
				contaAtual.setStatus("P");
				contaAtual.setDataPagamento(null);
				contasReceberController.merge(contaAtual);
				
			}
			
			parcelaPagarModel.setSituacao("P");
			parcelaPagarModel.setDataPagamento(null);
			parcelaPagarController.merge(parcelaPagarModel);
			addMsg("Parcela Estornada com sucesso!");
			parcelaPagarModel = null;
			busca();
		} catch (Exception e) {
			error();
			e.printStackTrace();
		}
	}
	
	public void estornoEspecial() { // verificar os dois estornos 
		try {
			caixaController.setExecuteParam("delete from Caixa where idPagamentoEspecial = " + pagamentoEspecialModel.getIdPagamentoEspecial() + " ");
			
			ContasReceber contaAtual = new ContasReceber();
			contaAtual = contasReceberController.findByPorId(ContasReceber.class, pagamentoEspecialModel.getContasReceber().getIdContasReceber());
			
			List<PagamentoEspecial> lst = new ArrayList<>();
			
			lst = pagamentoEspecialController.findListByQueryDinamica(" from PagamentoEspecial where contasReceber.idContasReceber = "+contaAtual.getIdContasReceber());
			
			int contLista = lst.size();
			int contAtual = 0;
			
			for (PagamentoEspecial pa : lst) {// se todos foram pendentes troca o status da conta
				contAtual = 0;
				if(pa.getSituacao().equals("P")) {
					contAtual++;
				}
			}

			if(contAtual == contLista ) {
				System.out.println("Duas parcelas pendentes da conta atual -----");
				contaAtual.setStatus("P");
				contaAtual.setDataPagamento(null);
				contasReceberController.merge(contaAtual);
				
			}
			
			pagamentoEspecialModel.setSituacao("P");
			pagamentoEspecialModel.setDataPagamento(null);
			
			pagamentoEspecialController.merge(pagamentoEspecialModel);
			addMsg("Estornado com sucesso!");
			pagamentoEspecialModel = null;
			busca();
		} catch (Exception e) {
			error();
			e.printStackTrace();
		}
	}

	public void pagarEspecial() {
		try {
		
			ContasReceber conta = (ContasReceber) contasReceberController.findById(ContasReceber.class, pagamentoEspecialModel.getContasReceber().getIdContasReceber());
			
			pagamentoEspecialModel.setSituacao("PA");
			pagamentoEspecialModel.setDataPagamento(new Date());

			caixaModel.setPaciente(new Paciente(conta.getPaciente().getIdPaciente()));
			caixaModel.setContasReceber(new ContasReceber(pagamentoEspecialModel.getContasReceber().getIdContasReceber()));
			caixaModel.setPagamentoEspecial(new PagamentoEspecial(pagamentoEspecialModel.getIdPagamentoEspecial()));
			caixaModel.setDataLancamento(new Date());
			caixaModel.setTipo("CR");
			caixaModel.setValorInserido(pagamentoEspecialModel.getValorBruto());
			caixaController.merge(caixaModel);

			pagamentoEspecialController.merge(pagamentoEspecialModel);
			addMsg("Parcela Paga com sucesso!");
			verificaPagamentoEspecialPendentes();
			pagamentoEspecialModel = null;
			busca();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Problema ao pagar especial");
		}
	}

	public void pagarParcela() {
		try {
			Long usuarioSessaoId = 0L;

			try {
				usuarioSessaoId = contextoBean.getEntidadeLogada().getIdLogin();
			} catch (Exception e) {
				System.out.println("Erro ao recuperar da sessao");
				e.printStackTrace();
				e.getMessage();
			}
			
			
			ContasReceber conta = (ContasReceber) contasReceberController.findById(ContasReceber.class, parcelaPagarModel.getContasReceber().getIdContasReceber());
			// Muda a situação da parcela para paga
			parcelaPagarModel.setSituacao("PA");
			parcelaPagarModel.setDataPagamento(new Date());
			parcelaPagarModel.setLogin(new Login(usuarioSessaoId));
			
			// adiciona valor da parcela ao CAIXA DA EMPRESA
			// ----------------------------------------------------------
			caixaModel.setPaciente(new Paciente(conta.getPaciente().getIdPaciente()));
			caixaModel.setContasReceber(new ContasReceber(parcelaPagarModel.getContasReceber().getIdContasReceber()));
			caixaModel.setParcelaPagar(new ParcelaPagar(parcelaPagarModel.getIdParcela()));
			caixaModel.setDataLancamento(new Date());
			caixaModel.setTipo("CR");
			caixaModel.setValorInserido(parcelaPagarModel.getValorBruto());
			caixaController.merge(caixaModel);
			
			caixaModel = new Caixa();
			// adiciona valor da parcela ao CAIXA DA EMPRESA
			// ----------------------------------------------------------

			// salva
			parcelaPagarController.merge(parcelaPagarModel);
			addMsg("Parcela Paga com sucesso!");
			verificaParcelasPendentes();
			parcelaPagarModel = null;
			busca();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Problema ao pagar parcela");
		}
		
		
	}

	public void recalcularDuasFormas() {
		try {
			Double restante = 0.0D;
			if (getValorUm() > contasReceberModel.getValorComDesconto()) {
				addMsg("Valor Digitado é maior que a conta atual");
				setValorUm(0.0);
				System.out.println("->" +getValorUm());
				setValorDois(0.0);
			} else {
				restante = contasReceberModel.getValorComDesconto() - getValorUm();
				System.out.println("RESTANTE <>> " + restante);
				setValorDois(restante);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void fazerDesconto() {
		try {
			
			InputStream input = new FileInputStream("C:\\Users\\Humberto\\Documents\\clinicaH\\clinica\\clinica\\src\\ini.properties"); // desconto atual esta no ini.property
			Properties prop = new Properties();
	        prop.load(input);
	        Double desconto = Double.parseDouble(String.valueOf(prop.get("desconto")));
			
			System.out.println(" O Valor da Consulta " + contasReceberModel.getValorConsulta());
			contasReceberModel.setValorComDesconto(contasReceberModel.getValorConsulta());
			
			Double maximoDesconto =  contasReceberModel.getValorConsulta() * (desconto / 100) ;
			
			System.out.println(" O Valor do Desconto" + contasReceberModel.getValorDesconto());
			
			System.out.println(desconto);
			System.out.println(maximoDesconto);
			if(contasReceberModel.getValorDesconto() > maximoDesconto ) {
				
					addMsg("Impossivel atribuir desconto maior que R$"+ maximoDesconto);
					contasReceberModel.setValorDesconto(0.0);
			
			}else {
				
				Double total = contasReceberModel.getValorConsulta() - contasReceberModel.getValorDesconto();
				System.out.println("ENTRADA >"+contasReceberModel.getValorEntrada());
				System.out.println("VALOR COM DESCONTO >"+total);
				
					if(contasReceberModel.getValorEntrada() > total) {
					
						 addMsg("Impossivel atribuir entrada! ");
						 contasReceberModel.setValorEntrada(0.0);
					
					}else {
						
						Double totalFinal = total - contasReceberModel.getValorEntrada();
						contasReceberModel.setValorComDesconto(totalFinal);
					
					}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void limpar() {
		contasReceberModel = new ContasReceber();
	}

	/**
	 * Verifica se há somente parcelas pendentes, se houver deixa estornar
	 */
	public void relancarContasReceberAtendente() {
		
		try {
			List<ParcelaPagar> total = new ArrayList<>();
			total = (List<ParcelaPagar>) parcelaPagarController
					.findListByQueryDinamica("from ParcelaPagar where contasReceber.idContasReceber = "	+ contasReceberModel.getIdContasReceber());

			List<ParcelaPagar> pendente = new ArrayList<>();
			pendente = (List<ParcelaPagar>) parcelaPagarController
					.findListByQueryDinamica("from ParcelaPagar where contasReceber.idContasReceber = "	+ contasReceberModel.getIdContasReceber() + " and situacao = 'P' ");

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
				parcelaPagarController.setExecuteParam("delete from parcelapagar where idcontasreceber = " + contasReceberModel.getIdContasReceber() + " and situacao = 'P' ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	public void simularParcelasCartao(Long idForma) {
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
		try {
			
			if(contasReceberModel.getFormaPagamento().getSiglaPagamento().equals("AV") || contasReceberModel.getFormaPagamento().getSiglaPagamento().equals("CD")) {
				System.out.println("fazerValorAvista() - > Entrou no if ");
				Double valorAvista = contasReceberModel.getValorComDesconto();
				System.out.println("VALOR A VISTA: " + valorAvista);
				contasReceberModel.setValorParcelado(valorAvista);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Lanca as parcelas que o atendente selecionou
	 *
	 */
	public void fazerPagamento() {
		Long idContaReceber = contasReceberModel.getIdContasReceber();
		
		try {
		List<ParcelaPagar> lstPagamento = new ArrayList<ParcelaPagar>();
		lstPagamento = parcelaPagarController.findListByQueryDinamica("from ParcelaPagar where contasReceber.idContasReceber = " + idContaReceber);

			for (ParcelaPagar pa : lstPagamento) {
				if (pa.getContasReceber().getIdContasReceber() == idContaReceber
																	&& contasReceberModel.getStatus().equals("L") // Se o status da conta for Lançadp
																	&& pa.getSituacao().equals("P")) { // Se as parcelas estiverem pendentes
							parcelaPagarController.delete(pa);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} // Deleta as parcelas e relançam

		try {
			parcelaPagarController.setExecuteParam("delete from parcelapagar where idcontasreceber = " + contasReceberModel.getIdContasReceber() );

			if(contasReceberModel.getValorEntrada() != null || contasReceberModel.getValorEntrada() > 0.00) {
				System.out.println("ENTRADA --------------------------------");

				parcelaPagarModel = new ParcelaPagar();
				parcelaPagarModel.setNumeroParcela(0); // igual a 4
				parcelaPagarModel.setLogin(contasReceberModel.getLogin());
				parcelaPagarModel.setDataVencimento(contasReceberModel.getDataVencimentoContasReceber());
				System.out.println("VALOR D ENTRADA -> -> "+ contasReceberModel.getValorEntrada());
				parcelaPagarModel.setValorBruto(contasReceberModel.getValorEntrada());
				parcelaPagarModel.setSituacao("PA");
				parcelaPagarModel.setContasReceber(new ContasReceber(contasReceberModel.getIdContasReceber()));
				
				parcelaPagarModel = parcelaPagarController.merge(parcelaPagarModel);
			
				caixaModel.setPaciente(new Paciente(contasReceberModel.getPaciente().getIdPaciente()));
				caixaModel.setContasReceber(new ContasReceber(parcelaPagarModel.getContasReceber().getIdContasReceber()));
				caixaModel.setParcelaPagar(new ParcelaPagar(parcelaPagarModel.getIdParcela()));
				caixaModel.setDataLancamento(new Date());
				caixaModel.setTipo("CR");
				caixaModel.setValorInserido(parcelaPagarModel.getValorBruto());
				caixaController.merge(caixaModel);
				
				caixaModel = new Caixa();
			}
			
			if (parcelas == 1) {
				parcelaPagarModel = new ParcelaPagar();
				parcelaPagarModel.setNumeroParcela(1);
				parcelaPagarModel.setLogin(contasReceberModel.getLogin());
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
					parcelaPagarModel.setLogin(contasReceberModel.getLogin());
					parcelaPagarModel.setValorDesconto(contasReceberModel.getValorDesconto());

					if (i == 1) {
						parcelaPagarModel.setDataVencimento(contasReceberModel.getDataVencimentoContasReceber());
					}
					if (i == 2) {
						Date dataSegundaParcela = DatasUtils.addMonth(contasReceberModel.getDataVencimentoContasReceber(), 1);
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
					parcelaPagarModel.setLogin(contasReceberModel.getLogin());
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
				
				
				contasReceberModel.setStatus("L"); // Seta como conta já lançada
				contasReceberModel.setTipoPagamento("PAR"); // Seta como conta PARCELADA - PAR
				contasReceberModel.setDataLançamento(new Date());
				contasReceberController.merge(contasReceberModel);
				limpar();
			}
		} catch (Exception e) {
			System.out.println("Erro ao fazer pagamentos");
			e.printStackTrace();
		}
		busca();
	}

	public void calculaVencimento() {
		try {
			Date vencimento = getVencimentoDois();
			
			if(vencimento.before(getVencimentoUm())) {
				System.out.println("Vencimento Dois menor que vencimento 1");
				setVencimentoDois(getVencimentoUm());
			}else
			{
				System.out.println("Vencimento Dois maior que o 1");
			}
			
			//setVencimentoDois(vencimento);
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
		}
	}

	public void fazerPagamentoDuasFormas() {
		Long idContaReceber = contasReceberModel.getIdContasReceber();
		try {
			System.out.println("Entrou no fazerPagamentoDuasFormas() ");
			List<PagamentoEspecial> lstPagamento = new ArrayList<PagamentoEspecial>();
			lstPagamento = pagamentoEspecialController.findListByQueryDinamica("from PagamentoEspecial where contasReceber.idContasReceber = " + idContaReceber);

			for (PagamentoEspecial pa : lstPagamento) {
				if (pa.getContasReceber().getIdContasReceber() == idContaReceber
						&& contasReceberModel.getStatus().equals("L") // Se o status da conta for Lançadp
						&& pa.getSituacao().equals("P")) { // Se as parcelas estiverem pendentes
					pagamentoEspecialController.delete(pa); // Deleta as parcelas e relançam
				}
			}
			PagamentoEspecial pgUm = new PagamentoEspecial();
			PagamentoEspecial pgDois = new PagamentoEspecial();
			PagamentoEspecial pgEntrada = new PagamentoEspecial();
			
			System.out.println("------------------------->" +contasReceberModel.getValorEntrada());
		
			if(contasReceberModel.getValorEntrada() != null || contasReceberModel.getValorEntrada() > 0.00) {
				pgEntrada.setValorBruto(contasReceberModel.getValorEntrada());
				pgEntrada.setDataVencimento(new Date());
				pgEntrada.setDataPagamento(new Date());
				pgEntrada.setDataLancamento(new Date());
				pgEntrada.setContasReceber(new ContasReceber(idContaReceber));
				pgEntrada.setSituacao("PA");
				
				pgEntrada = pagamentoEspecialController.merge(pgEntrada);
				
				
				caixaModel.setPaciente(new Paciente(contasReceberModel.getPaciente().getIdPaciente()));
				caixaModel.setContasReceber(new ContasReceber(pgEntrada.getContasReceber().getIdContasReceber()));
				caixaModel.setPagamentoEspecial(new PagamentoEspecial(pgEntrada.getIdPagamentoEspecial()));
				caixaModel.setDataLancamento(new Date());
				caixaModel.setTipo("CR");
				caixaModel.setValorInserido(pgEntrada.getValorBruto());
				caixaController.merge(caixaModel);
				
				caixaModel = new Caixa();
				pgEntrada = new PagamentoEspecial();
			}

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
			
			contasReceberModel.setTipoPagamento("ESP");// Seta como conta ESPECIAL - ESP
			contasReceberController.merge(contasReceberModel);
			addMsg("Operação realizada com sucesso");
			lstPagamentoEspecial = new ArrayList<>();
		} catch (Exception e) {
			System.out.println("Erro ao fazer pagamento duas formas");
			e.printStackTrace();
		}
	}

	/**
	 * Edita uma lançamento de parcelas já feito ao usuario e o STATUS É PENDENTE
	 * 
	 */
	public void editarPagamento() {
		Long idContaReceber = contasReceberModel.getIdContasReceber();
		if (contasReceberModel.getMaisForma().equals("N")) {

			try { // deleta os lancamentos do pagamento especial
				if (contasReceberModel.getStatus().equals("L")) {
					pagamentoEspecialController.setExecuteParam("delete from pagamentoespecial where idcontasreceber = " + idContaReceber + " and situacao = 'P' ");
				}
			} catch (Exception e) {
				System.out.println("Erro ao Deletar Pagamento Especial");
				e.printStackTrace();
			}

			try { // deleta os parcelas/pagar do pagamento parcelado
				if (contasReceberModel.getStatus().equals("L")) {
					parcelaPagarController.setExecuteParam("delete from parcelapagar where idcontasreceber = "+ idContaReceber + " and situacao = 'P' ");
				}
			} catch (Exception e) {
				System.out.println("Erro ao Deletar Pagamento Parcelado");
				e.printStackTrace();
			}
			
			
			
			System.out.println("SIGLA ->"+ contasReceberModel.getFormaPagamento().getSiglaPagamento());
			
			if (contasReceberModel.getFormaPagamento().getSiglaPagamento().equals("CRE")) {   // CREDIARIO
				try {
					fazerPagamento();
					
					addMsg("Operação Realizada Com Sucesso!");
					busca();
					DialogUtils.updateForm("formPai");
					DialogUtils.closeDialog("relancarPagamento");
					return;
				} catch (Exception e) {
					System.out.println("Erro ao salvar parcelas!");
					e.printStackTrace();
				}
			} else if (contasReceberModel.getFormaPagamento().getSiglaPagamento().equals("CC")) { // CARTAO DE CREDITO
					System.out.println("CARTAO DE CREDITO");
					try {
						System.out.println("QUANTIDADE DE PARCELAS ->" + contasReceberModel.getQuantidadeParcelas());
						contasReceberModel.setValorParcelado(contasReceberModel.getValorComDesconto());
						contasReceberModel.setStatus("L");
						contasReceberModel.setDataLançamento(new Date());
						contasReceberModel.setTipoPagamento("CC");
						contasReceberModel = contasReceberController.merge(contasReceberModel);
						addMsg("Operação Realizada Com Sucesso!");
						
						addMsg("Operação Realizada Com Sucesso!");
						busca();
						DialogUtils.updateForm("formPai");
						DialogUtils.closeDialog("relancarPagamento");
						return;
					} catch (Exception e) {
						addMsg("Houve um erro ao salvar!");
						e.getMessage();
						e.printStackTrace();
					}
					System.out.println("SIGLA DPS DO CC" + contasReceberModel.getFormaPagamento().getSiglaPagamento());
				} else if (contasReceberModel.getFormaPagamento().getSiglaPagamento().equals("CD")) { // CARTAO DE DEBITO
					try {
						System.out.println("CARTAO DE DEBITO");
						contasReceberModel.setStatus("L");
						contasReceberModel.setDataLançamento(new Date());
						contasReceberModel.setTipoPagamento("CD");
						contasReceberModel = contasReceberController.merge(contasReceberModel);
						addMsg("Operação Realizada Com Sucesso!");
						
						addMsg("Operação Realizada Com Sucesso!");
						busca();
						DialogUtils.updateForm("formPai");
						DialogUtils.closeDialog("relancarPagamento");
						return;
					}catch (Exception e) {
						e.printStackTrace();
					}	
				} else if (contasReceberModel.getFormaPagamento().getSiglaPagamento().equals("AV")) { // À VISTA
					System.out.println("À VISTA");
					try {
						contasReceberModel.setStatus("L");
						contasReceberModel.setDataLançamento(new Date());
						contasReceberModel.setTipoPagamento("AV");
						contasReceberModel = contasReceberController.merge(contasReceberModel);
						addMsg("Operação Realizada Com Sucesso!");
						
						busca();
						DialogUtils.updateForm("formPai");
						DialogUtils.closeDialog("relancarPagamento");
						return;
					} catch (Exception e) {
						addMsg("Houve um erro ao salvar!");
						e.getMessage();
						e.printStackTrace();
					}
				}

			//DialogUtils.updateForm("formPai");
		
		} else { // se for igual a 'S'
		
			System.out.println("Entrou no igual a S ");

			try { // deleta os lancamentos do pagamento especial
				if (contasReceberModel.getStatus().equals("L")) {
					pagamentoEspecialController.setExecuteParam("delete from pagamentoespecial where idcontasreceber = " + idContaReceber + " and situacao = 'P' ");
				}
			} catch (Exception e) {
				System.out.println("Erro ao Deletar Pagamento Especial");
				e.printStackTrace();
			}

			try { // deleta os parcelas/pagar do pagamento parcelado
				if (contasReceberModel.getStatus().equals("L")) {
					parcelaPagarController.setExecuteParam("delete from parcelapagar where idcontasreceber = " + idContaReceber + " and situacao = 'P' ");
				}
			} catch (Exception e) {
				System.out.println("Erro ao Deletar Pagamento Parcelado");
				e.printStackTrace();
			}

			try {
				
				fazerPagamentoDuasFormas();
				//sucesso();
				
				busca();
				DialogUtils.updateForm("formPai");
			} catch (Exception e) {
				System.out.println("Erro ao fazer Pagamento Duas Formas");
				e.printStackTrace();
			}
		}
		
		DialogUtils.closeDialog("relancarPagamento");
		//DialogUtils.closeDialog("relancarPagamentoEditar");

	}

	// CC - CD - AV
	public void pagamentoUnico() {
		try {
			caixaModel.setPaciente(new Paciente(contasReceberModel.getPaciente().getIdPaciente()));
			caixaModel.setContasReceber(new ContasReceber(contasReceberModel.getIdContasReceber()));
			caixaModel.setDataLancamento(new Date());
			caixaModel.setTipo("CR");
			caixaModel.setValorInserido(contasReceberModel.getValorEntrada() + contasReceberModel.getValorComDesconto());
			//caixaModel.setValorInserido(contasReceberModel.getValorConsulta());
			caixaController.merge(caixaModel);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			contasReceberModel.setStatus("PA");
			contasReceberModel.setDataPagamento(new Date());
			contasReceberController.merge(contasReceberModel);
			addMsg("Operação Realizado com sucesso!");
		} catch (Exception e) {
			addMsg("Ocorreu um erro ao salvar");
			e.printStackTrace();
		}
		busca();
		DialogUtils.updateForm("formPai");
	}

	public void limparPedido() {
		new ContasReceberBean();
		setValorUm(0.0);
		setValorDois(0.0);
	}
	
	public void estornarParcelasPagas() {

		parcelaPagarModel.setSituacao("P");
		parcelaPagarModel.setDataPagamento(null);
		try {
			caixaController.getListSQLDinamica("delete from caixa where idContasReceber = " + parcelaPagarModel.getContasReceber().getIdContasReceber() + " and idParcela = " + parcelaPagarModel.getIdParcela());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			ContasReceber contaAtual = new ContasReceber();
			contaAtual = contasReceberController.findByPorId(ContasReceber.class, parcelaPagarModel.getContasReceber().getIdContasReceber());
			
			if (contaAtual.getStatus().equals("PA")) {
				contaAtual.setStatus("L");
			}
			
			contaAtual.setDataPagamento(null);
			contasReceberController.merge(contaAtual);
			parcelaPagarController.merge(parcelaPagarModel);
			addMsg("Parcela estornada com sucesso!");
			busca();
		} catch (Exception e) {
			error();
			e.printStackTrace();
		}
	}
	
	/**
	 * Retira as parcelas pendentes no Usuario Adm
	 */
	public void relancarContasReceberAdm() {
		try {
			contasReceberModel.setStatus("P");
			contasReceberController.merge(contasReceberModel);
		} catch (Exception e1) {
			System.out.println("erro ao gravar");
			e1.printStackTrace();
		}
		try {
			parcelaPagarController.getListSQLDinamica("delete from parcelapagar where idcontasreceber = " + contasReceberModel.getIdContasReceber() + " and situacao = 'P' ");
		} catch (Exception e) {
			System.out.println("erro ao deletar parcelas no estorno");
			e.printStackTrace();
		}
	}


	@Override
	public String save() {

		try {
			contasReceberModel = contasReceberController.merge(contasReceberModel);
			return "";
		} catch (Exception e) {
			e.printStackTrace();
		}
		contasReceberModel = new ContasReceber();
		return "";
	}

	@Override
	public void saveNotReturn() {
		try {
			contasReceberModel = contasReceberController.merge(contasReceberModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		contasReceberModel = new ContasReceber();
		sucesso();
		busca();
	}

	@Override
	public void saveEdit() {
		saveNotReturn();
	}

	@Override
	public String novo()  {
		setarVariaveisNulas();
		return getUrl();
	}

	@Override
	public void setarVariaveisNulas(){
		contasReceberModel = new ContasReceber();
	}

	@Override
	public String editar() {
		return getUrl();
	}

	@Override
	public void excluir() {
		try {
			contasReceberModel = (ContasReceber) contasReceberController.getSession().get(ContasReceber.class, contasReceberModel.getIdContasReceber());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			contasReceberController.delete(contasReceberModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		contasReceberModel = new ContasReceber();
		sucesso();
		busca();
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


	@Override
	public String redirecionarFindEntidade() {
		setarVariaveisNulas();
		mudaEstadoPendencia();
		return getUrlFind();
	}

	@Override
	public void consultarEntidade() {
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

	boolean verificaParcelaPaga ;
	public void onRowSelect(SelectEvent event) {
		contasReceberModel = (ContasReceber) event.getObject();
		
		List<ParcelaPagar> auxParcela = new ArrayList<>();
		List<PagamentoEspecial> auxEspecial = new ArrayList<>();
		
		try {
		if(contasReceberModel.getTipoPagamento().equals("ESP")) {
				auxEspecial = pagamentoEspecialController.findListByQueryDinamica(" from PagamentoEspecial where contasReceber.idContasReceber =   " + contasReceberModel.getIdContasReceber() + " and situacao = 'PA'" );
				
				if(auxEspecial.size() > 0) {
					verificaParcelaPaga = true;
				}else {
					verificaParcelaPaga = false;
				}
		} else if(contasReceberModel.getTipoPagamento().equals("PAR")){

				auxParcela = parcelaPagarController.findListByQueryDinamica(" from ParcelaPagar where contasReceber.idContasReceber =   " + contasReceberModel.getIdContasReceber() + " and situacao = 'PA'" );
					
					if(auxParcela.size() > 0) {
						verificaParcelaPaga = true;
					}else {
						verificaParcelaPaga = false;
					}
		}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("VERIFICA -> "+verificaParcelaPaga);
	}

	public void onRowSelectParcelas(SelectEvent event) {
		parcelaPagarModel = (ParcelaPagar) event.getObject();
	}

	public void onRowSelectEspecial(SelectEvent event) {
		pagamentoEspecialModel = (PagamentoEspecial) event.getObject();
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


	public ContextoBean getContextoBean() {
		return contextoBean;
	}

	public void setContextoBean(ContextoBean contextoBean) {
		this.contextoBean = contextoBean;
	}

	public boolean isVerificaParcelaPaga() {
		return verificaParcelaPaga;
	}

	public void setVerificaParcelaPaga(boolean verificaParcelaPaga) {
		this.verificaParcelaPaga = verificaParcelaPaga;
	}

	public List<ContasReceber> getLstCredito() {
		return lstCredito;
	}

	public void setLstCredito(List<ContasReceber> lstCredito) {
		this.lstCredito = lstCredito;
	}

	public List<ContasReceber> getLstDebito() {
		return lstDebito;
	}

	public void setLstDebito(List<ContasReceber> lstDebito) {
		this.lstDebito = lstDebito;
	}
}



/*
 * <p:dialog widgetVar="relancarPagamentoEditar" id="relancarPagamentoEditar"
 * modal="true" resizable="false" height="400" width="900" closable="false"
 * responsive="true" dynamic="true" appendToBody="@body"
 * header="Editar Lançamento"> <div class="ui-fluid"> <h:form
 * id="relancarFormEditar"> <div class="ui-g"> <p:outputPanel
 * styleClass="painelPaciente"> <div class="ui-g"> <div class="ui-g-12 ui-lg-5">
 * <h:outputLabel value="Paciente"
 * style="font-size: large !important;color:white" /> <br /> <h:outputLabel
 * style="font-size: large !important; color:white;"
 * value="#{contasReceberBean.contasReceberModel.paciente.pessoa.pessoaNome}" />
 * </div>
 * 
 * <div class="ui-g-12 ui-lg-3"> <h:outputLabel value="Consulta"
 * style="font-size: large !important;color:white" /> <br /> <p:outputLabel
 * required="true" style="font-size: large !important; color:white"
 * value="R$ #{contasReceberBean.contasReceberModel.valorConsulta}" /> </div>
 * 
 * <div class="ui-g-12 ui-lg-4"> <h:outputLabel value="Total Desconto/Entrada"
 * style="font-size: large !important; color:white" /> <br /> <p:outputLabel
 * id="valorComDesconto" style="font-size: large !important; color:white"
 * value="R$ #{contasReceberBean.contasReceberModel.valorComDesconto}">
 * <f:convertNumber maxFractionDigits="2" minFractionDigits="2" locale="pt_BR"
 * /> </p:outputLabel> </div> </div> </p:outputPanel> <div class="ui-g-12"> <div
 * class="card card-w-title"> <div class="ui-g-12 ui-lg-12"> <div class="ui-g">
 * <div class="ui-g-12 ui-lg-2">
 * 
 * <h:outputLabel value="Desconto" /> <br /> <div class="ui-inputgroup">
 * <p:commandButton value="R$" /> <p:inputNumber decimalPlaces="2" symbol="R$"
 * symbolPosition="p" emptyValue="0" style="font-size: large !important"
 * value="#{contasReceberBean.contasReceberModel.valorDesconto}"> <p:ajax
 * event="change" update="valorComDesconto par @this"
 * listener="#{contasReceberBean.fazerDesconto}" /> </p:inputNumber> </div>
 * </div>
 * 
 * <div class="ui-g-12 ui-lg-2"> <h:outputLabel value="Entrada" /> <br />
 * <p:inputNumber decimalPlaces="2" symbol="R$" symbolPosition="p"
 * emptyValue="0" style="font-size: large !important"
 * value="#{contasReceberBean.contasReceberModel.valorEntrada}"> <p:ajax
 * event="change" update="valorComDesconto,par"
 * listener="#{contasReceberBean.fazerDesconto}" />
 * 
 * </p:inputNumber> </div>
 * 
 * <div class="ui-g-12 ui-lg-6 ui-sm-12"> <h:outputLabel
 * value="Duas Formas de Pagamento?" /> <p:selectOneRadio
 * value="#{contasReceberBean.contasReceberModel.maisForma}"> <f:selectItem
 * itemLabel="Sim" itemValue="S" /> <f:selectItem itemLabel="Não" itemValue="N"
 * /> <p:ajax event="change" update="relancarFormEditar" /> </p:selectOneRadio>
 * </div>
 * 
 * <ui:fragment
 * rendered="#{contasReceberBean.contasReceberModel.maisForma eq 'N'}"> <div
 * class="ui-g"> <div class="ui-g-12 ui-lg-6 ui-sm-12"> <h:outputLabel
 * value="Forma de Pagamento" /> <br /> <p:autoComplete id="pagamento"
 * completeMethod="#{auxController.completeFormaPagamento}"
 * forceSelection="true" converter="entityConverter" var="a"
 * itemLabel="#{a.nomePagamento}" itemValue="#{a}"
 * value="#{contasReceberBean.contasReceberModel.formaPagamento}"
 * minQueryLength="2" dropdown="true" maxResults="30" required="false"
 * requiredMessage="Informe uma uma forma de pagamento"> <p:column id="p">
 * <h:outputText value="#{a.nomePagamento.toUpperCase()}" /> </p:column> <p:ajax
 * event="itemSelect" update="parcelass ,par"
 * listener="#{contasReceberBean.fazerValorAvista}" /> </p:autoComplete> </div>
 * <div class="ui-g-12 ui-lg-2"> <p:outputPanel id="parcelass"> <h:outputLabel
 * value="Parcela(s)"
 * rendered="#{contasReceberBean.contasReceberModel.formaPagamento.idFormaPagamento eq '1'}"
 * /> <br /> <p:selectOneMenu style="height: 35px"
 * value="#{contasReceberBean.contasReceberModel.quantidadeParcelas}"
 * rendered="#{contasReceberBean.contasReceberModel.formaPagamento.idFormaPagamento eq '1'}"
 * > <f:selectItem itemLabel="Selecione" noSelectionOption="true" />
 * <f:selectItem itemLabel="1x" itemValue="1" /> <f:selectItem itemLabel="2x"
 * itemValue="2" /> <f:selectItem itemLabel="3x" itemValue="3" /> <p:ajax
 * event="change" update="par" listener=
 * "#{contasReceberBean.fazerParcelas(contasReceberBean.contasReceberModel.quantidadeParcelas)}"
 * /> </p:selectOneMenu> </p:outputPanel> </div> <div class="ui-g-12 ui-lg-2">
 * <h:outputLabel value="Vencimento" /> <p:calendar
 * value="#{contasReceberBean.contasReceberModel.dataVencimentoContasReceber}"
 * mindate="today" /> </div> <div class="ui-g-12 ui-lg-2"> <h:outputLabel
 * value="Valor" /> <p:inputNumber id="par" disabled="true" decimalPlaces="2"
 * symbol="R$" symbolPosition="p"
 * value="#{contasReceberBean.contasReceberModel.valorParcelado}"
 * style="font-size: large !important;" /> </div> </div> </ui:fragment>
 * <ui:fragment
 * rendered="#{contasReceberBean.contasReceberModel.maisForma eq 'S'}">
 * <h:panelGroup id="group"> <div class="ui-g"> <div
 * class="ui-g-12 ui-lg-4 ui-sm-12"> <h:outputLabel value="Forma de Pagamento"
 * /> <p:autoComplete
 * completeMethod="#{auxController.completeFormaPagamentoSemCrediario}"
 * forceSelection="true" converter="entityConverter" var="a"
 * itemLabel="#{a.nomePagamento}" itemValue="#{a}"
 * value="#{contasReceberBean.formaUm}" minQueryLength="2" dropdown="true"
 * maxResults="30" required="false"
 * requiredMessage="Informe uma uma forma de pagamento"> </p:autoComplete>
 * </div>
 * 
 * <div class="ui-g-12 ui-lg-3 ui-sm-12"> <h:outputLabel value="Valor" />
 * <p:inputNumber value="#{contasReceberBean.valorUm}" symbol="R$"> <p:ajax
 * update="@this" event="blur" process="@this"
 * listener="#{contasReceberBean.recalcularDuasFormas}" /> </p:inputNumber>
 * </div>
 * 
 * <div class="ui-g-12 ui-lg-2 ui-sm-12"> <h:outputLabel value="Vencimento" />
 * <p:calendar value="#{contasReceberBean.vencimentoUm}" mindate="today"
 * pattern="dd/MM/yyyy"> <p:ajax event="change"/> <p:ajax event="dateSelect" />
 * </p:calendar> </div> </div> <div class="ui-g"> <div
 * class="ui-g-12 ui-lg-4 ui-sm-12"> <h:outputLabel value="Forma de Pagamento"
 * /> <p:autoComplete
 * completeMethod="#{auxController.completeFormaPagamentoSemCrediario}"
 * forceSelection="true" converter="entityConverter" var="a"
 * itemLabel="#{a.nomePagamento}" itemValue="#{a}"
 * value="#{contasReceberBean.formaDois}" minQueryLength="2" dropdown="true"
 * maxResults="30" required="false"
 * requiredMessage="Informe uma uma forma de pagamento"> </p:autoComplete>
 * </div>
 * 
 * <div class="ui-g-12 ui-lg-3 ui-sm-12"> <h:outputLabel value="Valor" />
 * <p:inputNumber id="valorDois" disabled="true"
 * value="#{contasReceberBean.valorDois}" symbol="R$"> </p:inputNumber> </div>
 * 
 * <div class="ui-g-12 ui-lg-2 ui-sm-12"> <h:outputLabel value="Vencimento" />
 * <p:calendar value="#{contasReceberBean.vencimentoDois}"
 * mindate="#{contasReceberBean.vencimentoUm}" pattern="dd/MM/yyyy"> <p:ajax
 * event="change" listener="#{contasReceber.calculaVencimento}" update="@this"/>
 * <p:ajax event="dateSelect" listener="#{contasReceber.calculaVencimento}"
 * update="@this"/> </p:calendar> </div> </div> </h:panelGroup> </ui:fragment>
 * </div> <div class="ui-g" > <p:commandButton value="Lançar"
 * style="margin:0 auto !important;height:40px;width:130px; background-color: seagreen; background: seagreen; color:white !important; font-size: large !important"
 * actionListener="#{contasReceberBean.editarPagamento}" />
 * 
 * <p:commandButton value="Fechar"
 * style="margin:0 auto !important;height:40px;width:130px;  background-color: gray; background: gray; color:white !important; font-size: large !important"
 * oncomplete="PF('relancarPagamentoEditar').hide()"
 * actionListener="#{contasReceberBean.busca}" /> </div> </div> </div> </div>
 * </div> </h:form> </div> </p:dialog>
 */