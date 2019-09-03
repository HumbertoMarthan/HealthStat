package br.com.clinica.controller.geral;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.financeiro.Caixa;
import br.com.clinica.repository.interfaces.RepositoryCaixa;
import br.com.clinica.service.interfaces.SrvCaixa;

@Controller
public class CaixaController extends ImplementacaoCrud<Caixa> implements 
	InterfaceCrud<Caixa>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvCaixa srvCaixa;
	
	@Resource
	private RepositoryCaixa repositoryCaixa;
	
}
