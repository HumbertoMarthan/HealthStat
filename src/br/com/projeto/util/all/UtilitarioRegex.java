package br.com.projeto.util.all;

public class UtilitarioRegex {
	
	public String retiraAcentos(String string) {
		String aux = new String(string);
		aux = aux.replaceAll("[��������]", "e");
		aux = aux.replaceAll("[��������]", "u");
		aux = aux.replaceAll("[��������]", "i");
		aux = aux.replaceAll("[���������]", "a");
		aux = aux.replaceAll("[���������]", "o");
		return aux;
	}
}
