package br.com.clinica.bean.view;

import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.PrimeFaces;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.clinica.bean.geral.BeanManagedViewAbstract;
import br.com.clinica.controller.geral.SessionController;
import br.com.clinica.service.interfaces.SrvLogin;


@Controller
@Scope(value="request")
@ManagedBean(name = "loginInvalidarBean")
public class LoginInvalidarBean extends BeanManagedViewAbstract {

	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;
	
	@Resource
	private SessionController sessionController;

	@Resource
	private SrvLogin srvLogin;
	@RequestMapping(value ="**/invalidar_session", method = RequestMethod.POST)
	public void invalidarSessionMetodo(HttpServletRequest httpServletRequest) throws Exception {
		String userLogadoSessao = null;
		if(httpServletRequest.getUserPrincipal() != null) {
			userLogadoSessao = httpServletRequest.getUserPrincipal().getName();
		}
		
		if(userLogadoSessao == null || (userLogadoSessao != null && userLogadoSessao.trim().isEmpty())) {
			userLogadoSessao = httpServletRequest.getRemoteUser();
		}
		
		if(userLogadoSessao == null || (userLogadoSessao.isEmpty() )){
			sessionController.invalidateSession(userLogadoSessao);
		}
		
	}
	
	public void invalidar(ActionEvent actionEvent) throws Exception {
		PrimeFaces context = PrimeFaces.current();
		FacesMessage message = null;
		boolean loggedIn = false;
	
		if(srvLogin.autentico(getUsername(), getPassword())) {
			sessionController.invalidateSession(getUsername());
			loggedIn = true;
		}else {
			loggedIn = false;
			message = new FacesMessage(FacesMessage.SEVERITY_WARN,"Acesso negado","Login ou senha errados");
		}
		
		if(message != null) {
			FacesContext.getCurrentInstance().addMessage("msg", message);
		}
		
		context.ajax().addCallbackParam("loggedIn", loggedIn);
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
