package br.com.srv.implementacao;

import java.io.Serializable;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.repository.interfaces.RepositoryLoginSessao;
import br.com.srv.interfaces.SrvLoginSessao;

@Service
public class SrvLoginSessaoImp implements Serializable, SrvLoginSessao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private RepositoryLoginSessao repositoryLoginSessao;

	@Override
	public Date getUltimoAcessoEntidadeLogada(String name) {
		return repositoryLoginSessao.getUltimoAcessoEntidadeLogada(name);
		
	}

	@Override
	public void updateUltimoAcessoUser(String login) {
		repositoryLoginSessao.updateUltimoAcessoUser(login);
	}

	@Override
	public boolean existeUsuario(String login) {
			 
		return repositoryLoginSessao.existeUsuario(login);
	}

}
