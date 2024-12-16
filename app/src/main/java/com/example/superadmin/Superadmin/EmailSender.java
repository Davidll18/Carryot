package com.example.superadmin.Superadmin;

import android.os.AsyncTask;
import android.util.Log;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {

    // Método para enviar el correo de forma asíncrona
    public static void sendEmail(final String email, final String subject, final String messageBody) {
        new SendEmailTask(email, subject, messageBody).execute();
    }

    // Clase AsyncTask para enviar el correo en segundo plano
    private static class SendEmailTask extends AsyncTask<Void, Void, Void> {
        private String email;
        private String subject;
        private String messageBody;

        // Constructor para inicializar los parámetros del correo
        public SendEmailTask(String email, String subject, String messageBody) {
            this.email = email;
            this.subject = subject;
            this.messageBody = messageBody;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                // Configuración de la sesión de correo con autenticación
                Session session = Session.getInstance(getMailProperties(), new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        // Aquí debes poner tu dirección de correo y contraseña de aplicación
                        return new PasswordAuthentication("coreldrain@gmail.com", "soac kjuc sphk tqsa");
                    }
                });

                // Crear un objeto MimeMessage
                MimeMessage message = new MimeMessage(session);

                // Establecer la dirección del remitente y del receptor
                message.setFrom(new InternetAddress("coreldrain@gmail.com")); // Cambiar a tu email
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

                // Establecer el asunto y el cuerpo del mensaje (en formato HTML)
                message.setSubject(subject);
                message.setContent(messageBody, "text/html; charset=UTF-8"); // Establecer tipo HTML

                // Enviar el mensaje
                Transport.send(message);
                Log.d("EmailSender", "Correo enviado correctamente a " + email);
            } catch (MessagingException e) {
                Log.e("EmailSender", "Error al enviar el correo: ", e);
            }
            return null;
        }

        // Método para obtener las propiedades de la sesión de correo
        private Properties getMailProperties() {
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.gmail.com"); // Cambiar al servidor SMTP de tu elección
            properties.put("mail.smtp.port", "587"); // El puerto para TLS
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true"); // Habilitar TLS
            return properties;
        }
    }
}
