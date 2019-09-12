package br.com.clinica.bean.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.CaixaController;
import br.com.clinica.controller.geral.CaixaMensalController;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.financeiro.Caixa;
import br.com.clinica.model.financeiro.CaixaMensal;

@Controller
@ViewScoped
@ManagedBean(name = "atendenteBean")
public class CaixaBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private Caixa caixaModel;
	private CaixaMensal caixaMensalModel;
	private String url = "/cadastro/cadCaixa.jsf?faces-redirect=true";
	private String urlFind = "/financeiro/caixa.jsf?faces-redirect=true";
	private List<Caixa> lstContasReceber;
	private List<Caixa> lstContasPagar;
	private List<Caixa> lstCaixa;
	private List<CaixaMensal> lstCaixaMensal;
	private List<Map<Object, Object>> lstFinanceiroMap;
	private List<Map<Object, Object>> lstDespesaMap;
	private List<Map<Object, Object>> lstTotalReceber;
	private List<Map<Object, Object>> lstTotalPagar;
	private Double total = 0.0;
	private Date campoDataInicio;
	private Date campoDataFim;
	private int valorStatus;
	private String campoBuscaFornecedor;
	private String campoBuscaPaciente;

	@Autowired
	private CaixaController caixaController;

	@Autowired
	private CaixaMensalController caixaMensalController;



	public StreamedContent getArquivoReport() throws Exception {
		super.setNomeRelatorioJasper("report_atendente");
		super.setNomeRelatorioSaida("report_atendente");
		// super.setListDataBeanCollectionReport(caixaController.findList(getClassImp()));
		return super.getArquivoReport();
	}

	public CaixaBean() {
		caixaModel = new Caixa();
		caixaMensalModel = new CaixaMensal();
		lstContasReceber = new ArrayList<Caixa>();
		lstContasPagar = new ArrayList<Caixa>();
		lstCaixa = new ArrayList<>();
		lstCaixaMensal = new ArrayList<>();
	}

	@PostConstruct
	public void init() {
		buscaTotalMensal();
		resultadoAnual();

	}

	public void buscaTotalMensal() {
		StringBuilder str = new StringBuilder();
		str.append("select sum(c.valorRetirado) as totalPagar from Caixa c where c.tipo = 'CP'");
		try {
			lstTotalPagar = caixaController.getSqlListMap(str.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//------------------------------------------------------------------------------------
		
		StringBuilder strr = new StringBuilder();
		strr.append("select sum(c.valorInserido) as totalReceber from Caixa c where c.tipo = 'CR'");
		try {
			lstTotalReceber = caixaController.getSqlListMap(strr.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void resultadoAnual() {
		StringBuilder str = new StringBuilder();
		str.append("select ");
		str.append("(select sum(c.valorRetirado) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-01-01' and '2019-01-31') as janeiroP, ");
		str.append("(select sum(c.valorRetirado) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-02-01' and '2019-02-28') as fevereiroP, ");
		str.append("(select sum(c.valorRetirado) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-03-01' and '2019-03-31') as marcoP, ");
		str.append("(select sum(c.valorRetirado) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-04-01' and '2019-04-30') as abrilP, ");
		str.append("(select sum(c.valorRetirado) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-05-01' and '2019-05-31') as maioP, "); 
		str.append("(select sum(c.valorRetirado) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-06-01' and '2019-06-30') as junhoP, ");
		str.append("(select sum(c.valorRetirado) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-07-01' and '2019-07-31') as julhoP, ");
		str.append("(select sum(c.valorRetirado) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-08-01' and '2019-08-31') as agostoP, ");
		str.append("(select sum(c.valorRetirado) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-09-01' and '2019-09-30') as setembroP, ");
		str.append("(select sum(c.valorRetirado) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-10-01' and '2019-10-31') as outubroP, ");
		str.append("(select sum(c.valorRetirado) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-11-01' and '2019-11-30') as novembroP, ");
		str.append("(select sum(c.valorRetirado) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-12-01' and '2019-12-31') as dezembroP, ");
		//Receita
		str.append("(select sum(c.valorInserido) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-01-01' and '2019-01-31') as janeiroR, ");
		str.append("(select sum(c.valorInserido) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-02-01' and '2019-02-28') as fevereiroR, ");
		str.append("(select sum(c.valorInserido) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-03-01' and '2019-03-31') as marcoR, ");
		str.append("(select sum(c.valorInserido) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-04-01' and '2019-04-30') as abrilR, ");
		str.append("(select sum(c.valorInserido) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-05-01' and '2019-05-31') as maioR, ");
		str.append("(select sum(c.valorInserido) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-06-01' and '2019-06-30') as junhoR, ");
		str.append("(select sum(c.valorInserido) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-07-01' and '2019-07-31') as julhoR, ");
		str.append("(select sum(c.valorInserido) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-08-01' and '2019-08-31') as agostoR, ");
		str.append("(select sum(c.valorInserido) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-09-01' and '2019-09-30') as setembroR, ");
		str.append("(select sum(c.valorInserido) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-10-01' and '2019-10-31') as outubroR, ");
		str.append("(select sum(c.valorInserido) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-11-01' and '2019-11-30') as novembroR, ");
		str.append("(select sum(c.valorInserido) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-12-01' and '2019-12-31') as dezembroR, ");
		//Total
		
		str.append("(select (select sum(c.valorInserido) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-01-01'	and '2019-01-31') - ");
		str.append("(select sum(c.valorRetirado) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-01-01' and '2019-01-31')  ) as saldoJaneiro, ");
		
		str.append("(select	(select sum(c.valorInserido) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-02-01' and '2019-02-28') - ");
		str.append("(select sum(c.valorRetirado) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-02-01' and '2019-02-28')) as saldoFevereiro, ");
		
		str.append("(select (select sum(c.valorInserido) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-03-01' and '2019-03-31') - ");
		str.append("(select sum(c.valorRetirado) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-03-01' and '2019-03-31')) as saldoMarco, ");	

		str.append("(select (select sum(c.valorInserido) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-04-01' and '2019-04-30') - ");
		str.append("(select sum(c.valorRetirado) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-04-01' and '2019-04-30')) as saldoAbril, ");

		str.append("(select (select sum(c.valorInserido) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-05-01' and '2019-05-31') - ");
		str.append("(select sum(c.valorRetirado) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-05-01' and '2019-05-31')) as saldoMaio, ");

		str.append("(select (select sum(c.valorInserido) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-06-01' and '2019-06-30') - ");
		str.append("(select sum(c.valorInserido) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-06-01' and '2019-06-30')) as saldoJunho, ");

		str.append("(select (select sum(c.valorInserido) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-07-01' and '2019-07-31') - ");
		str.append("(select sum(c.valorRetirado) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-07-01' and '2019-07-31')) as saldoJulho, ");

		str.append("(select (select sum(c.valorInserido) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-08-01' and '2019-08-31') -  ");
		str.append("(select sum(c.valorRetirado) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-08-01' and '2019-08-31')) as saldoAgosto, ");
				
		str.append("(select (select sum(c.valorInserido) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-09-01' and '2019-09-30') - ");
		str.append("(select sum(c.valorRetirado) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-09-01' and '2019-09-30')) as saldoSetembro, ");

		str.append("(select (select sum(c.valorInserido) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-10-01' and '2019-10-31') - ");
		str.append("(select sum(c.valorRetirado) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-10-01' and '2019-10-31')) as saldoOutubro, ");

		str.append("(select (select sum(c.valorInserido) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-11-01' and '2019-11-30') - ");
		str.append("(select sum(c.valorRetirado) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-11-01' and '2019-11-30')) as saldoNovembro, ");

		str.append("(select (select sum(c.valorInserido) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2019-12-01' and '2019-12-31') - ");
		str.append("(select sum(c.valorRetirado) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2019-12-01' and '2019-12-31')) as saldoDezembro, ");
				
		str.append("(select (select sum(c.valorInserido) from Caixa c where c.tipo = 'CR' and c.dataLancamento BETWEEN '2020-01-01'	and '2020-01-31') - ");
		str.append("(select sum(c.valorRetirado) from Caixa c where c.tipo = 'CP' and c.dataLancamento BETWEEN '2020-01-01' and '2020-01-31')  ) as saldoJaneiro2019 ");		
		
		lstFinanceiroMap = caixaController.getSqlListMap(str.toString());
	}
	
	public void busca() {
		System.out.println("Campo Inicio" + campoDataInicio);
		System.out.println("Campo Fim" + campoDataFim);
		lstCaixa = new ArrayList<Caixa>();
		StringBuilder str = new StringBuilder();
		str.append("from Caixa a where 1=1");
		str.append(" and (tipo = 'CR' or tipo = 'CP')");
		
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
		if(!campoBuscaFornecedor.equals("")) {
		  str.append(" and a.fornecedor.nomeFornecedor LIKE '%"+ campoBuscaFornecedor +"%'");	
		}
		
		if(!campoBuscaPaciente.equals("")) {
			  str.append(" and a.paciente.pessoa.pessoaNome LIKE '%" + campoBuscaPaciente +"%'");	
		}


		System.out.println(" SQL IMPRIMIDO " + str.toString());
		try {
			lstCaixa = caixaController.findListByQueryDinamica(str.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		//chama a lista de balanço 
		resultadoAnual();
	}

	@Override
	public String save() throws Exception {
		caixaModel = caixaController.merge(caixaModel);
		caixaModel = new Caixa();
		return "";
	}

	@Override
	public void saveNotReturn() throws Exception {
		caixaModel = caixaController.merge(caixaModel);
		caixaModel = new Caixa();
		sucesso();

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
	public String editar() throws Exception {
		return getUrl();
	}

	@Override
	public void excluir() throws Exception {
		caixaModel = (Caixa) caixaController.getSession().get(getClassImp(), caixaModel.getIdCaixa());
		caixaController.delete(caixaModel);
		caixaModel = new Caixa();
		sucesso();
	}

	@Override
	protected Class<Caixa> getClassImp() {
		return Caixa.class;
	}

	@Override
	public String redirecionarFindEntidade() throws Exception {
		setarVariaveisNulas();
		return getUrlFind();
	}

	@Override
	public void setarVariaveisNulas() throws Exception {
		caixaModel = new Caixa();
	}

	@Override
	protected InterfaceCrud<Caixa> getController() {
		return caixaController;
	}

	@Override
	public void consultarEntidade() throws Exception {
		caixaModel = new Caixa();
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

	public List<Caixa> getLstContasReceber() {
		return lstContasReceber;
	}

	public void setLstContasReceber(List<Caixa> lstContasReceber) {
		this.lstContasReceber = lstContasReceber;
	}

	public List<Caixa> getLstContasPagar() {
		return lstContasPagar;
	}

	public void setLstContasPagar(List<Caixa> lstContasPagar) {
		this.lstContasPagar = lstContasPagar;
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

	public List<Map<Object, Object>> getLstDespesaMap() {
		return lstDespesaMap;
	}

	public void setLstDespesaMap(List<Map<Object, Object>> lstDespesaMap) {
		this.lstDespesaMap = lstDespesaMap;
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
