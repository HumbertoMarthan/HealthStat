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
import br.com.clinica.controller.geral.CaixaMensalController;
import br.com.clinica.model.financeiro.Caixa;
import br.com.clinica.model.financeiro.CaixaMensal;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;

@Controller
@ViewScoped
@ManagedBean(name = "atendenteBean")
public class CaixaBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private Caixa caixaModel = new Caixa();
	private CaixaMensal caixaMensalModel = new CaixaMensal();

	private String url = "/cadastro/cadCaixa.jsf?faces-redirect=true";
	private String urlFind = "/financeiro/caixa.jsf?faces-redirect=true";

	private List<Caixa> lstCaixa = new ArrayList<>();
	private List<CaixaMensal> lstCaixaMensal = new ArrayList<>();
	private List<Map<Object, Object>> lstFinanceiroMap;
	private List<Map<Object, Object>> lstTotalReceber;
	private List<Map<Object, Object>> lstTotalPagar;

	private Double total = 0.0;
	private Date campoDataInicio;
	private Date campoDataFim;
	private int valorStatus;
	private String campoBuscaFornecedor = "";
	private String campoBuscaPaciente = "";

	@Autowired
	private CaixaController caixaController;

	@Autowired
	private CaixaMensalController caixaMensalController;

	@PostConstruct
	public void init() {
		buscaTotalMensal();
		resultadoAnual();
		busca();
	}
	
	public void geraRelatorio(){
		JasperPrint  relatorio =  imprimir(lstCaixa, "movimentos.jrxml");
		try {
			//JasperPrintManager.printReport(print, true);
			JasperPrintManager.printReport(relatorio, true);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}


	public void buscaTotalMensal() {
		StringBuilder str = new StringBuilder();
		str.append("select sum(c.valorRetirado) as totalPagar from Caixa c where c.tipo = 'CP'");
		try {
			lstTotalPagar = caixaController.getSqlListMap(str.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ------------------------------------------------------------------------------------

		StringBuilder strr = new StringBuilder();
		strr.append("select sum(c.valorInserido) as totalReceber from Caixa c where c.tipo = 'CR'");
		try {
			lstTotalReceber = caixaController.getSqlListMap(strr.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void resultadoAnual() {
		// Despesas
		 StringBuilder str = new StringBuilder();
			str.append("select ");
			str.append(
					"(select COALESCE(sum(c.valorRetirado),0.0) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-01-01' and '2019-01-31') as janeiroP, ");
			str.append(
					"(select COALESCE(sum(c.valorRetirado),0.0) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-02-01' and '2019-02-28') as fevereiroP, ");
			str.append(
					"(select COALESCE(sum(c.valorRetirado),0.0) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-03-01' and '2019-03-31') as marcoP, ");
			str.append(
					"(select COALESCE(sum(c.valorRetirado),0.0) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-04-01' and '2019-04-30') as abrilP, ");
			str.append(
					"(select COALESCE(sum(c.valorRetirado),0.0) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-05-01' and '2019-05-31') as maioP, ");
			str.append(
					"(select COALESCE(sum(c.valorRetirado),0.0) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-06-01' and '2019-06-30') as junhoP, ");
			str.append(
					"(select COALESCE(sum(c.valorRetirado),0.0) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-07-01' and '2019-07-31') as julhoP, ");
			str.append(
					"(select COALESCE(sum(c.valorRetirado),0.0) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-08-01' and '2019-08-31') as agostoP, ");
			str.append(
					"(select COALESCE(sum(c.valorRetirado),0.0) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-09-01' and '2019-09-30') as setembroP, ");
			str.append(
					"(select COALESCE(sum(c.valorRetirado),0.0) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-10-01' and '2019-10-31') as outubroP, ");
			str.append(
					"(select COALESCE(sum(c.valorRetirado),0.0) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-11-01' and '2019-11-30') as novembroP, ");
			str.append(
					"(select COALESCE(sum(c.valorRetirado),0.0) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-12-01' and '2019-12-31') as dezembroP, ");

			// Receita
			str.append(
					"(select COALESCE(sum(c.valorInserido),0.0) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-01-01' and '2019-01-31') as janeiroR, ");
			str.append(
					"(select COALESCE(sum(c.valorInserido),0.0) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-02-01' and '2019-02-28') as fevereiroR, ");
			str.append(
					"(select COALESCE(sum(c.valorInserido),0.0) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-03-01' and '2019-03-31') as marcoR, ");
			str.append(
					"(select COALESCE(sum(c.valorInserido),0.0) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-04-01' and '2019-04-30') as abrilR, ");
			str.append(
					"(select COALESCE(sum(c.valorInserido),0.0) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-05-01' and '2019-05-31') as maioR, ");
			str.append(
					"(select COALESCE(sum(c.valorInserido),0.0) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-06-01' and '2019-06-30') as junhoR, ");
			str.append(
					"(select COALESCE(sum(c.valorInserido),0.0) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-07-01' and '2019-07-31') as julhoR, ");
			str.append(
					"(select COALESCE(sum(c.valorInserido),0.0) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-08-01' and '2019-08-31') as agostoR, ");
			str.append(
					"(select COALESCE(sum(c.valorInserido),0.0) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-09-01' and '2019-09-30') as setembroR, ");
			str.append(
					"(select COALESCE(sum(c.valorInserido),0.0) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-10-01' and '2019-10-31') as outubroR, ");
			str.append(
					"(select COALESCE(sum(c.valorInserido),0.0) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-11-01' and '2019-11-30') as novembroR, ");
			str.append(
					"(select COALESCE(sum(c.valorInserido),0.0) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-12-01' and '2019-12-31') as dezembroR, ");
			// Total

			str.append(
					"((select COALESCE(sum(c.valorInserido),0.0) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-01-01'	and '2019-01-31') - ");
			str.append(
					"(select  COALESCE(sum(c.valorRetirado),0.0) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-01-01' and '2019-01-31')) as saldoJaneiro, ");

			str.append(
					"((select COALESCE(sum(c.valorInserido),0.0) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-02-01' and '2019-02-28') - ");
			str.append(
					"(select  COALESCE(sum(c.valorRetirado),0.0) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-02-01' and '2019-02-28'))  as saldoFevereiro, ");

			str.append(
					"((select COALESCE(sum(c.valorInserido),0.0) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-03-01' and '2019-03-31') - ");
			str.append(
					"(select  COALESCE(sum(c.valorRetirado),0.0) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-03-01' and '2019-03-31')) as saldoMarco, ");

			str.append(
					"((select COALESCE(sum(c.valorInserido),0.0) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-04-01' and '2019-04-30') - ");
			str.append(
					"(select  COALESCE(sum(c.valorRetirado),0.0) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-04-01' and '2019-04-30')) as saldoAbril, ");

			str.append(
					"((select COALESCE(sum(c.valorInserido),0.0) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-05-01' and '2019-05-31') - ");
			str.append(
					"(select  COALESCE(sum(c.valorRetirado),0.0) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-05-01' and '2019-05-31')) as saldoMaio, ");

			str.append(
					"((select COALESCE(sum(c.valorInserido),0.0) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-06-01' and '2019-06-30') - ");
			str.append(
					"(select  COALESCE(sum(c.valorInserido),0.0) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-06-01' and '2019-06-30')) as saldoJunho, ");

			str.append(
					"((select COALESCE(sum(c.valorInserido),0.0) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-07-01' and '2019-07-31') - ");
			str.append(
					"(select  COALESCE(sum(c.valorRetirado),0.0) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-07-01' and '2019-07-31')) as saldoJulho, ");

			str.append(
					"((select COALESCE(sum(c.valorInserido),0.0) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-08-01' and '2019-08-31') -  ");
			str.append(
					"(select  COALESCE(sum(c.valorRetirado),0.0) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-08-01' and '2019-08-31')) as saldoAgosto, ");

			str.append(
					"((select COALESCE(sum(c.valorInserido),0.0) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-09-01' and '2019-09-30') - ");
			str.append(
					"(select  COALESCE(sum(c.valorRetirado),0.0) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-09-01' and '2019-09-30')) as saldoSetembro, ");

			str.append(
					"((select COALESCE(sum(c.valorInserido),0.0) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-10-01' and '2019-10-31') - ");
			str.append(
					"(select  COALESCE(sum(c.valorRetirado),0.0) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-10-01' and '2019-10-31')) as saldoOutubro, ");

			str.append(
					"((select COALESCE(sum(c.valorInserido),0.0) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-11-01' and '2019-11-30') - ");
			str.append(
					"(select  COALESCE(sum(c.valorRetirado),0.0) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-11-01' and '2019-11-30')) * (-1) as saldoNovembro, ");

			str.append(
					"((select COALESCE(sum(c.valorInserido),0.0) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-12-01' and '2019-12-31') - ");
			str.append(
					"(select  COALESCE(sum(c.valorRetirado),0.0) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-12-01' and '2019-12-31')) as saldoDezembro, ");

			str.append(
					"((select COALESCE(sum(c.valorInserido),0.0) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2020-01-01'	and '2020-01-31') - ");
			str.append(
					"(select  COALESCE(sum(c.valorRetirado),0.0) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2020-01-01' and '2020-01-31')) as saldoJaneiro2019 ");

			
			lstFinanceiroMap = caixaController.getSqlListMap(str.toString());

			System.out.println("SQL >> "+str.toString());

	}

	public void busca() {
		
		StringBuilder str = new StringBuilder();
		str.append("from Caixa a where 1=1");
		str.append(" and (tipo = 'CR' or tipo = 'CP')");
		
		try {
		
		if (campoDataInicio != null || campoDataFim != null) {
			if (campoDataFim.before(campoDataInicio) == false) {
				str.append(" and a.dataLancamento BETWEEN  '" + campoDataInicio + "' AND '" + campoDataFim + "'");
			} else {
				try {
					addMsg("A data Fim não pode vir antes da Data Inicio");
				} catch (Exception e) {
					System.out.println("erro ao mostra mensagem no filtro do caixa ");
					e.printStackTrace();
				}
			}

			if (campoDataInicio == null && campoDataFim != null) {
				try {
					addMsg("Campo de Data Inicio não pode estar vazio");
				} catch (Exception e) {
					System.out.println("Erro ao mostrar Data Inicio");
					e.printStackTrace();
				}

			} else if (campoDataFim == null && campoDataInicio != null) {
				try {
					addMsg("Campo de Data Fim não pode estar vazio");
				} catch (Exception e) {
					System.out.println("Erro ao mostrar Data Fim");
					e.printStackTrace();
				}
			}

		}
		if (!campoBuscaFornecedor.equals("")) {
			str.append(" and upper(a.fornecedor.nomeFornecedor) LIKE '%" + campoBuscaFornecedor.toUpperCase() + "%'");
		}

		if (!campoBuscaPaciente.equals("")) {
			str.append(" and upper(a.paciente.pessoa.pessoaNome) LIKE '%" + campoBuscaPaciente.toUpperCase() + "%'");
		}

		System.out.println(" SQL IMPRIMIDO " + str.toString());
		try {
			lstCaixa = caixaController.findListByQueryDinamica(str.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		resultadoAnual();
	}

	@Override
	public String save() {
		try {
			caixaModel = caixaController.merge(caixaModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		limpar();
		return "";
	}

	private void limpar() {
		caixaModel = new Caixa();
	}

	@Override
	public void saveNotReturn() {
		try {
			caixaModel = caixaController.merge(caixaModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		limpar();
		sucesso();

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

	public String edita() throws Exception {
		return getUrl();
	}

	@Override
	public String editar() {
		return getUrl();
	}

	@Override
	public void excluir() {
		try {
			caixaModel = (Caixa) caixaController.getSession().get(Caixa.class, caixaModel.getIdCaixa());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			caixaController.delete(caixaModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		limpar();
		sucesso();
	}

	@Override
	public String redirecionarFindEntidade() {
		limpar();
		return getUrlFind();
	}

	// GETTERS E SETTERS

	public String getUrl() {
		return url;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public String getUrlFind() {
		return urlFind;
	}

	public void setUrlFind(String urlFind) {
		this.urlFind = urlFind;
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

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Map<Object, Object>> getLstTotalPagar() {
		return lstTotalPagar;
	}

	public void setLstTotalPagar(List<Map<Object, Object>> lstTotalPagar) {
		this.lstTotalPagar = lstTotalPagar;
	}

	public List<Map<Object, Object>> getLstTotalReceber() {
		return lstTotalReceber;
	}

	public void setLstTotalReceber(List<Map<Object, Object>> lstTotalReceber) {
		this.lstTotalReceber = lstTotalReceber;
	}

	public Date getCampoDataInicio() {
		return campoDataInicio;
	}

	public int getValorStatus() {
		return valorStatus;
	}

	public void setValorStatus(int valorStatus) {
		this.valorStatus = valorStatus;
	}

	public void setCampoDataInicio(Date campoDataInicio) {
		this.campoDataInicio = campoDataInicio;
	}

	public Date getCampoDataFim() {
		return campoDataFim;
	}

	public void setCampoDataFim(Date campoDataFim) {
		this.campoDataFim = campoDataFim;
	}

	public CaixaMensal getCaixaMensalModel() {
		return caixaMensalModel;
	}

	public void setCaixaMensalModel(CaixaMensal caixaMensalModel) {
		this.caixaMensalModel = caixaMensalModel;
	}

	public CaixaMensalController getCaixaMensalController() {
		return caixaMensalController;
	}

	public void setCaixaMensalController(CaixaMensalController caixaMensalController) {
		this.caixaMensalController = caixaMensalController;
	}

	public List<CaixaMensal> getLstCaixaMensal() {
		return lstCaixaMensal;
	}

	public void setLstCaixaMensal(List<CaixaMensal> lstCaixaMensal) {
		this.lstCaixaMensal = lstCaixaMensal;
	}

	public List<Caixa> getLstCaixa() {
		return lstCaixa;
	}

	public void setLstCaixa(List<Caixa> lstCaixa) {
		this.lstCaixa = lstCaixa;
	}

	public List<Map<Object, Object>> getLstFinanceiroMap() {
		return lstFinanceiroMap;
	}

	public void setLstFinanceiroMap(List<Map<Object, Object>> lstFinanceiroMap) {
		this.lstFinanceiroMap = lstFinanceiroMap;
	}

	public String getCampoBuscaFornecedor() {
		return campoBuscaFornecedor;
	}

	public void setCampoBuscaFornecedor(String campoBuscaFornecedor) {
		this.campoBuscaFornecedor = campoBuscaFornecedor;
	}

	public String getCampoBuscaPaciente() {
		return campoBuscaPaciente;
	}

	public void setCampoBuscaPaciente(String campoBuscaPaciente) {
		this.campoBuscaPaciente = campoBuscaPaciente;
	}

	public void onRowSelect(SelectEvent event) {
		caixaModel = (Caixa) event.getObject();
	}
}
