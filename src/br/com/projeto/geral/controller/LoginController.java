package br.com.projeto.geral.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.framework.inteface.crud.InterfaceCrud;
import br.com.projeto.model.Login;
import br.com.srv.interfaces.SrvLoginSessao;

@Controller
public class LoginController extends ImplementacaoCrud<Login> 
implements  InterfaceCrud<Login> {

	@Autowired
	private SrvLoginSessao srvLoginSessao;
	
	private static final long serialVersionUID = 1L;
	

	public Login findUserLogado(String userLogado) throws Exception{
		
		return super.findUniqueByProperty(Login.class, 
				userLogado, "login", " and entity.inativo is false");
	}
	
	public Date getUltimoAcessoEntidadeLogada(String login) {
		return srvLoginSessao.getUltimoAcessoEntidadeLogada(login);
	}
	
	public void updateUltimoAcessoUser(String name) {	
		srvLoginSessao.updateUltimoAcessoUser(name);
	
	
	}

}
