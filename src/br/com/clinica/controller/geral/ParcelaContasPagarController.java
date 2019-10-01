package br.com.clinica.controller.geral;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.financeiro.ParcelaContasPagar;
import br.com.clinica.repository.interfaces.RepositoryParcelaContasPagar;
import br.com.clinica.service.interfaces.SrvParcelaContasPagar;

@Controller
public class ParcelaContasPagarController extends ImplementacaoCrud<ParcelaContasPagar> implements 
	InterfaceCrud<ParcelaContasPagar>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvParcelaContasPagar srvParcelaContasPagar;
	
	@Resource
	private RepositoryParcelaContasPagar repositoryParcelaPagar;
	
	
}
