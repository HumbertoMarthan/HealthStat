package br.com.clinica.bean.view;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.CaixaController;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.financeiro.Caixa;

@Controller
@ViewScoped
@ManagedBean(name = "atendenteBean")
public class CaixaBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private Caixa caixaModel;
	private String url = "/cadastro/cadCaixa.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/findCaixa.jsf?faces-redirect=true";
	private List<Caixa> lstContasReceber;
	private List<Caixa> lstContasPagar;
	private List<Caixa> lstCaixa;
	private String campoBusca = "";

	@Autowired
	private CaixaController caixaController;

	public StreamedContent getArquivoReport() throws Exception {
		super.setNomeRelatorioJasper("report_atendente");
		super.setNomeRelatorioSaida("report_atendente");
		// super.setListDataBeanCollectionReport(caixaController.findList(getClassImp()));
		return super.getArquivoReport();
	}

	public CaixaBean() {
		caixaModel = new Caixa();
		lstContasReceber = new ArrayList<Caixa>();
		lstContasPagar = new ArrayList<Caixa>();
		lstCaixa = new ArrayList<>();
	}

	public List<Caixa> getLstCaixa() {
		return lstCaixa;
	}

	public void setLstCaixa(List<Caixa> lstCaixa) {
		this.lstCaixa = lstCaixa;
	}

	public void onRowSelect(SelectEvent event) {
		caixaModel = (Caixa) event.getObject();
	}

	public void buscaReceber() throws Exception {
		lstContasReceber = new ArrayList<Caixa>();
		StringBuilder str = new StringBuilder();
		str.append("from Caixa a where 1=1");
		str.append(" and tipo = 'CR'");
		
		lstContasReceber = caixaController.findListByQueryDinamica(str.toString());
	}
	
	public void buscar() {
		lstCaixa = new ArrayList<Caixa>();
		StringBuilder str = new StringBuilder();
		str.append("from Caixa a where 1=1");
		try {
			lstCaixa = caixaController.findListByQueryDinamica(str.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void buscaPagar() throws Exception {
		lstContasPagar = new ArrayList<Caixa>();
		StringBuilder str = new StringBuilder();
		str.append("from Caixa a where 1=1");

		str.append(" and tipo = 'CP'");
		
		lstContasPagar = caixaController.findListByQueryDinamica(str.toString());
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

	public String getCampoBusca() {
		return campoBusca;
	}

	public void setCampoBusca(String campoBusca) {
		this.campoBusca = campoBusca;
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
}
