package br.com.clinica.bean.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.EstoquistaController;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.pessoa.Estoquista;
import br.com.clinica.model.cadastro.pessoa.Pessoa;
import br.com.clinica.utils.ValidaCPF;


@Controller
@ViewScoped
@ManagedBean(name = "estoquistaBean")
public class EstoquistaBean extends BeanManagedViewAbstract {
	
	private static final long serialVersionUID = 1L;
	private Estoquista estoquistaModel;
	private List<Estoquista> lstEstoquista;
	private String url = "/cadastro/cadEstoquista.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/findEstoquista.jsf?faces-redirect=true";
	private String campoBuscaNome;
	private String campoBuscaCPF;
	

	@Autowired
	private EstoquistaController estoquistaController;
	
	public EstoquistaBean() {
		estoquistaModel = new Estoquista();
		lstEstoquista = new ArrayList<Estoquista>();
	}
	
	public void busca() throws Exception {
		StringBuilder str = new StringBuilder();
		str.append("from Estoquista a where 1=1");

		if (!campoBuscaNome.equals("")) {
			str.append(" and upper(a.pessoa.pessoaNome) like upper('%" + campoBuscaNome + "%')");
		}
		if (!campoBuscaCPF.equals("")) {
			str.append(" and a.pessoa.pessoaCPF like'%" + campoBuscaCPF + "%'");
		}
		
		lstEstoquista =  estoquistaController.findListByQueryDinamica(str.toString());
	}
	
	public void limpar() {
		
		this.estoquistaModel = new Estoquista();
	}
	
	public void onRowSelect(SelectEvent event) {
		estoquistaModel = (Estoquista) event.getObject();
	}
	

	public EstoquistaController getEstoquistaController() {
		return estoquistaController;
	}
	
	//Gera o Relatório 
	@Override
	public StreamedContent getArquivoReport() throws Exception {
		super.setNomeRelatorioJasper("report_estoquista");
		super.setNomeRelatorioSaida("report_estoquista");
		super.setListDataBeanCollectionReport(estoquistaController.findList(getClassImp()));
		return super.getArquivoReport();
	}
	
	public void pesquisarCep(AjaxBehaviorEvent event) throws Exception {
		try {
			URL url = new URL("https://viacep.com.br/ws/"+ estoquistaModel.getPessoa().getCep() +"/json/");
			URLConnection connection = url.openConnection();
			InputStream inputStream = connection.getInputStream(); //is
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8")); //br
		
			String cep = "";
			StringBuilder jsonCEP = new StringBuilder();
			
			while((cep = bufferedReader.readLine()) != null)    {
				jsonCEP.append(cep);
				
			}
			Pessoa gson = new Gson().fromJson(jsonCEP.toString(), Pessoa.class);
			
			//String logradouro ="";
			estoquistaModel.getPessoa().setCep(gson.getCep());
			estoquistaModel.getPessoa().setLogradouro(gson.getLogradouro());
			estoquistaModel.getPessoa().setBairro(gson.getBairro());
			//estoquistaModel.getPessoa().setIbge(gson.getIbge());
			estoquistaModel.getPessoa().setComplemento(gson.getComplemento());
			estoquistaModel.getPessoa().setUf(gson.getUf());
			estoquistaModel.getPessoa().setLocalidade(gson.getLocalidade());
			System.out.println("CEP Saindo " +jsonCEP);
			
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
			addMsg("Cep Inválido (Erro ao Buscar|Sem internet");
			error();
		} catch (IOException   e) {
			addMsg("Cep Inválido");
		}
	}
	
	public void limpa() {
		
		  /*Dados*/
		  estoquistaModel.getPessoa().setPessoaNome("");
		  estoquistaModel.getPessoa().setPessoaDataNascimento(null);
		  estoquistaModel.getPessoa().setPessoaSexo("");
		  estoquistaModel.getPessoa().setPessoaEmail("");
		  estoquistaModel.getPessoa().setPessoaRG("");
		  estoquistaModel.getPessoa().setPessoaCPF("");
		  estoquistaModel.getPessoa().setPessoaObservacao("");
		  estoquistaModel.getPessoa().setPessoaTelefonePrimario("");
		  estoquistaModel.getPessoa().setPessoaTelefoneSecundario("");
		  /*Endereço*/
		  estoquistaModel.getPessoa().setCep("");
		  estoquistaModel.getPessoa().setBairro("");
		  estoquistaModel.getPessoa().setUf("");
		  estoquistaModel.getPessoa().setLogradouro("");
		  estoquistaModel.getPessoa().setComplemento("");
		  estoquistaModel.getPessoa().setLocalidade("");
	}
	
