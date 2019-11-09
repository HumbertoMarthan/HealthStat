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

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.EstoquistaController;
import br.com.clinica.controller.geral.LoginController;
import br.com.clinica.controller.geral.PessoaController;
import br.com.clinica.model.cadastro.pessoa.Estoquista;
import br.com.clinica.model.cadastro.pessoa.Pessoa;
import br.com.clinica.model.cadastro.usuario.Login;
import br.com.clinica.model.cadastro.usuario.Perfil;
import br.com.clinica.utils.DialogUtils;
import br.com.clinica.utils.ValidaCPF;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;

@Controller
@ViewScoped
@ManagedBean(name = "estoquistaBean")
public class EstoquistaBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private Login loginModel = new Login();
	private Perfil perfilModel;
	private Estoquista estoquistaModel = new Estoquista();
	private List<Estoquista> 	lstEstoquista = new ArrayList<Estoquista>();
	private String url = "/cadastro/cadEstoquista.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/findEstoquista.jsf?faces-redirect=true";
	private String campoBuscaNome = "";
	private String campoBuscaCPF = "";
	private String campoBuscaAtivo = "T";
	private String criarLogin ;
	private Long idPessoa = 0L;

	@Autowired
	private EstoquistaController estoquistaController;
	
	@Autowired
	private LoginController loginController;
	
	@Autowired
	private PessoaController pessoaController;

	@PostConstruct
	public void init() {
		busca();
	}
	
	public void geraRelatorio(){
		JasperPrint  relatorio =  imprimir(lstEstoquista, "estoquista.jrxml");
		try {
			//JasperPrintManager.printReport(print, true);
			JasperPrintManager.printReport(relatorio, true);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
	
	public void busca(){
		lstEstoquista = new ArrayList<Estoquista>();
		StringBuilder str = new StringBuilder();
		str.append("from Estoquista a where 1=1");
		try {
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
		
		str.append(" and pessoa.tipoPessoa = 'EST' " );

		lstEstoquista = estoquistaController.findListByQueryDinamica(str.toString());
		
		}catch (Exception e) {
			System.out.println("Erro ao buscar o Estoquista");
			e.printStackTrace();
		}
		
		campoBuscaCPF = "";
		campoBuscaNome = "";
	}

	public void inativar() {

		if (estoquistaModel.getAtivo().equals("I")) {
			estoquistaModel.setAtivo("A");
		} else {
			estoquistaModel.setAtivo("I");
		}

		try {
			estoquistaController.saveOrUpdate(estoquistaModel);
		} catch (Exception e) {
			System.out.println("Erro ao ativar/inativar");
			e.printStackTrace();
		}
		this.estoquistaModel = new Estoquista();
		try {
			busca();
		} catch (Exception e) {
			System.out.println("Erro ao buscar atendente");
			e.printStackTrace();
		}
	}

	public void limpar() {
		this.estoquistaModel = new Estoquista();
	}

	public void onRowSelect(SelectEvent event) {
		estoquistaModel = (Estoquista) event.getObject();
	}

	public void pesquisarCep(AjaxBehaviorEvent event)  {
		try {
			URL url = new URL("https://viacep.com.br/ws/" + estoquistaModel.getPessoa().getCep() + "/json/");
			URLConnection connection = url.openConnection();
			InputStream inputStream = connection.getInputStream(); // is
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8")); // br

			String cep = "";
			StringBuilder jsonCEP = new StringBuilder();

			while ((cep = bufferedReader.readLine()) != null) {
				jsonCEP.append(cep);

			}
			Pessoa gson = new Gson().fromJson(jsonCEP.toString(), Pessoa.class);

			// String logradouro ="";
			estoquistaModel.getPessoa().setCep(gson.getCep());
			estoquistaModel.getPessoa().setLogradouro(gson.getLogradouro());
			estoquistaModel.getPessoa().setBairro(gson.getBairro());
			// estoquistaModel.getPessoa().setIbge(gson.getIbge());
			estoquistaModel.getPessoa().setComplemento(gson.getComplemento());
			estoquistaModel.getPessoa().setUf(gson.getUf());
			estoquistaModel.getPessoa().setLocalidade(gson.getLocalidade());
			System.out.println("CEP Saindo " + jsonCEP);

		} catch (MalformedURLException ex) {
			ex.printStackTrace();
			addMsg("Cep Inválido (Erro ao Buscar|Sem internet");
			error();
		} catch (IOException e) {
			addMsg("Cep Inválido");
		}
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
			addMsg("A idade informada deve ser maior/igual que 16 Anos");
			System.out.println("TRUE IDADE ATENDENTE:>>>" + age);
			return false;
		} else {
			System.out.println("FALSE IDADE ATENDENTE:>>>" + age);
			return true;
		}

	}


	@Override
	public String save() {

		try {
			estoquistaModel = estoquistaController.merge(estoquistaModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		estoquistaModel = new Estoquista();
		return "";
	}

	@Override
	public void saveNotReturn() {
		try {
		if (idadeMinimaFuncionario() == true) {
			if (ValidaCPF.isValid(estoquistaModel.getPessoa().getPessoaCPF())) {
				estoquistaModel.getPessoa().setTipoPessoa("EST");
				estoquistaModel = estoquistaController.merge(estoquistaModel);
				idPessoa = estoquistaModel.getPessoa().getIdPessoa();
				criarLogin = estoquistaModel.getTemLogin();
				limpar();
				sucesso();
			} else {
				addMsg("Cpf Inválido: " + estoquistaModel.getPessoa().getPessoaCPF());
				System.out.println("ERRO CPF INVÁLIDO");
			}
			
			if(criarLogin.equals("S")) {
				
				List<Login> lst = new ArrayList<Login>();
				try {
					lst = (List<Login>) estoquistaController.getListSQLDinamica("select * from Login where idPessoa ="+ idPessoa);
				} catch (Exception e) {
					e.printStackTrace();
				}

				if(lst.size() == 0) {
					DialogUtils.openDialog("dialogUsuario");
				}else {
					addMsg("Usuário já tem Login!");
				}
			}
			
			
		} else {
			System.out.println("ERRO IDADE MINIMA INVALIDA>>>");
		}
		}catch (Exception e) {
			System.out.println("Erro ao Salvar Estoquista");
			e.printStackTrace();
		}
		busca();
	}
	
	public void salvarLogin() {
		if (loginModel.getLogin() != null && loginModel.getSenha() != null && perfilModel != null) { // nome do login vazio
			Pessoa pessoa = null;
			try {
				pessoa = (Pessoa) pessoaController.findById(Pessoa.class, idPessoa);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			try {
				
				loginModel.setPerfil(new Perfil(perfilModel.getIdPerfil()));
				loginModel.setPessoa((pessoa));
				
				loginModel = loginController.merge(loginModel);
				addMsg("Salvou o Login");
				loginModel = new Login();
				DialogUtils.closeDialog("dialogUsuario");
			
			} catch (Exception e) {
				error();
				e.printStackTrace();
			}
		}
	}
	
	

	@Override
	public void saveEdit()  {
		saveNotReturn();
	}

	@Override
	public String novo() {
		limpar();
		return getUrl();
	}

	public String editar() {
		return getUrl();
	}

	@Override
	public void excluir() {
		try {
			estoquistaModel = (Estoquista) estoquistaController.getSession().get(Estoquista.class,estoquistaModel.getIdEstoquista());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			estoquistaController.delete(estoquistaModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		estoquistaModel = new Estoquista();
		sucesso();
		busca();
	}

	@Override
	public String redirecionarFindEntidade() {
		limpar();
		return getUrlFind();
	}

	public EstoquistaController getEstoquistaController() {
		return estoquistaController;
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

	public Estoquista getEstoquistaModel() {
		return estoquistaModel;
	}

	public void setEstoquistaModel(Estoquista estoquistaModel) {
		this.estoquistaModel = estoquistaModel;
	}

	public String getCampoBuscaAtivo() {
		return campoBuscaAtivo;
	}

	public void setCampoBuscaAtivo(String campoBuscaAtivo) {
		this.campoBuscaAtivo = campoBuscaAtivo;
	}

	public String getCriarLogin() {
		return criarLogin;
	}

	public void setCriarLogin(String criarLogin) {
		this.criarLogin = criarLogin;
	}

	public Long getIdPessoa() {
		return idPessoa;
	}

	public void setIdPessoa(Long idPessoa) {
		this.idPessoa = idPessoa;
	}

	public Login getLoginModel() {
		return loginModel;
	}

	public void setLoginModel(Login loginModel) {
		this.loginModel = loginModel;
	}

	public Perfil getPerfilModel() {
		return perfilModel;
	}

	public void setPerfilModel(Perfil perfilModel) {
		this.perfilModel = perfilModel;
	}

	public LoginController getLoginController() {
		return loginController;
	}

	public void setLoginController(LoginController loginController) {
		this.loginController = loginController;
	}

	public PessoaController getPessoaController() {
		return pessoaController;
	}

	public void setPessoaController(PessoaController pessoaController) {
		this.pessoaController = pessoaController;
	}
}