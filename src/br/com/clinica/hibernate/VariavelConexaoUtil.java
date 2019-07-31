package br.com.clinica.hibernate;

import java.io.Serializable;

/*
 * CAMINHO DO JNDI TOMCAT
 * 
 * */
public class VariavelConexaoUtil implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String JAVA_COMP_ENV_JDBC_DATA_SOURCE = "java:/comp/env/jdbc/datasource";

}
