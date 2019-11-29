package br.com.clinica.bean.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.ConvenioController;
import br.com.clinica.model.cadastro.outro.Convenio;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;

@Controller
@ViewScoped
@ManagedBean(name = "convenioBean")
public class ConvenioBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private Convenio convenioModel = new Convenio();
	
	private String url = "/cadastro/cadConvenio.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/findConvenio.jsf?faces-redirect=true";

	private Map<String, Object> parametroRelatorio;
	private List<Convenio> lstConvenio = new ArrayList<>();

	private String campoBuscaNome = "";

	@Autowired
	private ConvenioController convenioController;

	@PostConstruct
	public void init() {
		busca();
	}
	
	public void geraRelatorio(){
		JasperPrint  relatorio =  imprimir(lstConvenio, "convenio.jrxml");
		try {
			JasperPrintManager.printReport(relatorio, true);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	public void busca() {
		try {
			lstConvenio = new ArrayList<Convenio>();
			StringBuilder str = new StringBuilder();
			str.append("from Convenio a where 1=1");

			if (!campoBuscaNome.equals("")) {
				str.append(" and upper(a.nomeConvenio) like upper('%" + campoBuscaNome + "%')");
			}

			lstConvenio = convenioController.findListByQueryDinamica(str.toString());
		} catch (Exception e) {
			System.out.println("Erro ao buscar Convenio");
			e.printStackTrace();
		}
		campoBuscaNome ="";
	}

	public void onRowSelect(SelectEvent event) {
		convenioModel = (Convenio) event.getObject();
	}
	

	public void onRowSelectDouble(SelectEvent event) {
		convenioModel = (Convenio) event.getObject();
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/clinica/cadastro/cadConvenio.jsf");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	// PESQUISA CEP
		public void pesquisarCep(AjaxBehaviorEvent event) throws Exception {
			try {
				URL url = new URL("https://viacep.com.br/ws/" + convenioModel.getCep().replace(".", "").replace("-", "") + "/json/");
				URLConnection connection = url.openConnection();
				InputStream inputStream = connection.getInputStream(); // is
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8")); // br

				String cep = "";
				StringBuilder jsonCEP = new StringBuilder();

				while ((cep = bufferedReader.readLine()) != null) {
					jsonCEP.append(cep);

				}

				Convenio gson = new Gson().fromJson(jsonCEP.toString(), Convenio.class);

				convenioModel.setCep(gson.getCep());
				convenioModel.setLogradouro(gson.getLogradouro());
				convenioModel.setBairro(gson.getBairro());
				convenioModel.setLocalidade(gson.getLocalidade());

				System.out.println("CEP Saindo " + jsonCEP);

			} catch (MalformedURLException ex) {
				ex.printStackTrace();
				addMsg("Cep Inválido (Erro ao Buscar|Sem internet");
				error();
				System.out.println("Erro ao buscar cep 'Internet' ");
			} catch (IOException e) {
				// CAI AQUI SE DIGITAR MAIS NUMEROS DO QUE TEM UM CEP
				// COLOQUEI UM LIMITADOR NO CAMPO DE DIGITOS
				addMsg("Cep Inválido");
			}
		}

	@Override
	public String save(){
		try {
			convenioModel = convenioController.merge(convenioModel);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	@Override
	public void saveNotReturn() {
		try {
			convenioModel = convenioController.merge(convenioModel);
			limpar();
			sucesso();
		} catch (Exception e) {
			System.out.println("Erro ao Salvar Convênio");
			e.printStackTrace();
		}
		busca();
	}

	@Override
	public void saveEdit() {
		saveNotReturn();
	}

	@Override
	public String novo(){
		limpar();
		return getUrl();
	}


	@Override
	public String editar() {
		return getUrl();
	}

	@Override
	public void excluir(){
		try {
			convenioModel = (Convenio) convenioController.getSession().get(Convenio.class, convenioModel.getIdConvenio());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			convenioController.delete(convenioModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		limpar();
		sucesso();
		busca();
	}

	@Override
	public String redirecionarFindEntidade() {
		limpar();
		return getUrlFind();
	}
	
	public void limpar(){
		convenioModel = new Convenio();
	}
	
	public Convenio getConvenioModel() {
		return convenioModel;
	}

	public void setConvenioModel(Convenio convenioModel) {
		this.convenioModel = convenioModel;
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

	public Map<String, Object> getParametroRelatorio() {
		return parametroRelatorio;
	}

	public void setParametroRelatorio(Map<String, Object> parametroRelatorio) {
		this.parametroRelatorio = parametroRelatorio;
	}
}
