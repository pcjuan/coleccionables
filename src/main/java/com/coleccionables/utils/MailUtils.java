package com.coleccionables.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.coleccionables.models.entity.Producto;
import com.coleccionables.models.entity.User;

public class MailUtils {

	private Logger logger = LoggerFactory.getLogger(MailUtils.class);

	@Value("${spring.mail.username}")
	private String emailFrom;

	@Autowired
	private JavaMailSender javaMailSender;

	/**
	 * Metodo encargado de enviar un correo usando los parametros seteados en el
	 * <b>application.properties</b>
	 * <ul>
	 * <li>spring.mail.host</li>
	 * <li>spring.mail.username</li>
	 * <li>spring.mail.password</li>
	 * <li>spring.mail.port</li>
	 * <li>spring.mail.properties.mail.smtp.starttls.enable</li>
	 * </ul>
	 * 
	 * Se recepciona un usuario al cual enviar el correo y un listado de productos
	 * el cual se enviará como información en el correo (TODO)
	 * 
	 * @param user
	 * @param productos
	 * @throws MailException
	 */
	public void sendMail(User user, List<Producto> productos) throws MailException {

		try {

			SimpleMailMessage mail = new SimpleMailMessage();
			mail.setTo(user.getEmailAddress());
			mail.setFrom(this.emailFrom);
			mail.setSubject("Sujeto de prueba");
			mail.setText(user.getFirstName() + " " + user.getLastName());

			this.javaMailSender.send(mail);

		} catch (Exception ex) {
			this.logger.error(ex.toString());
		}
	}

}
