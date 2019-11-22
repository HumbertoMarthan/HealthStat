package br.com.clinica.service.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.clinica.repository.interfaces.RepositoryMedicamentoProntuario;
import br.com.clinica.service.interfaces.SrvMedicamentoProntuario;

@Service
public class SrvMedicamentoProntuarioImp implements SrvMedicamentoProntuario{

	private static final long serialVersionUID = 1L;
	
	@Resource 
	private RepositoryMedicamentoProntuario repositoryMedicamentoProntuario;
}
