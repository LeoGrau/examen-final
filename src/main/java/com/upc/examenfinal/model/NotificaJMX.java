package com.upc.examenfinal.model;

import javax.jms.MessageListener;
import java.util.Arrays;
import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import org.apache.activemq.ActiveMQConnectionFactory;

public class NotificaJMX implements MessageListener {
    private final Object mutex = new Object();
    private Connection connection;
    private static Session session;
    private MessageProducer publisher;
    private Topic topic;
    private Topic control;
    private String url = "tcp://18.117.118.104:61616";
    private int subscribers = 1;
    private int remaining;
    private int messages = 1;
    private long delay;
    private int batch = 1;


    public static void main(String[] argv) throws Exception {
        NotificaJMX p = new NotificaJMX();
        p.cargaClases();
        MapMessage mx= creaMensajeEmail("MAIL","incidente@vbbc.com.pe","jdelgad@gmail.com","Demo","cuerpo del correo");
        p.run(mx);
    }
    public void cargaClases() throws Exception {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(url);
        connection = factory.createConnection("admin","admin");

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        topic = session.createTopic("messages");
        control = session.createTopic("control");
        publisher = session.createProducer(topic);
        publisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        session.createConsumer(control).setMessageListener(this);
    }

    public void run(MapMessage mx) throws Exception {

        connection.start();
        this.publish(mx);
        waitForCompletion();
        connection.stop();
        connection.close();
    }

    public void publish(MapMessage mx) throws Exception {
        publisher.send(mx);
    }

    public static MapMessage creaMensajeEmail(String tipo, String from, String sendto, String subject, String body)
            throws JMSException {

        MapMessage mapmessage = session.createMapMessage();
        mapmessage.setString("tipo", tipo);
        mapmessage.setString("from", from);
        mapmessage.setString("sendto", sendto);
        mapmessage.setString("subject", subject);
        mapmessage.setString("body", body);

        return mapmessage;
    }

    public MapMessage creaMensajeSMS(String tipo, String phone, String smsmensaje) throws JMSException {
        MapMessage mapmessage = session.createMapMessage();
        mapmessage.setString("tipo", "SMS");
        mapmessage.setString("phone", phone);
        mapmessage.setString("message", smsmensaje);
        //System.out.println(mapmessage);
        return mapmessage;
    }

    public void waitForCompletion() throws Exception {
        System.out.println("Realizando envio...");
        synchronized (mutex) {
            while (remaining > 0) {
                mutex.wait();
            }
        }
    }

    public void onMessage(Message message) {
        synchronized (mutex) {
            System.out.println("Acuse de recibo: ");
            if (remaining == 0) {
                mutex.notify();
            }
        }
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public void setMessages(int messages) {
        this.messages = messages;
    }

    public void setSubscribers(int subscribers) {
        this.subscribers = subscribers;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}