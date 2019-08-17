package br.com.clinica.hibernate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class ImplementacaoCrud<T> implements InterfaceCrud<T>, Serializable {

	private static final long serialVersionUID = 1L;

	private static SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

	@Autowired
	private JdbcTemplateImpl jdbcTemplate;

	@Autowired
	private SimpleJdbcTemplateImpl simpleJdbcTemplate;

	@Autowired
	private SimpleJdbcInsertImplents simpleJdbcInsert;

	@Autowired
	private SimpleJdbcTemplateImpl simpleJdbcTemplateImpl;

	@SuppressWarnings("unused")
	private SimpleJdbcTemplate getSimpleJdbcTemplateImpl() {
		return simpleJdbcTemplateImpl;

	}

	@Override
	public void save(T obj) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().save(obj);
		executeFlushSession();
	}

	@Override
	public void persist(T obj) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().persist(obj);
		executeFlushSession();
	}

	@Override
	public void saveOrUpdate(T obj) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().saveOrUpdate(obj);
		executeFlushSession();
	}

	@Override
	public void update(T obj) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().update(obj);
		executeFlushSession();

	}

	@Override
	public void delete(T obj) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().delete(obj);
		executeFlushSession();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T merge(T obj) throws Exception {
		validaSessionFactory();
		obj = (T) sessionFactory.getCurrentSession().merge(obj);
		executeFlushSession();
		return obj;
	}

	/**
	 * Select em HQL, CONTADOR
	 * 
	 * @param hql
	 * @return
	 */
	public Long getCountParam(String hql) {
		try {
			return (Long) getSession().createQuery("select count(*) " + hql.replace("fetch", "")).uniqueResult();

		} catch (Exception e) {
			System.out.println("Erro:" + e.getMessage());
		}

		return new Long("0");
	}

	/**
	 * Map recebe como parametro um sql puro
	 * 
	 * @param sql
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<Object, Object>> getSqlListMap(final String sql) {

		try {
			return (List<Map<Object, Object>>) getSession().createSQLQuery(sql)
					.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).list();

		} catch (Exception e) {
			System.out.println("Erro:" + e.getMessage());
		}

		return null;
	}
	
	/**
	 * Passa um sql e uma classe para retorna uma Lista para manipular
	 * @param sql
	 * @param entity
	 * @return
	 */
	public List<?> getSQLListParam(String sql, Class<?> entity) {

		try {
			SQLQuery query = getSession().createSQLQuery(sql);
			query.addEntity(entity);
			return query.list();
		} catch (Exception e) {
			System.out.println("Erro:" + e.getMessage());
		}

		return null;
	}

	/**
	 * Retorna uma lista é pedido uma classe para trazer os valores
	 */
	@Override
	public List<T> findList(Class<T> login) throws Exception {
		validaSessionFactory();

		StringBuilder query = new StringBuilder();
		query.append(" select distinct(entity) from ").append(login.getSimpleName()).append(" entity ORDER BY entity");

		@SuppressWarnings("unchecked")
		List<T> lista = sessionFactory.getCurrentSession().createQuery(query.toString()).list();

		return lista;
	}

	@Override
	public Object findById(Class<T> login, Long id) throws Exception {
		validaSessionFactory();
		Object obj = sessionFactory.getCurrentSession().load(login, id);
		return obj;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class)
	@Override
	public T findByPorId(Class<T> login, Long id) throws Exception {
		validaSessionFactory();
		@SuppressWarnings("unchecked")
		T obj = (T) sessionFactory.getCurrentSession().load(login, id);
		return obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findListByQueryDinamica(String s) throws Exception {
		validaSessionFactory();
		List<T> lista = new ArrayList<T>();
		lista = sessionFactory.getCurrentSession().createQuery(s).list();
		return lista;
	}

	@Override
	public void executeUpdateQueryDinamica(String s) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().createQuery(s).executeUpdate();
		executeFlushSession();
	}

	@Override
	public void executeUpdateSQLDinamica(String s) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().createSQLQuery(s).executeUpdate();
		executeFlushSession();

	}

	@Override
	public void clearSession() throws Exception {
		sessionFactory.getCurrentSession().clear();
	}

	@Override
	public void evict(Object objs) throws Exception {
		validaSessionFactory();
		sessionFactory.getCurrentSession().evict(objs);
	}

	@Override
	public Session getSession() throws Exception {
		validaSessionFactory();
		return sessionFactory.getCurrentSession();
	}

	@Override
	public List<?> getListSQLDinamica(String sql) throws Exception {
		validaSessionFactory();
		List<?> lista = sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		return lista;
	}
	
	@Override
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	@Override
	public SimpleJdbcInsert getSimpleJdbcInsert() {
		return simpleJdbcInsert;
	}

	@Override
	public Long totalRegistro(String table) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" select count(1) from ").append(table);
		return jdbcTemplate.queryForLong(sql.toString());
	}

	@Override
	public Query obterQuery(String query) throws Exception {
		validaSessionFactory();
		Query queryReturn = sessionFactory.getCurrentSession().createQuery(query.toString());
		return queryReturn;
	}

	/**
	 * Realiza consulta no banco de dados, iniciando o carregamento a partir do
	 * registro passado no paramentro -> iniciaNoRegistro e obtendo maximo de
	 * resultados passados em -> maximoResultado.
	 * 
	 * @param query
	 * @param iniciaNoRegistro
	 * @param maximoResultado
	 * @return List<T>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> findListByQueryDinamica(String query, int iniciaNoRegistro, int maximoResultado) throws Exception {
		validaSessionFactory();
		List<T> lista = new ArrayList<T>();
		lista = sessionFactory.getCurrentSession().createQuery(query).setFirstResult(iniciaNoRegistro)
				.setMaxResults(maximoResultado).list();
		return lista;
	}

	private void validaSessionFactory() {
		if (sessionFactory == null) {
			sessionFactory = HibernateUtil.getSessionFactory();
		}
		validaTransaction();
	}

	private void validaTransaction() {
		if (!sessionFactory.getCurrentSession().getTransaction().isActive()) {
			sessionFactory.getCurrentSession().beginTransaction();
		}
	}

	@SuppressWarnings("unused")
	private void commitProcessoAjax() {
		sessionFactory.getCurrentSession().beginTransaction().commit();
	}

	@SuppressWarnings("unused")
	private void rollBackProcessoAjax() {
		sessionFactory.getCurrentSession().beginTransaction().rollback();
	}

	/**
	 * Roda instantaneamente o SQL no banco de dados
	 */
	private void executeFlushSession() {
		sessionFactory.getCurrentSession().flush();
	}

	public List<Object[]> getListSQLDinamicaArray(String sql) throws Exception {
		validaSessionFactory();

		@SuppressWarnings("unchecked")
		List<Object[]> lista = (List<Object[]>) sessionFactory.getCurrentSession().createSQLQuery(sql).list();
		return lista;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T findUniqueByQueryDinamica(String query) throws Exception {
		validaSessionFactory();
		T obj = (T) sessionFactory.getCurrentSession().createQuery(query.toString()).uniqueResult();

		return obj;
	}

	public T findUniqueByProperty(Class<T> login, Object valor, String atributo, String condicao) throws Exception {

		validaSessionFactory();
		StringBuilder query = new StringBuilder();

		query.append(" select entity from ").append(login.getSimpleName()).append(" entity where entity.")
				.append(atributo).append(" = '").append(valor).append("' ").append(condicao);

		T obj = (T) this.findUniqueByQueryDinamica(query.toString());

		return obj;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findListByQueryDinamica(String s, List<String> param, Object... value) throws Exception {
		validaSessionFactory();
		List<T> lista = new ArrayList<T>();
		Query q = HibernateUtil.getCurrentSession().createQuery(s);

		for (String p : param) {
			int i = param.indexOf(p);
			q.setParameter(p, value[i]);
		}
		lista = q.list();
		return lista;
	}

}
