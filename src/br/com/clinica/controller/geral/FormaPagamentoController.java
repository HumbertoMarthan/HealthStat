package br.com.clinica.controller.geral;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.financeiro.FormaPagamento;
import br.com.clinica.repository.interfaces.RepositoryFormaPagamento;
import br.com.clinica.service.interfaces.SrvFormaPagamento;

@Controller
public class FormaPagamentoController extends ImplementacaoCrud<FormaPagamento> implements 
InterfaceCrud<FormaPagamento> {
	
	private static final long serialVersionUID = 1L;

	@Resource
	private SrvFormaPagamento srvFormaPagamento;
	
	@Resource
	private RepositoryFormaPagamento repositoryFormaPagamento;
	
}