	public boolean idadeMinimaFuncionario() {
		Calendar dateOfBirth = new GregorianCalendar();
		dateOfBirth.setTime(estoquistaModel.getPessoa().getPessoaDataNascimento());
		Calendar today = Calendar.getInstance();
		int age = today.get(Calendar.YEAR) - dateOfBirth.get(Calendar.YEAR);
		dateOfBirth.add(Calendar.YEAR, age);
		if (today.before(dateOfBirth)) {
			age--;
		}
		if (age < 16) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
					"A idade informada deve ser maior/igual que 16 Anos", "");
			System.out.println("TRUE IDADE ATENDENTE:>>>" + age);
			addMessage(message);
			return false;
		} else {
			System.out.println("FALSE IDADE ATENDENTE:>>>" + age);
			return true;
		}

	}
	
	private void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	@Override
	public String save() throws Exception {
	    
		estoquistaModel = estoquistaController.merge(estoquistaModel);
		estoquistaModel = new Estoquista();
		return "";
	}
	
	@Override
	public void saveNotReturn() throws Exception {
		if (idadeMinimaFuncionario() == true) {
			if (ValidaCPF.isCPF(estoquistaModel.getPessoa().getPessoaCPF())) { 
		estoquistaModel = estoquistaController.merge(estoquistaModel);
		estoquistaModel = new Estoquista();
		sucesso();
			}else {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_WARN, "Cpf Inválido: "
				+estoquistaModel.getPessoa().getPessoaCPF(), "");
				addMessage(message);
				System.out.println("ERRO CPF INVÁLIDO");
			}
		}else {
				//FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Cpf Inválido", "");
				//addMessage(message);
				System.out.println("ERRO IDADE MINIMA INVALIDA>>>");
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
	
	public String edita()  throws Exception {
		return getUrl();
	}
	
	@Override
	public void setarVariaveisNulas() throws Exception {
		estoquistaModel = new Estoquista();
	}
	
	public String editar() throws Exception {
		return getUrl();
	}
	
	@Override
	public void excluir() throws Exception {
		estoquistaModel = (Estoquista) estoquistaController.getSession().get(getClassImp(),  estoquistaModel.getIdEstoquista());
		estoquistaController.delete(estoquistaModel);	
		estoquistaModel = new Estoquista();
		sucesso();
		busca();
	}

	@Override
	protected Class<Estoquista> getClassImp() {
		return Estoquista.class;
	}
	
	@Override
	public String redirecionarFindEntidade() throws Exception {
		setarVariaveisNulas();
		return getUrlFind();
	}

	@Override
	protected InterfaceCrud<Estoquista> getController() {
		return estoquistaController;
	}
	@Override
	public void consultarEntidade() throws Exception {
	}

	@Override
	public String condicaoAndParaPesquisa() throws Exception {
		return "";
	}

	public List<Estoquista> getLstEstoquista() {
		return lstEstoquista;
	}

	public void setLstEstoquista(List<Estoquista> lstEstoquista) {
		this.lstEstoquista = lstEstoquista;
	}
	
	public void setEstoquistaController(EstoquistaController estoquistaController) {
		this.estoquistaController = estoquistaController;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Estoquista getestoquistaModel() {
		return estoquistaModel;
	}
	
	public void setestoquistaModel(Estoquista estoquistaModel) {
		this.estoquistaModel = estoquistaModel;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getUrlFind() {
		return urlFind;
	}
	
	public String getCampoBuscaNome() {
		return campoBuscaNome;
	}

	public void setCampoBuscaNome(String campoBuscaNome) {
		this.campoBuscaNome = campoBuscaNome;
	}

	public String getCampoBuscaCPF() {
		return campoBuscaCPF;
	}

	public void setCampoBuscaCPF(String campoBuscaCPF) {
		this.campoBuscaCPF = campoBuscaCPF;
	}
	
	public void setUrlFind(String urlFind) {
		this.urlFind = urlFind;
	}
	
	
}
