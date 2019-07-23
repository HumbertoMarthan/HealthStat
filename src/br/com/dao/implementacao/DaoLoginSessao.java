package br.com.dao.implementacao;

import java.io.Serializable;
import java.util.Date;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.projeto.model.Login;
import br.com.repository.interfaces.RepositoryLoginSessao;

@Repository
public class DaoLoginSessao extends ImplementacaoCrud<Login> implements  RepositoryLoginSessao, Serializable {

	
	private static final long serialVersionUID = 1L;
	//@Autowired
	//private RepositoryEntidade repositoryEntidade;
	
	@Override
	public Date getUltimoAcessoEntidadeLogada(String name) {
		SqlRowSet rowSet = super.getJdbcTemplate().
				queryForRowSet("select ultimoacesso from entidade where inativo is false and login = ?", 
						new Object[] {name});
		return rowSet.next() ? rowSet.getDate("ultimoacesso") : null;
	}
	@Override
	public void updateUltimoAcessoUser(String login) {
		String sql = "update entidade set ultimoacesso = current_timestamp where inativo is false and login = ?";
		super.getSimpleJdbcTemplate().update(sql, login);
	}
	@Override
	public boolean existeUsuario(String login) {
		StringBuilder builder = new StringBuilder();
		builder.append("select count(1) >=1 from entidade where login = '").append(login).append("' ");
		return super.getJdbcTemplate().queryForObject(builder.toString(), Boolean.class )  ;
	}
	

}
