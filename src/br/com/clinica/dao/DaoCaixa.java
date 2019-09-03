package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.model.financeiro.Caixa;
import br.com.clinica.repository.interfaces.RepositoryCaixa;
@Repository
public class DaoCaixa extends ImplementacaoCrud<Caixa> 
		implements RepositoryCaixa {
	
	private static final long serialVersionUID = 1L;

}
