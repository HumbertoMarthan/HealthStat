package br.com.clinica.controller.geral;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.financeiro.ParcelaPagar;
import br.com.clinica.repository.interfaces.RepositoryParcelaPagar;
import br.com.clinica.service.interfaces.SrvParcelaPagar;

@Controller
public class ParcelaPagarController extends ImplementacaoCrud<ParcelaPagar> implements 
	InterfaceCrud<ParcelaPagar>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvParcelaPagar srvParcelaPagar;
	
	@Resource
	private RepositoryParcelaPagar repositoryParcelaPagar;
	
	
}
