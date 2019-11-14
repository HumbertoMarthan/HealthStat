package br.com.clinica.service.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.clinica.repository.interfaces.RepositoryMedicamento;
import br.com.clinica.service.interfaces.SrvMedicamento;

@Service
public class SrvMedicamentoImp implements SrvMedicamento{

	private static final long serialVersionUID = 1L;
	
	@Resource 
	private RepositoryMedicamento repositoryMedicamento;
}
