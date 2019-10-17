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
import br.com.clinica.controller.geral.PrecoConsultaController;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.outro.PrecoConsulta;

@Controller
@ViewScoped
@ManagedBean(name = "precoConsultaBean")
public class PrecoConsultaBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;
	private PrecoConsulta precoConsultaModel;
	private String url = "/cadastro/cadPrecoConsulta.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/findPrecoConsulta.jsf?faces-redirect=true";
	private List<PrecoConsulta> lstPrecoConsulta;
	private String campoCategoria = "";

	@Autowired
	private PrecoConsultaController precoConsultaController;


	public PrecoConsultaBean() {
		lstPrecoConsulta = new ArrayList<PrecoConsulta>();
		precoConsultaModel = new PrecoConsulta();
	}

	public void busca() {
		try {
		lstPrecoConsulta = new ArrayList<PrecoConsulta>();
		StringBuilder str = new StringBuilder();
		str.append("from PrecoConsulta a where 1=1");

		if (!campoCategoria.equals("")) {
			str.append(" and upper(a.categoria) like upper('%" + campoCategoria + "%')");
		}
		
		lstPrecoConsulta =  precoConsultaController.findListByQueryDinamica(str.toString());
	}catch (Exception e) {
		System.out.println("Erro ao buscar PrecoConsulta");
		e.printStackTrace();
	}
		}

	@Override
	public StreamedContent getArquivoReport() throws Exception {
		super.setNomeRelatorioJasper("a");
		super.setNomeRelatorioSaida("a");
		super.setListDataBeanCollectionReport(precoConsultaController.findListByQueryDinamica("from PrecoConsulta"));
		return super.getArquivoReport();
	}

	public String edita() {
		return getUrl();
	}

	public void onRowSelect(SelectEvent event) {
		precoConsultaModel = (PrecoConsulta) event.getObject();
	}

	@Override
	public String save() throws Exception {
		precoConsultaModel = precoConsultaController.merge(precoConsultaModel);

		return "";
	}


	@Override
	public void saveNotReturn()  {
		try {
		precoConsultaModel = precoConsultaController.merge(precoConsultaModel);
		precoConsultaModel = new PrecoConsulta();
		sucesso();
		}catch (Exception e) {
			System.out.println("Erro ao Salvar Convênio");
			e.printStackTrace();
		}
	}

	@Override
	public void saveEdit(){
		saveNotReturn();
	}

	@Override
	public String novo() throws Exception {
		setarVariaveisNulas();
		return getUrl();
	}

	@Override
	public void setarVariaveisNulas() throws Exception {
		precoConsultaModel = new PrecoConsulta();
	}

	@Override
	public String editar() throws Exception {
		return getUrl();
	}

	@Override
	public void excluir() throws Exception {
		precoConsultaModel = (PrecoConsulta) precoConsultaController.getSession().get(getClassImp(), precoConsultaModel.getIdPrecoConsulta());
		precoConsultaController.delete(precoConsultaModel);
		precoConsultaModel = new PrecoConsulta();
		sucesso();
		busca();
	}

	@Override
	protected Class<PrecoConsulta> getClassImp() {
		return PrecoConsulta.class;
	}

	@Override
	public String redirecionarFindEntidade() throws Exception {
		setarVariaveisNulas();
		return getUrlFind();
	}

	@Override
	protected InterfaceCrud<PrecoConsulta> getController() {
		return precoConsultaController;
	}

	@Override
	public void consultarEntidade() throws Exception {
		precoConsultaModel = new PrecoConsulta();
	}

	public PrecoConsulta getPrecoConsultaModel() {
		return precoConsultaModel;
	}

	public void setPrecoConsultaModel(PrecoConsulta precoConsultaModel) {
		this.precoConsultaModel = precoConsultaModel;
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

	public List<PrecoConsulta> getLstPrecoConsulta() {
		return lstPrecoConsulta;
	}

	public void setLstPrecoConsulta(List<PrecoConsulta> lstPrecoConsulta) {
		this.lstPrecoConsulta = lstPrecoConsulta;
	}

	public String getCampoCategoria() {
		return campoCategoria;
	}

	public void setCampoCategoria(String campoCategoria) {
		this.campoCategoria = campoCategoria;
	}

	public PrecoConsultaController getPrecoConsultaController() {
		return precoConsultaController;
	}

	public void setPrecoConsultaController(PrecoConsultaController precoConsultaController) {
		this.precoConsultaController = precoConsultaController;
	}
	
	

}