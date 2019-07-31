package br.com.clinica.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.repository.interfaces.RepositoryLogin;

@Repository
public class DaoLogin extends ImplementacaoCrud<Object> implements 	RepositoryLogin {

	private static final long serialVersionUID = 1L;

	@Override
	public boolean autentico(String login, String senha) throws Exception {
		String sql = "select count(1) as autentica from login where login = ? and senha = ?";
		SqlRowSet sqlRowSet = super.getJdbcTemplate().queryForRowSet(sql, new Object[]{login,senha});
		return sqlRowSet.next() ? sqlRowSet.getInt("autentica") > 0 : false;
		
	}

}
