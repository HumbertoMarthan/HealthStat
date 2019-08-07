package br.com.clinica.service.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.clinica.repository.interfaces.RepositoryParcelaPagar;
import br.com.clinica.service.interfaces.SrvParcelaPagar;

@Service
public class SrvParcelaPagarImp implements SrvParcelaPagar{

	private static final long serialVersionUID = 1L;
	//tdo Imp tem injeção de dependencia do repositorio
	
	@Resource 
	private RepositoryParcelaPagar repositoryParcelaPagar; 
}
