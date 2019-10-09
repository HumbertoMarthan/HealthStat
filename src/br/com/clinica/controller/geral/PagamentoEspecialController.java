package br.com.clinica.controller.geral;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.financeiro.PagamentoEspecial;
import br.com.clinica.repository.interfaces.RepositoryPagamentoEspecial;
import br.com.clinica.service.interfaces.SrvPagamentoEspecial;

@Controller
public class PagamentoEspecialController extends ImplementacaoCrud<PagamentoEspecial> implements InterfaceCrud<PagamentoEspecial>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvPagamentoEspecial srvPagamentoEspecial;
	
	@Resource
	private RepositoryPagamentoEspecial repositoryPagamentoEspecial;
}
