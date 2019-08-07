package br.com.clinica.controller.geral;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.financeiro.ContasPagar;
import br.com.clinica.repository.interfaces.RepositoryContasPagar;
import br.com.clinica.service.interfaces.SrvContasPagar;

@Controller
public class ContasPagarController extends ImplementacaoCrud<ContasPagar> implements 
	InterfaceCrud<ContasPagar>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvContasPagar srvContasPagar;
	
	@Resource
	private RepositoryContasPagar repositoryContasPagar;
	
}
