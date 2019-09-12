package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.model.financeiro.CaixaMensal;
import br.com.clinica.repository.interfaces.RepositoryCaixaMensal;
@Repository
public class DaoCaixaMensal extends ImplementacaoCrud<CaixaMensal> 
		implements RepositoryCaixaMensal {
	
	private static final long serialVersionUID = 1L;

}
