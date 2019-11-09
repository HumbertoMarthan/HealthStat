package br.com.clinica.bean.view;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class Teste {

	public static void main(String[] args) throws IOException {

		Calendar c = Calendar.getInstance();
		c.set(Calendar.DATE, 1);
		c.set(Calendar.MONTH, 11);
		c.set(Calendar.YEAR, 1997);
		System.out.println(c.getTime());
		calculaIdade(c.getTime());
	}
	
	public static Date adicionaDias(Date data) {
		data = new Date();
		
		System.out.println(data);
		
		Calendar c = Calendar.getInstance(); 
		c.setTime(data);
		c.add(Calendar.MONTH, + 2);
		
		data = c.getTime();
		System.out.println("sss" +data);
		
		return data = c.getTime();
	}

	public static Integer calculaIdade(Date data) {
		Calendar dataNascimento = Calendar.getInstance();
			dataNascimento.setTime(data);
			Calendar dataAtual = Calendar.getInstance();

			Integer diferencaMes = dataAtual.get(Calendar.MONTH) - dataNascimento.get(Calendar.MONTH);
			Integer diferencaDia = dataAtual.get(Calendar.DAY_OF_MONTH) - dataNascimento.get(Calendar.DAY_OF_MONTH);
			Integer idade = (dataAtual.get(Calendar.YEAR) - dataNascimento.get(Calendar.YEAR));
			
			System.out.println(idade);
			if(diferencaMes < 0 || (diferencaMes == 0 && diferencaDia < 0)) {
				System.out.println(idade); ;
			}
			System.out.println("sss" +idade);
			 
			return idade;
	}
}
