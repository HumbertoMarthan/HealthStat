package br.com.clinica.controller.geral;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.financeiro.ContasReceber;
import br.com.clinica.repository.interfaces.RepositoryContasReceber;
import br.com.clinica.service.interfaces.SrvContasReceber;

@Controller
public class ContasReceberController extends ImplementacaoCrud<ContasReceber> implements 
	InterfaceCrud<ContasReceber>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvContasReceber srvContasReceber ;
	
	@Resource
	private RepositoryContasReceber repositoryContasReceber;
	
}
