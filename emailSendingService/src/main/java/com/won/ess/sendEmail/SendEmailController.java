package com.won.ess.sendEmail;

import java.util.Locale;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class SendEmailController {

private static final Logger logger = LoggerFactory.getLogger(SendEmailController.class);
	
	@RequestMapping(value="/send/input", method =RequestMethod.GET)
	public String sendMessage(Locale locale, Model model) {
		logger.info("you can send email - locale is {}.", locale);
	
		return "sendMsg/inputForm";
	}
	
	/**
	* 네이버 이메일 발송
	* 
	* @ param HttpServletRequest request, ModelMap mo
	* @ return page 주소
	* @ exception AddressException, MessagingException
	*/
	@RequestMapping(value="/send/email")
	public String mailSender(HttpServletRequest request, ModelMap mo) throws AddressException, MessagingException {
		
		//네이버인 경우 smtp.naver.com
		//구글인 경우 smtp.google.com
		String host = "smtp.naver.com";
		
		final String username = "naver_id";		//네이버 아이디. @naver.com는 작성하지 않습니다.
		final String password = "password";		//네이버 이메일 비밀번호 
		int port = 465;		//포트 다를 수도 있음	 	(네이버 : SMTP 포트 번호 : 465 / 보안 연결 필요 (SSL) 또는 587 / 보안 연결 필요 (TLS))
		
		String recipient = "email_id@naver.com";	//받는 사람 이메일
		String subject = "메일 테스트 제목";		//메일 제목
		String body = username + "님으로 부터 메일을 받았습니다. ";		//메일 내용
		
		Properties props = System.getProperties();	//정보를 담기 위한 객체 생성
		
		//smtp 서버 정보 설정
		props.put("mail.smtp.host", host);	
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.ssl.trust", host);
		
		//Session 생성
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			String un = username;
			String pw = password;
			protected javax.mail.PasswordAuthentication getPasswordAuthentication(){
				return new javax.mail.PasswordAuthentication(un, pw);
			}
		});
		session.setDebug(true);	//for debug
		
		try {
			Message mimeMessage = new MimeMessage(session);		//MimeMessage 생성
			mimeMessage.setFrom(new InternetAddress("naver_id@naver.com"));	//발신자 setting, 보내는 사람 이메일 전체주소 작성
			mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));	//수신자 이메일	
			//.TO 외에 .CC(참조) .BCC(숨은참조)도 있습니다.
			
			mimeMessage.setSubject(subject);	//제목 setting
			mimeMessage.setText(body);			//내용 setting
			Transport.send(mimeMessage); 	//javax.mail.Transport.send() 이용
		}catch (AddressException e) {
			e.printStackTrace(); 
		} catch (MessagingException e) { 
			e.printStackTrace(); 
		}


		return "sendMsg/resultForm";
	}
	
	
	/**
	* 구글 이메일 발송
	* 
	* @ param HttpServletRequest request, ModelMap mo
	* @ return page 주소
	* @ exception AddressException, MessagingException
	*/
	@RequestMapping(value="/send/gmail")	
	public String gmailSender(HttpServletRequest request, ModelMap mo) throws AddressException, MessagingException {
		
		//네이버인 경우 smtp.naver.com
		//구글인 경우 smtp.google.com
		String host = "smtp.naver.com";
		
		final String username = "gmail_id";		
		final String password = "password*";
		int port = 465;	
		
		String recipient = "email_id@naver.com";
		String subject = "메일 테스트 제목";
		String body = username + "님으로 부터 메일을 받았습니다. ";
		
		Properties props = System.getProperties();
		
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.ssl.trust", host);
		
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			String un = username;
			String pw = password;
			protected javax.mail.PasswordAuthentication getPasswordAuthentication(){
				return new javax.mail.PasswordAuthentication(un, pw);
			}
		});
		session.setDebug(true);
		
		try {
			Message mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress("gmail_id@gmail.com"));	//발신자
			mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));	//수신자
			
			mimeMessage.setSubject(subject);
			mimeMessage.setText(body);
			Transport.send(mimeMessage); 	//javax.mail.Transport.send() 이용
		}catch (AddressException e) {
			// TODO Auto-generated catch block 
			e.printStackTrace(); 
		} catch (MessagingException e) { 
			// TODO Auto-generated catch block 
			e.printStackTrace(); 
		}


		return "sendMsg/resultForm";
	}
	
}
