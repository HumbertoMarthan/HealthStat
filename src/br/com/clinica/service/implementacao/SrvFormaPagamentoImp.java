package br.com.clinica.service.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.clinica.repository.interfaces.RepositoryFormaPagamento;
import br.com.clinica.service.interfaces.SrvFormaPagamento;


@Service
public class SrvFormaPagamentoImp implements SrvFormaPagamento{
	
	private static final long serialVersionUID = 1L;
	//tdo Imp tem injeção de dependencia do repositorio
	
	
	@Resource 
	private RepositoryFormaPagamento repositoryFormaPagamento;
}
