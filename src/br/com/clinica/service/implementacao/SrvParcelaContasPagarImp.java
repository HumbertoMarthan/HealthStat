package br.com.clinica.service.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.clinica.repository.interfaces.RepositoryParcelaContasPagar;
import br.com.clinica.service.interfaces.SrvParcelaContasPagar;

@Service
public class SrvParcelaContasPagarImp implements SrvParcelaContasPagar{

	private static final long serialVersionUID = 1L;
	//tdo Imp tem injeção de dependencia do repositorio
	
	@Resource 
	private RepositoryParcelaContasPagar repositoryParcelaContasPagar; 
}
