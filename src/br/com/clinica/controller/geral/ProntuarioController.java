package br.com.clinica.controller.geral;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.prontuario.Prontuario;
import br.com.clinica.repository.interfaces.RepositoryProntuario;
import br.com.clinica.service.interfaces.SrvProntuario;

@Controller
public class ProntuarioController extends ImplementacaoCrud<Prontuario> implements 
	InterfaceCrud<Prontuario>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvProntuario srvProntuario;
	
	@Resource
	private RepositoryProntuario repositoryProntuario;
	
	
	
}
