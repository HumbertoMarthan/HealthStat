package br.com.clinica.bean.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.bean.geral.LoginAtualizaSenha;
import br.com.clinica.controller.geral.LoginController;
import br.com.clinica.controller.geral.PessoaController;
import br.com.clinica.model.cadastro.pessoa.Atendente;
import br.com.clinica.model.cadastro.pessoa.Estoquista;
import br.com.clinica.model.cadastro.pessoa.Medico;
import br.com.clinica.model.cadastro.pessoa.Pessoa;
import br.com.clinica.model.cadastro.usuario.Login;
import br.com.clinica.model.cadastro.usuario.Perfil;
import br.com.clinica.utils.EmailUtils;

@Controller
@ViewScoped
@ManagedBean(name = "loginBean")
public class LoginBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;

	@Autowired
	private ContextoBean contextoBean;

	private Login recuperacao = new Login();

	private Login loginModel = new Login();
	private Perfil perfilModel;
	private Pessoa pessoaModel = new Pessoa();
	private Pessoa pessoaAux;
	private Atendente atendenteModel;// = new Atendente();
	private Estoquista estoquistaModel;// = new Estoquista();
	private Medico medicoModel;// = new Medico();

	private LoginAtualizaSenha loginAtualizaSenha = new LoginAtualizaSenha();

	private String url = "/cadastro/cadUsuario.jsf?faces-redirect=true";
	private String urlFind = "/cadastro/findUsuario.jsf?faces-redirect=true";
	private String urlForget = "/publico/recuperacao.jsf?faces-redirect=true";

	private List<Login> lstLogin = new ArrayList<>();

	private String campoBuscaNome = "";
	private String campoBuscaLogin = "";

	private String cpf = "";
	private String login = "";

	private String senhaAtual = "";
	private String novaSenha = "";
	private String confirmaSenha = "";

	private String novo = "N";

	@Autowired
	private LoginController loginController;

	@Autowired
	private PessoaController pessoaController;

	Long id;

	@PostConstruct
	public void init() throws Exception {
		busca();
		id = contextoBean.getEntidadeLogada().getIdLogin();
	}

	public void updateSenha() throws Exception  {
		Login usuario = (Login)  loginController.findById(Login.class, id);

		//loginController.setExecuteParam("update Login set login = '"+campo1 + "' where idLogin = "+ usuario.getIdLogin());
		
		  if (!senhaAtual.equals(usuario.getSenha())) {
			  addMsg("A senha atual não é valida."); 
		  } 

		  if(senhaAtual.equals(novaSenha)) { 
			  addMsg("A senha atual não pode ser igual a nova senha."); 
		  }
		
		  if(!novaSenha.equals(confirmaSenha)) {
			  addMsg("A nova senha e a confirmação não conferem.");
		  } else {
			
			  usuario.setSenha(novaSenha);
		 
		  try { 
			  loginController.merge(usuario); sucesso();
			  usuario = new Login(); 
		  }
		  catch (Exception e) { 
			  error(); 
			  e.printStackTrace(); }
		  }
		 
	}

	private void limparRecuperacao() {
		login = "";
		cpf = "";
	}

	public void forgetSenha() {
		List<Login> aux = new ArrayList<>();

		try {
			aux = loginController.findListByQueryDinamica(
					"from Login where login = '" + login + "' and pessoa.pessoaCPF = '" + cpf + "'");
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (aux.size() > 0) {
			addMsg("Um email foi enviado para o usuário com a senha de acesso");
			Login t = aux.get(0);
			sendEmailSenha(t);
		} else {
			addMsg("Usuário não encontrado!");
		}
	}

	public void sendEmailSenha(Login t) {

		List<Login> emails = new ArrayList<>();

		try {
			emails = loginController.findListByQueryDinamica(
					"from Login where login = '" + login + "' and pessoa.pessoaCPF = '" + cpf + "'");
			System.out.println("EMAIL ->->" + emails);
		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}

		String email = "";

		for (Login e : emails) {
			email = e.getPessoa().getPessoaEmail();
		}

		String emailTitulo = "";
		emailTitulo += "Usuário e senha Healtstat : ";

		String emailConteudo = "";

		emailConteudo += "<b>INFORMAÇÕES DO LOGIN</b><br />";
		emailConteudo += "<b>USUÁRIO:</b> " + t.getLogin() + "<br />";
		emailConteudo += "<b>SENHA:</b> " + t.getSenha() + "<br /><br /><br />";

		emailConteudo += "<b>EMAIL ENVIADO DE FORMA AUTOMÁTICA POR CLINICA HEALTSTAT, POR FAVOR NÃO RESPONDA O EMAIL</b><br />";
		// Enviar Email com o dados coletados dentro desse método
		EmailUtils.enviarEmail(email, emailTitulo, emailConteudo, true);

		t = new Login();
		limparRecuperacao();
	}

	public void onRowSelect(SelectEvent event) {
		loginModel = (Login) event.getObject();
	}

	@Override
	public String redirecionarFindEntidade() {
		limpar();
		return getUrlFind();
	}

	private void limpar() {
		loginModel = new Login();
	}

	@Override
	public String novo() {
		limpar();
		return getUrl();
	}

	public String redirecionarForget() {
		return getUrlForget();
	}

	public void busca() {
		StringBuilder str = new StringBuilder();
		str.append("from Login a where 1=1");

		if (!campoBuscaLogin.equals("")) {
			str.append(" and upper(a.login) like upper('%" + campoBuscaLogin + "%')");
		}

		try {
			lstLogin = loginController.findListByQueryDinamica(str.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void inativar() {

		if (loginModel.getAtivo().equals("I")) {
			loginModel.setAtivo("A");
			// pessoaModel.setAtivo("A");
			loginModel.setInativo(false);
		} else {
			loginModel.setAtivo("I");
			// pessoaModel.setAtivo("I");
			loginModel.setInativo(true);
		}

		try {
			loginController.saveOrUpdate(loginModel);
			// pessoaController.saveOrUpdate(pessoaModel);
			addMsg("Ativado/Inativado com sucesso!");
		} catch (Exception e) {
			System.out.println("Erro ao ativar/inativar");
			e.printStackTrace();
		}
		limpar();
		try {
			busca();
		} catch (Exception e) {
			System.out.println("Erro ao buscar S");
			e.printStackTrace();
		}
	}

	@Override
	public String editar() {
		return getUrl();
	}

	@Override
	public void excluir() {
		try {
			loginModel = (Login) loginController.getSession().get(Login.class, loginModel.getIdLogin());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			loginController.delete(loginModel);
		} catch (Exception e) {
			e.printStackTrace();
		}

		sucesso();
		busca();
	}

	@Override
	public void saveNotReturn() {
		try {
			loginController.persist(loginModel);
			addMsg("Salvou o Login");
		} catch (Exception e) {
			e.getMessage();
			error();
			e.printStackTrace();
		}

		limpar();
		pessoaModel = new Pessoa();
		pessoaAux = new Pessoa();
	}

	public List<Pessoa> completePessoa(String q) throws Exception {
		return pessoaController.findListByQueryDinamica(
				" from Pessoa where pessoaNome like '%" + q.toUpperCase() + "%' order by pessoaNome ASC");
	}

	@Override
	public void saveEdit() {
		saveNotReturn();
	}

	// GETTERS E SETTERS

	public String getUsuarioLogadoSecurity() {
		return contextoBean.getAuthentication().getName();
	}

	public LoginAtualizaSenha getLoginAtualizaSenha() {
		return loginAtualizaSenha;
	}

	public void setLoginAtualizaSenha(LoginAtualizaSenha loginAtualizaSenha) {
		this.loginAtualizaSenha = loginAtualizaSenha;
	}

	public Date getUltimoAcesso() throws Exception {
		return contextoBean.getEntidadeLogada().getUltimoAcesso();
	}

	public ContextoBean getContextoBean() {
		return contextoBean;
	}

	public void setContextoBean(ContextoBean contextoBean) {
		this.contextoBean = contextoBean;
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

	public String getCampoBuscaNome() {
		return campoBuscaNome;
	}

	public void setCampoBuscaNome(String campoBuscaNome) {
		this.campoBuscaNome = campoBuscaNome;
	}

	public String getCampoBuscaLogin() {
		return campoBuscaLogin;
	}

	public Perfil getPerfilModel() {
		return perfilModel;
	}

	public void setPerfilModel(Perfil perfilModel) {
		this.perfilModel = perfilModel;
	}

	public void setCampoBuscaLogin(String campoBuscaLogin) {
		this.campoBuscaLogin = campoBuscaLogin;
	}

	public Login getLoginModel() {
		return loginModel;
	}

	public void setLoginModel(Login loginModel) {
		this.loginModel = loginModel;
	}

	public List<Login> getLstLogin() {
		return lstLogin;
	}

	public void setLstLogin(List<Login> lstLogin) {
		this.lstLogin = lstLogin;
	}

	public LoginController getLoginController() {
		return loginController;
	}

	public void setLoginController(LoginController loginController) {
		this.loginController = loginController;
	}

	public Pessoa getPessoaModel() {
		return pessoaModel;
	}

	public void setPessoaModel(Pessoa pessoaModel) {
		this.pessoaModel = pessoaModel;
	}

	public String getNovo() {
		return novo;
	}

	public void setNovo(String novo) {
		this.novo = novo;
	}

	public PessoaController getPessoaController() {
		return pessoaController;
	}

	public void setPessoaController(PessoaController pessoaController) {
		this.pessoaController = pessoaController;
	}

	public Atendente getAtendenteModel() {
		return atendenteModel;
	}

	public void setAtendenteModel(Atendente atendenteModel) {
		this.atendenteModel = atendenteModel;
	}

	public Estoquista getEstoquistaModel() {
		return estoquistaModel;
	}

	public void setEstoquistaModel(Estoquista estoquistaModel) {
		this.estoquistaModel = estoquistaModel;
	}

	public Medico getMedicoModel() {
		return medicoModel;
	}

	public void setMedicoModel(Medico medicoModel) {
		this.medicoModel = medicoModel;
	}

	public Pessoa getPessoaAux() {
		return pessoaAux;
	}

	public void setPessoaAux(Pessoa pessoaAux) {
		this.pessoaAux = pessoaAux;
	}

	public Login getRecuperacao() {
		return recuperacao;
	}

	public void setRecuperacao(Login recuperacao) {
		this.recuperacao = recuperacao;
	}

	public String getUrlForget() {
		return urlForget;
	}

	public void setUrlForget(String urlForget) {
		this.urlForget = urlForget;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenhaAtual() {
		return senhaAtual;
	}

	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	public String getConfirmaSenha() {
		return confirmaSenha;
	}

	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}
}
