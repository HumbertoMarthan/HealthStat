package br.com.clinica.bean.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.bean.geral.LoginAtualizaSenha;
import br.com.clinica.controller.geral.LiberacaoController;
import br.com.clinica.controller.geral.LoginController;
import br.com.clinica.controller.geral.PerfilController;
import br.com.clinica.controller.geral.PessoaController;
import br.com.clinica.model.cadastro.pessoa.Atendente;
import br.com.clinica.model.cadastro.pessoa.Estoquista;
import br.com.clinica.model.cadastro.pessoa.Medico;
import br.com.clinica.model.cadastro.pessoa.Pessoa;
import br.com.clinica.model.cadastro.usuario.Liberacao;
import br.com.clinica.model.cadastro.usuario.Login;
import br.com.clinica.model.cadastro.usuario.Perfil;
import br.com.clinica.utils.DialogUtils;
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
	private List<Perfil> lstSelecionada = new ArrayList<>();
	private List<Perfil> lstPerfis = new ArrayList<>();
	private List<Perfil> lstSelecao = new ArrayList<>();
	
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
	private PerfilController perfilController;

	@Autowired
	private PessoaController pessoaController;
	
	@Autowired
	private LiberacaoController liberacaoController;

	Long id;

	@PostConstruct
	public void init() throws Exception {
		busca(); 
		listaPerfis();
	}
	
	public void verificaLogin() {
		List<Login> lst = new ArrayList<>();
		try {
			lst = loginController.findListByQueryDinamica("from Login where login = '" +loginModel.getLogin()+"'");
			
			if(!lst.isEmpty()) {
				addMsg("Este usuário já existe!");
				loginModel.setLogin("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void listaPerfis() {
		try {
			lstPerfis = perfilController.findListByQueryDinamica("from Perfil");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void populaPerfis() {
		
		try {
			lstPerfis = perfilController.findListByQueryDinamica(" from Perfil ");
		
			lstSelecionada =  perfilController.findListByQueryDinamica(" from Perfil e where exists (select 1 from Liberacao ue where ue.perfil.idPerfil = e.idPerfil and ue.login.idLogin = "  +loginModel.getIdLogin() + ")");
															 	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void saveNotReturn() {
		//SOLUCA --> COLOCAR ELSE PARA EXECUTAR
		System.out.println("Entrou no saveNotReturn");
			List<Login> lst = new ArrayList<>();
			try {
				lst = loginController.findListByQueryDinamica("from Login where pessoa.idPessoa = " + loginModel.getPessoa().getIdPessoa());
			}catch (Exception e) {
				error();
				e.printStackTrace();
			}
			
			if(lst.isEmpty() || loginModel.getIdLogin() != null) {
				try {
					Liberacao liberacaoModel = new Liberacao();
					loginModel = loginController.merge(loginModel);
					
					List<Liberacao> lib = new ArrayList<>();
					lib = liberacaoController.findListByQueryDinamica("from Liberacao where login.idLogin = " + loginModel.getIdLogin());
					
					if(!lib.isEmpty()) {
						for (Liberacao l : lib) {
							liberacaoController.delete(l);
						}
					}
					
					for(Perfil p :lstSelecionada){
						liberacaoModel.setPerfil(new Perfil(p.getIdPerfil()));
						liberacaoModel.setLogin(new Login(loginModel.getIdLogin()));
						liberacaoController.merge(liberacaoModel);
					}
					
					addMsg("Usuário salvo com sucesso!!");
				}catch (Exception e) {
					error();
					e.printStackTrace();
				}
			}
			limpar();
	}
	
	public void updateSenha() throws Exception  {
		Login usuario = (Login)  contextoBean.retornaUsuario();

//		Login usuario = (Login)  loginController.findById(Login.class, id);
		System.out.println("Usuario Nome " +usuario.getLogin());
		System.out.println("Senha Atual "+senhaAtual);
		System.out.println("Nova Senha "+novaSenha);
		System.out.println("Confirma Senha" +confirmaSenha);
		//loginController.setExecuteParam("update Login set login = '"+campo1 + "' where idLogin = "+ usuario.getIdLogin());
		
		  if (!senhaAtual.equals(usuario.getSenha())) {
			  addMsg("Senha nâo confere com a do banco"); 
		  } else if(senhaAtual.equals(novaSenha)) { 
			  addMsg("A senha atual não pode ser igual a nova senha."); 
		  } else if(!novaSenha.equals(confirmaSenha)) {
			  addMsg("A nova senha e a confirmação não conferem.");
		  } else {
			  usuario.setSenha(novaSenha);

			  try { 
			  loginController.merge(usuario); 
			  sucesso();
			  usuario = new Login();
			  Thread.sleep(2500);
			  DialogUtils.execute("logout('"+ FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()+"')");
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
		populaPerfis();
	}

	@Override
	public String redirecionarFindEntidade() {
		limpar();
		return getUrlFind();
	}

	public void limpar() {
		loginModel = new Login();
		lstSelecionada = new ArrayList<Perfil>();
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
			str.append(" and upper(a.pessoa.pessoaNome) like '%" + campoBuscaLogin.toUpperCase() + "%'");
		}

		try {
			lstLogin = loginController.findListByQueryDinamica(str.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void inativar() {

		if (loginModel.getInativo() == false) {
			loginModel.setInativo(true);
		} else {
			loginModel.setInativo(false);
		}

		try {
			loginController.merge(loginModel);
			addMsg("Ativado/Inativado com sucesso!");
		} catch (Exception e) {
			System.out.println("Erro ao ativar/inativar");
			e.printStackTrace();
		}
		limpar();
		busca();
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

	public List<Pessoa> completePessoa(String q) throws Exception {
		//select * from Pessoa p where exists(select 1 from Login where p.idPessoa = login.idPessoa)
		return pessoaController.findListByQueryDinamica(" from Pessoa p where pessoaNome like '%" + q.toUpperCase() + "%' and  not exists (select 1  from Login l where p.idPessoa = l.pessoa.idPessoa ) order by p.pessoaNome ASC");
	}

	@Override
	public void saveEdit() {
		System.out.println("Entrou no Save Edit");
		saveNotReturn();
	}

	// GETTERS E SETTERS

	public String getNomePerfil() {
		try {
			return contextoBean.getEntidadeLogada().getPerfil().getPerfilNome();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "N";
	}
	
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
	
	public List<Perfil> getLstSelecionada() {
		return lstSelecionada;
	}

	public void setLstSelecionada(List<Perfil> lstSelecionada) {
		this.lstSelecionada = lstSelecionada;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PerfilController getPerfilController() {
		return perfilController;
	}

	public void setPerfilController(PerfilController perfilController) {
		this.perfilController = perfilController;
	}

	public LiberacaoController getLiberacaoController() {
		return liberacaoController;
	}

	public void setLiberacaoController(LiberacaoController liberacaoController) {
		this.liberacaoController = liberacaoController;
	}

	public List<Perfil> getLstPerfis() {
		return lstPerfis;
	}

	public void setLstPerfis(List<Perfil> lstPerfis) {
		this.lstPerfis = lstPerfis;
	}

	public List<Perfil> getLstSelecao() {
		return lstSelecao;
	}

	public void setLstSelecao(List<Perfil> lstSelecao) {
		this.lstSelecao = lstSelecao;
	}
}
