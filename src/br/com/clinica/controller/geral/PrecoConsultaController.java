package br.com.clinica.controller.geral;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.outro.PrecoConsulta;
import br.com.clinica.repository.interfaces.RepositoryPrecoConsulta;
import br.com.clinica.service.interfaces.SrvPrecoConsulta;

@Controller
public class PrecoConsultaController extends ImplementacaoCrud<PrecoConsulta> implements InterfaceCrud<PrecoConsulta>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvPrecoConsulta srvPrecoConsulta;
	
	@Resource
	private RepositoryPrecoConsulta repositoryPrecoConsulta;
}
