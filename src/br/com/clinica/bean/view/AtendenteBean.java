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
import java.util.Date;
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
import br.com.clinica.controller.geral.AtendenteController;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.pessoa.Atendente;
import br.com.clinica.model.cadastro.pessoa.Pessoa;
import br.com.clinica.utils.ValidaCPF;

@Controller
@ViewScoped
@ManagedBean(name = "atendenteBean")
public class AtendenteBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ContextoBean contextoBean;

	public ContextoBean getContextoBean() {
		return contextoBean;
	}

	public void setContextoBean(ContextoBean contextoBean) {
		this.contextoBean = contextoBean;
	}

	private Atendente atendenteModel;
	private String url = "/cadastro/cadAtendente.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/findAtendente.jsf?faces-redirect=true";
	private List<Atendente> lstAtendente;
	private String campoBuscaNome = "";
	private String campoBuscaCPF = "";
	private String campoBuscaAtivo = "A";
	@Autowired
	private AtendenteController atendenteController; // Injeta o Atendente Controller

	@Override
	public StreamedContent getArquivoReport() throws Exception {
		super.setNomeRelatorioJasper("report_atendente");
		super.setNomeRelatorioSaida("report_atendente");
		super.setListDataBeanCollectionReport(atendenteController.findList(getClassImp()));
		return super.getArquivoReport();
	}

	public AtendenteBean() {
		atendenteModel = new Atendente();
		lstAtendente = new ArrayList<Atendente>();
	}

	public void onRowSelect(SelectEvent event) {
		atendenteModel = (Atendente) event.getObject();
	}

	public void limpa() {
		/* Dados */
		atendenteModel.getPessoa().setPessoaNome("");
		atendenteModel.getPessoa().setPessoaDataNascimento(new Date());
		atendenteModel.getPessoa().setPessoaSexo("");
		atendenteModel.getPessoa().setPessoaEmail("");
		atendenteModel.getPessoa().setPessoaRG("");
		atendenteModel.getPessoa().setPessoaCPF("");
		atendenteModel.getPessoa().setPessoaObservacao("");
		atendenteModel.getPessoa().setPessoaTelefonePrimario("");
		atendenteModel.getPessoa().setPessoaTelefoneSecundario("");

		/* Endereço */
		atendenteModel.getPessoa().setCep("");
		atendenteModel.getPessoa().setBairro("");
		atendenteModel.getPessoa().setUf("");
		atendenteModel.getPessoa().setLogradouro("");
		atendenteModel.getPessoa().setComplemento("");
		atendenteModel.getPessoa().setLocalidade("");
	}

	public void inativar() {

		if (atendenteModel.getAtivo().equals("I")) {
			atendenteModel.setAtivo("A");
		} else {
			atendenteModel.setAtivo("I");
		}

		try {
			atendenteController.saveOrUpdate(atendenteModel);
		} catch (Exception e) {
			System.out.println("Erro ao ativar/inativar");
			e.printStackTrace();
		}
		this.atendenteModel = new Atendente();
		try {
			busca();
		} catch (Exception e) {
			System.out.println("Erro ao buscar atendente");
			e.printStackTrace();
		}
	}

	public void busca() throws Exception {
		lstAtendente = new ArrayList<Atendente>();
		StringBuilder str = new StringBuilder();
		str.append("from Atendente a where 1=1");

		if (!campoBuscaNome.equals("")) {
			str.append(" and (upper(a.pessoa.pessoaNome) like upper('%" + campoBuscaNome + "%'))");
		}
		if (!campoBuscaCPF.equals("")) {
			str.append(" and a.pessoa.pessoaCPF like'%" + campoBuscaCPF + "%'");
		}
		if (campoBuscaAtivo.equals("A") || campoBuscaAtivo.equals("I")) {
			System.out.println("Entrou no A or I");
			str.append(" and a.ativo = '" + campoBuscaAtivo.toUpperCase() + "'");
		}
		if (campoBuscaAtivo.equals("T")) {
			System.out.println("Entro no T");
			str.append(" and (a.ativo = 'A' or a.ativo = 'I') ");
		}
		System.out.println("LISTA SELECT :> " + str);
		lstAtendente = atendenteController.findListByQueryDinamica(str.toString());
	}

	private void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	@Override
	public String save() throws Exception {

		atendenteModel = atendenteController.merge(atendenteModel);
		atendenteModel = new Atendente();

		return "";
	}

	@Override
	public void saveNotReturn() {
		if (idadeMinimaFuncionario() == true) {
			if (ValidaCPF.isCPF(atendenteModel.getPessoa().getPessoaCPF())) {
				try {
				atendenteModel = atendenteController.merge(atendenteModel);
				atendenteModel = new Atendente();
					sucesso();
				} catch (Exception e) {
					System.out.println("Erro ao salvar Atendente");
					e.printStackTrace();
				}
			} else {
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
						"Cpf Inválido: " + atendenteModel.getPessoa().getPessoaCPF(), "");
				addMessage(message);
				System.out.println("ERRO CPF INVÁLIDO");
			}
		} else {
			System.out.println("ERRO IDADE MINIMA INVALIDA>>>");
		}
		try {
			busca();
		} catch (Exception e) {
			System.out.println("Erro ao Buscar Atendente");
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

	public String edita() throws Exception {
		return getUrl();
	}

	@Override
	public String editar() throws Exception {
		return getUrl();
	}

	@Override
	public void excluir() throws Exception {
		atendenteModel = (Atendente) atendenteController.getSession().get(getClassImp(),
				atendenteModel.getIdAtendente());
		atendenteController.delete(atendenteModel);
		atendenteModel = new Atendente();
		sucesso();
		busca();
	}

	@Override
	protected Class<Atendente> getClassImp() {
		return Atendente.class;
	}

	@Override
	public String redirecionarFindEntidade() throws Exception {
		setarVariaveisNulas();
		return getUrlFind();
	}

	@Override
	public void setarVariaveisNulas() throws Exception {
		atendenteModel = new Atendente();
	}

	@Override
	protected InterfaceCrud<Atendente> getController() {
		return atendenteController;
	}

	// PESQUISA CEP
	public void pesquisarCep(AjaxBehaviorEvent event) throws Exception {
		try {
			URL url = new URL("https://viacep.com.br/ws/" + atendenteModel.getPessoa().getCep() + "/json/");
			URLConnection connection = url.openConnection();
			InputStream inputStream = connection.getInputStream(); // is
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8")); // br

			String cep = "";
			StringBuilder jsonCEP = new StringBuilder();

			while ((cep = bufferedReader.readLine()) != null) {
				jsonCEP.append(cep);

			}

			Pessoa gson = new Gson().fromJson(jsonCEP.toString(), Pessoa.class);

			atendenteModel.getPessoa().setCep(gson.getCep());
			atendenteModel.getPessoa().setLogradouro(gson.getLogradouro());
			atendenteModel.getPessoa().setBairro(gson.getBairro());
			atendenteModel.getPessoa().setComplemento(gson.getComplemento());
			atendenteModel.getPessoa().setUf(gson.getUf());
			atendenteModel.getPessoa().setLocalidade(gson.getLocalidade());

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

	public boolean idadeMinimaFuncionario() {
		Calendar dateOfBirth = new GregorianCalendar();
		dateOfBirth.setTime(atendenteModel.getPessoa().getPessoaDataNascimento());
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

	@Override
	public void consultarEntidade() throws Exception {
		atendenteModel = new Atendente();
	}

	// GETTERS E SETTERS

	public Atendente getatendenteModel() {
		return atendenteModel;
	}

	public void setatendenteModel(Atendente atendenteModel) {

		this.atendenteModel = atendenteModel;
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

	public Atendente getAtendenteModel() {
		return atendenteModel;
	}

	public void setAtendenteModel(Atendente atendenteModel) {
		this.atendenteModel = atendenteModel;
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

	public AtendenteController getAtendenteController() {
		return atendenteController;
	}

	public void setAtendenteController(AtendenteController atendenteController) {
		this.atendenteController = atendenteController;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Atendente> getLstAtendente() {
		return lstAtendente;
	}

	public void setLstAtendente(List<Atendente> lstAtendente) {
		this.lstAtendente = lstAtendente;
	}

	public String getCampoBuscaAtivo() {
		return campoBuscaAtivo;
	}

	public void setCampoBuscaAtivo(String campoBuscaAtivo) {
		this.campoBuscaAtivo = campoBuscaAtivo;
	}

}
