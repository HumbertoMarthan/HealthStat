package br.com.clinica.service.implementacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.clinica.repository.interfaces.RepositoryLogin;
import br.com.clinica.service.interfaces.SrvLogin;


@Service
public class SrvLoginImp implements SrvLogin {
	
	private static final long serialVersionUID = 1L;
	
	@Autowired
	private RepositoryLogin repositoryLogin;
	
	@Override
	public boolean autentico(String login, String senha) throws Exception {
		return repositoryLogin.autentico(login, senha);
	}

}
