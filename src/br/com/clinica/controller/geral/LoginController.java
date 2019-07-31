package br.com.clinica.controller.geral;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.usuario.Login;
import br.com.clinica.service.interfaces.SrvLoginSessao;

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
