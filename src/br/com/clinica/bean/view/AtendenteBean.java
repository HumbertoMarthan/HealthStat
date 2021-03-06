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
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.AtendenteController;
import br.com.clinica.controller.geral.LiberacaoController;
import br.com.clinica.controller.geral.LoginController;
import br.com.clinica.controller.geral.PessoaController;
import br.com.clinica.model.cadastro.pessoa.Atendente;
import br.com.clinica.model.cadastro.pessoa.Pessoa;
import br.com.clinica.model.cadastro.usuario.Liberacao;
import br.com.clinica.model.cadastro.usuario.Login;
import br.com.clinica.model.cadastro.usuario.Perfil;
import br.com.clinica.utils.DialogUtils;
import br.com.clinica.utils.ValidaCPF;
import br.com.clinica.utils.ValidaEmail;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;

@Controller
@ViewScoped
@ManagedBean(name = "atendenteBean")
public class AtendenteBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	private Login loginModel = new Login();
	private Perfil perfilModel;
	private Atendente atendenteModel = new Atendente();
	private String url = "/cadastro/cadAtendente.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/findAtendente.jsf?faces-redirect=true";
	private List<Atendente> lstAtendente = new ArrayList<Atendente>();;
	private String campoBuscaNome = "";
	private String campoBuscaCPF = "";
	private String campoBuscaAtivo = "A";
	private String criarLogin ;
	private Long idPessoa = 0L;

	@Autowired
	private LoginController loginController;
	
	@Autowired
	private PessoaController pessoaController;
	
	@Autowired
	private ContextoBean contextoBean;
	
	
	@Autowired
	private AtendenteController atendenteController; // Injeta o Atendente Controller

	@PostConstruct
	public void init() {
		busca();
	}
	
	public void geraRelatorio(){
		JasperPrint  relatorio =  imprimir(lstAtendente, "atendente.jrxml");
		try {
			//JasperPrintManager.printReport(print, true);
			JasperPrintManager.printReport(relatorio, true);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	public void onRowSelect(SelectEvent event) {
		atendenteModel = (Atendente) event.getObject();
	}
	
	public void onRowSelectDouble(SelectEvent event) {
		atendenteModel = (Atendente) event.getObject();
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("/clinica/cadastro/cadAtendente.jsf");
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public void busca() {
		lstAtendente = new ArrayList<Atendente>();
		StringBuilder str = new StringBuilder();
		str.append("from Atendente a where 1=1");

		if (!campoBuscaNome.equals("")) {
			str.append(" and upper(a.pessoa.pessoaNome) like '%" + campoBuscaNome.toUpperCase() + "%'");
		}
		if (!campoBuscaCPF.equals("")) {
			str.append(" and a.pessoa.pessoaCPF like '%" + campoBuscaCPF + "%'");
		}
		if (campoBuscaAtivo.equals("A") || campoBuscaAtivo.equals("I")) {
			System.out.println("Entrou no A or I");
			str.append(" and a.ativo = '" + campoBuscaAtivo.toUpperCase() + "'");
		}
		if (campoBuscaAtivo.equals("T")) {
			System.out.println("Entro no T");
			str.append(" and (a.ativo = 'A' or a.ativo = 'I') ");
		}
		
		str.append(" and pessoa.tipoPessoa = 'ATE' " );
		
		try {
			lstAtendente = atendenteController.findListByQueryDinamica(str.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		campoBuscaCPF = "";
		campoBuscaNome = "";
	}

	@Override
	public String save() {
		try {
			atendenteModel = atendenteController.merge(atendenteModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		atendenteModel = new Atendente();
		return "";
	}

	@Override
	public void saveNotReturn() {
		if (idadeMinimaFuncionario() == true) {
			if (ValidaCPF.isValid(atendenteModel.getPessoa().getPessoaCPF())) {
				if (ValidaEmail.validarEmail(atendenteModel.getPessoa().getPessoaEmail())) {
					try {
						atendenteModel.getPessoa().setTipoPessoa("ATE");
						atendenteModel = atendenteController.merge(atendenteModel);
						idPessoa = atendenteModel.getPessoa().getIdPessoa();
						
						List<Login> lst = new ArrayList<>();
						lst = loginController.findListByQueryDinamica("from Login where pessoa.idPessoa = " + atendenteModel.getPessoa().getIdPessoa());
						
						if(lst.isEmpty()) {
							DialogUtils.openDialog("dialogUsuario");
						}
						
						limpar();
						sucesso();
						
					} catch (Exception e) {
						System.out.println("Erro ao salvar Atendente");
						e.printStackTrace();
					}
			  }else {
				  addMsg("Email Inv�lido: " + atendenteModel.getPessoa().getPessoaEmail()); 
			  }
			} else {
				addMsg("Cpf Inv�lido: " + atendenteModel.getPessoa().getPessoaCPF());
				System.out.println("ERRO CPF INV�LIDO");
			}
			
			busca();

		}
	}
	private List<Perfil> lstSelecionada = new ArrayList<>();
	
	@Autowired
	private LiberacaoController liberacaoController;
	
	public void verificaLogin() {
		List<Login> lst = new ArrayList<>();
		try {
			lst = loginController.findListByQueryDinamica("from Login where login = '" +loginModel.getLogin()+"'");
			
			if(!lst.isEmpty()) {
				addMsg("Este usu�rio j� existe!");
				loginModel.setLogin("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void salvarLogin() {
			Pessoa pessoa = null;
			try {
				pessoa = (Pessoa) pessoaController.findById(Pessoa.class, idPessoa);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				loginModel.setPessoa((pessoa));
				loginModel = loginController.merge(loginModel);
				Liberacao liberacaoModel = new Liberacao();
				
				for(Perfil p :lstSelecionada){
					liberacaoModel.setPerfil(new Perfil(p.getIdPerfil()));
					liberacaoModel.setLogin(new Login(loginModel.getIdLogin()));
					liberacaoController.merge(liberacaoModel);
				}
				addMsg("Usu�rio salvo com sucesso!!");
				
				DialogUtils.closeDialog("dialogUsuario");
				loginModel = new Login();
				lstSelecionada = new ArrayList<>();
			} catch (Exception e) {
				error();
				e.printStackTrace();
			}
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

	@Override
	public String editar() {
		return getUrl();
	}

	@Override
	public void excluir() {
		try {
			atendenteModel = (Atendente) atendenteController.getSession().get(Atendente.class,
					atendenteModel.getIdAtendente());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			atendenteController.delete(atendenteModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		atendenteModel = new Atendente();
		sucesso();
		busca();
	}

	@Override
	public String redirecionarFindEntidade() {
		limpar();
		return getUrlFind();
	}

	public void limpar() {
		atendenteModel = new Atendente();
	}

	// PESQUISA CEP
	public void pesquisarCep(AjaxBehaviorEvent event) throws Exception {
		try {
			URL url = new URL("https://viacep.com.br/ws/" + atendenteModel.getPessoa().getCep().replace(".", "").replace("-", "") + "/json/");
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
			addMsg("Cep Inv�lido (Erro ao Buscar|Sem internet");
			error();
			System.out.println("Erro ao buscar cep 'Internet' ");
		} catch (IOException e) {
			// CAI AQUI SE DIGITAR MAIS NUMEROS DO QUE TEM UM CEP
			// COLOQUEI UM LIMITADOR NO CAMPO DE DIGITOS
			
			System.out.println("https://viacep.com.br/ws/" + atendenteModel.getPessoa().getCep().replace(".", "").replace("-", "") + "/json/"); 
			e.printStackTrace();
			//addMsg("Cep Inv�lido");
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
			addMsg("A idade informada deve ser maior/igual que 16 Anos");
			System.out.println("TRUE IDADE ATENDENTE:>>>" + age);
			return false;
		} else {
			System.out.println("FALSE IDADE ATENDENTE:>>>" + age);
			return true;
		}

	}

	// GETTERS E SETTERS

	public Atendente getAtendenteModel() {
		return atendenteModel;
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

	public String getCriarLogin() {
		return criarLogin;
	}

	public void setCriarLogin(String criarLogin) {
		this.criarLogin = criarLogin;
	}

	public Login getLoginModel() {
		return loginModel;
	}

	public void setLoginModel(Login loginModel) {
		this.loginModel = loginModel;
	}

	public ContextoBean getContextoBean() {
		return contextoBean;
	}

	public void setContextoBean(ContextoBean contextoBean) {
		this.contextoBean = contextoBean;
	}

	public LoginController getLoginController() {
		return loginController;
	}

	public void setLoginController(LoginController loginController) {
		this.loginController = loginController;
	}

	public Perfil getPerfilModel() {
		return perfilModel;
	}

	public void setPerfilModel(Perfil perfilModel) {
		this.perfilModel = perfilModel;
	}

	public Long getIdPessoa() {
		return idPessoa;
	}

	public void setIdPessoa(Long idPessoa) {
		this.idPessoa = idPessoa;
	}

	public PessoaController getPessoaController() {
		return pessoaController;
	}

	public void setPessoaController(PessoaController pessoaController) {
		this.pessoaController = pessoaController;
	}

	public List<Perfil> getLstSelecionada() {
		return lstSelecionada;
	}

	public void setLstSelecionada(List<Perfil> lstSelecionada) {
		this.lstSelecionada = lstSelecionada;
	}

	public LiberacaoController getLiberacaoController() {
		return liberacaoController;
	}

	public void setLiberacaoController(LiberacaoController liberacaoController) {
		this.liberacaoController = liberacaoController;
	}
}
