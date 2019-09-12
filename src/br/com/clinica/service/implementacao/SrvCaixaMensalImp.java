package br.com.clinica.service.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.clinica.repository.interfaces.RepositoryCaixa;
import br.com.clinica.service.interfaces.SrvCaixaMensal;

@Service
public class SrvCaixaMensalImp implements SrvCaixaMensal{

	private static final long serialVersionUID = 1L;
	
	@Resource 
	private RepositoryCaixa repositoryCaixa;
}
