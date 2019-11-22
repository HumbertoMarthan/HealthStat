package br.com.clinica.bean.view;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import br.com.clinica.utils.DatasUtils;

public class Teste {

	public static void main(String[] args) throws IOException {
		
		Calendar c = Calendar.getInstance();
		Date date = new Date();
		
		c.setTimeInMillis(date.getTime());
		c.set(Calendar.HOUR_OF_DAY, 16); // hora 8 da manha 
		c.set(Calendar.MINUTE, 00);
		c.set(Calendar.SECOND, 00);
		
		
		
		Calendar inicio = Calendar.getInstance();
		inicio.set(Calendar.HOUR_OF_DAY, 7); // hora 8 da manha 
		inicio.set(Calendar.MINUTE, 00);
		inicio.set(Calendar.SECOND, 00);
		
		Date dateInicio = inicio.getTime();
		
		Calendar fim = Calendar.getInstance();
		fim.set(Calendar.HOUR_OF_DAY, 16); // hora 17 da tarde
		fim.set(Calendar.MINUTE, 59);
		fim.set(Calendar.SECOND, 0);
		Date dateFim = fim.getTime();
		
		System.out.println("Hora Atual > "+ c.getTime());
		System.out.println(DatasUtils.formatDateSql(inicio.getTime())+ " 00:00:00");
		System.out.println(DatasUtils.formatDateSql(fim.getTime())+ " 23:59:59");

		if(date.getTime() >= inicio.getTimeInMillis() && date.getTime() <= fim.getTimeInMillis()) {
			System.out.println("Esta entre 8 e 17:59");
		}
		else {
			System.out.println("Não está entre 8 e 17:59");
		}
		

		
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
