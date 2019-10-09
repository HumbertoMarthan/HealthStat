package br.com.clinica.bean.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.ConvenioController;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.outro.Convenio;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@Controller
@ViewScoped
@ManagedBean(name = "convenioBean")
public class ConvenioBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;
	private Convenio convenioModel;
	private String url = "/cadastro/cadConvenio.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/findConvenio.jsf?faces-redirect=true";
	private List<Convenio> lstConvenio;
	private List<Map<Object, Object>> lstConvenioTeste;
	private String campoBuscaNome = "";

	@Autowired
	private ConvenioController convenioController;

	public List<Map<Object, Object>> getLstConvenioTeste() {
		return lstConvenioTeste;
	}

	public void setLstConvenioTeste(List<Map<Object, Object>> lstConvenioTeste) {
		this.lstConvenioTeste = lstConvenioTeste;
	}

	public ConvenioBean() {
		setLstConvenio(new ArrayList<>());
		convenioModel = new Convenio();
	}

	public void busca() {
		try {
		lstConvenio = new ArrayList<Convenio>();
		StringBuilder str = new StringBuilder();
		str.append("from Convenio a where 1=1");

		if (!campoBuscaNome.equals("")) {
			str.append(" and upper(a.nomeConvenio) like upper('%" + campoBuscaNome + "%')");
		}
		
		lstConvenio =  convenioController.findListByQueryDinamica(str.toString());
	}catch (Exception e) {
		System.out.println("Erro ao buscar Convenio");
		e.printStackTrace();
	}
		}

	@Override
	public StreamedContent getArquivoReport() throws Exception {
		super.setNomeRelatorioJasper("a");
		super.setNomeRelatorioSaida("a");
		super.setListDataBeanCollectionReport(convenioController.findListByQueryDinamica("from Convenio"));
		return super.getArquivoReport();
	}

	public void gerarRelatorio() throws JRException, Exception {

		System.out.println("Gerando relatório...");
		List<Convenio> listaConvenio = convenioController.findListByQueryDinamica("from Convenio");

		System.out.println("LISTA CONVENIO" + listaConvenio.size());

		System.out.println("NOME DO CONVENIO" + listaConvenio.get(0).getNomeConvenio());

		// JasperReport pathjrxml = JasperCompileManager
		// .compileReport("C:\\Users\\Humberto\\workspace-tcc\\clinica\\src\\relatorio\\a.jrxml");

		JasperReport relatorioJasper = (JasperReport) JRLoader
				.loadObjectFromFile("C:\\Users\\Humberto\\workspace-tcc\\clinica\\src\\relatorio\\a.jasper");

		// Map<String, Object> parametros = new HashMap<>();

		JRBeanCollectionDataSource jrbcds = new JRBeanCollectionDataSource(listaConvenio, false);
		System.out.println("COLLECTION DATASOURCE>>>>>>>" + jrbcds);
		JasperPrint printReport = JasperFillManager.fillReport(relatorioJasper, null, jrbcds);
		JasperExportManager.exportReportToPdfFile(printReport,
				"C:\\Users\\Humberto\\workspace-tcc\\clinica\\src\\relatorio\\a.pdf");
		System.out.println("Relatorio gerado");
	}

	public String edita() throws Exception {
		return getUrl();
	}

	public void onRowSelect(SelectEvent event) {
		convenioModel = (Convenio) event.getObject();
	}

	@Override
	public String save() throws Exception {
		convenioModel = convenioController.merge(convenioModel);

		return "";
	}

	public Convenio getConvenioModel() {
		return convenioModel;
	}

	public void setConvenioModel(Convenio convenioModel) {
		this.convenioModel = convenioModel;
	}

	@Override
	public void saveNotReturn()  {
		try {
		convenioModel = convenioController.merge(convenioModel);
		convenioModel = new Convenio();
		sucesso();
		}catch (Exception e) {
			System.out.println("Erro ao Salvar Convênio");
			e.printStackTrace();
		}
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
		convenioModel = new Convenio();
	}

	@Override
	public String editar() throws Exception {
		return getUrl();
	}

	@Override
	public void excluir() throws Exception {
		convenioModel = (Convenio) convenioController.getSession().get(getClassImp(), convenioModel.getIdConvenio());
		convenioController.delete(convenioModel);
		convenioModel = new Convenio();
		sucesso();
		busca();
	}

	@Override
	protected Class<Convenio> getClassImp() {
		return Convenio.class;
	}

	@Override
	public String redirecionarFindEntidade() throws Exception {
		setarVariaveisNulas();
		return getUrlFind();
	}

	@Override
	protected InterfaceCrud<Convenio> getController() {
		return convenioController;
	}

	@Override
	public void consultarEntidade() throws Exception {
		convenioModel = new Convenio();
	}

	public Convenio getObjetoSelecionado() {
		return convenioModel;
	}

	public void setObjetoSelecionado(Convenio convenioModel) {

		this.convenioModel = convenioModel;
	}

	public String getUrl() {
		return url;
	}

	public String getUrlFind() {
		return urlFind;
	}

	public void setUrlFind(String urlFind) {
		this.urlFind = urlFind;
	}

	public ConvenioController getConvenioController() {
		return convenioController;
	}

	public void setConvenioController(ConvenioController convenioController) {
		this.convenioController = convenioController;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Convenio> getLstConvenio() {
		return lstConvenio;
	}

	public void setLstConvenio(List<Convenio> lstConvenio) {
		this.lstConvenio = lstConvenio;
	}

	public String getCampoBuscaNome() {
		return campoBuscaNome;
	}

	public void setCampoBuscaNome(String campoBuscaNome) {
		this.campoBuscaNome = campoBuscaNome;
	}

}
