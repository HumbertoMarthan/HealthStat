package br.com.clinica.controller.geral;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.financeiro.CaixaMensal;
import br.com.clinica.repository.interfaces.RepositoryCaixaMensal;
import br.com.clinica.service.interfaces.SrvCaixaMensal;

@Controller
public class CaixaMensalController extends ImplementacaoCrud<CaixaMensal> implements 
	InterfaceCrud<CaixaMensal>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvCaixaMensal srvCaixaMensal;
	
	@Resource
	private RepositoryCaixaMensal repositoryCaixaMensal;
	
}
