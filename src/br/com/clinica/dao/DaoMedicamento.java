package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.model.prontuario.Medicamento;
import br.com.clinica.repository.interfaces.RepositoryMedicamento;
@Repository
public class DaoMedicamento extends ImplementacaoCrud<Medicamento> 
		implements RepositoryMedicamento {
	
	private static final long serialVersionUID = 1L;

}
