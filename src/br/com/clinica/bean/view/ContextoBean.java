package br.com.clinica.bean.view;

import java.io.Serializable;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import br.com.clinica.controller.geral.LoginController;
import br.com.clinica.controller.geral.SessionController;
import br.com.clinica.model.cadastro.usuario.Login;

@Scope(value = "session")
@Component(value = "contextoBean")
public class ContextoBean implements Serializable {

	/**
	 * Retorna todas a informações do usuario logado
	 * 
	 * @return
	 */
	private static final long serialVersionUID = 1L;
	private static final String USER_LOGADO_SESSAO = "userLogadoSessao";

	@Autowired
	private LoginController loginController;

	@Autowired
	private SessionController sessionController;

	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	public Login getEntidadeLogada() throws Exception {
		Login login = (Login) getExternalContext().getSessionMap().get(USER_LOGADO_SESSAO);

		if (login == null || (login != null && !login.getIdLogin().equals(getUserPrincipal()))) {

			if (getAuthentication().isAuthenticated()) {
				
				loginController.updateUltimoAcessoUser(getAuthentication().getName());
				
				login = loginController.findUserLogado(getAuthentication().getName());
				
				getExternalContext().getSessionMap().put(USER_LOGADO_SESSAO, login);

				sessionController.addSession(login.getLogin(),
						(HttpSession) getExternalContext().getSession(true));
			}

		}
		return login;
	}
	
	public Login retornaUsuario() {
		Login login = (Login) getExternalContext().getSessionMap().get(USER_LOGADO_SESSAO);
	return login;
	}

	private Object getUserPrincipal() {

		return getExternalContext().getUserPrincipal().getName();
	}

	public ExternalContext getExternalContext() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		return externalContext;
	}

	public boolean possuiAcesso(String... acessos) {

		for (String acesso : acessos) {
			for (GrantedAuthority authority : getAuthentication().getAuthorities()) {
				if (authority.getAuthority().trim().equals(acesso.trim())) {
					return true;
				}
			}
		}

		return false;
	}
}
