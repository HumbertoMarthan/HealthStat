package br.com.clinica.utils;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailUtils {

	public static void main(String[] args ) {
	}
	
	public static void enviarEmail(String email, String titulo, String mensagem, boolean HTML){
		final String username = "marthan.informatica@gmail.com";
		final String password = "Rpegdsq0";
		
		//Configura Servidor
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		//Pega a sessão do email e autentica com usuario e senha
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {
			//Mostra no console o log 
			session.setDebug(true);
			
			//Inicia Corpo Titulo, destinatario e corpo
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress( username ));
		
			//Seta Valor do Email Cadastrado do Paciente
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			
			//Seta Valor do Tituloo do Email
			message.setSubject(titulo); 
			
			//Seta Valor do conteudo da mensagem
			message.setContent(mensagem, ((HTML) ? "text/html; charset=utf-8" : "text/plain"));
			
			//Envia Email
			Transport.send(message);
			
			System.out.println("Hora Enviada: "+ new Date());
			System.out.println("Enviado com Sucesso!");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		
	}
}
