package com.upc.examenfinal.api.rest;

import com.upc.examenfinal.model.NotificaJMX;
import com.upc.examenfinal.resources.MailResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.JMSException;
import javax.jms.MapMessage;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static com.upc.examenfinal.model.NotificaJMX.creaMensajeEmail;

@RestController
@RequestMapping(value = "/EListenerUPC", produces = "application/json")
public class BrokerController {

    public BrokerController() {
    }

    @GetMapping("/mail/subject")
    public Map<String, String> getMail() throws Exception {
        NotificaJMX notificaJMX = new NotificaJMX();
        notificaJMX.cargaClases();  // Ensure session is initialized
        MapMessage mx = creaMensajeEmail("MAIL", "incidente@vbbc.com.pe", "jdelgad@gmail.com", "Demo", "cuerpo del correo");

        // Convert MapMessage to a simple Map<String, String>
        Map<String, String> result = new HashMap<>();
        var mapNames = mx.getMapNames();
        while (mapNames.hasMoreElements()) {
            String key = (String) mapNames.nextElement();
            result.put(key, mx.getString(key));
        }

        notificaJMX.run(mx);
        return result;
    }
    @PostMapping("/mail/subject")
    public Map<String, String> subjectFilter(MailResource mailResource) throws Exception {

        NotificaJMX notificaJMX = new NotificaJMX();
        notificaJMX.cargaClases();  // Ensure session is initialized
        MapMessage mx = creaMensajeEmail("MAIL", "incidente@vbbc.com.pe", "jdelgad@gmail.com", "Demo", "cuerpo del correo");

        // Convert MapMessage to a simple Map<String, String>
        Map<String, String> result = new HashMap<>();
        var mapNames = mx.getMapNames();
        while (mapNames.hasMoreElements()) {
            String key = (String) mapNames.nextElement();
            result.put(key, mx.getString(key));
        }

        notificaJMX.run(mx);

        return result;
    }

    @PostMapping("/mail/sendto")
    public Map<String, String> destinyFilter(MailResource mailResource) throws Exception {
        NotificaJMX notificaJMX = new NotificaJMX();
        notificaJMX.cargaClases();  // Ensure session is initialized
        MapMessage mx = creaMensajeEmail("MAIL", "incidente@vbbc.com.pe", "jdelgad@gmail.com", "Demo", "contenido del mail");

        // Convert MapMessage to a simple Map<String, String>
        Map<String, String> result = new HashMap<>();
        var mapNames = mx.getMapNames();
        while (mapNames.hasMoreElements()) {
            String key = (String) mapNames.nextElement();
            result.put(key, mx.getString(key));
        }

        notificaJMX.run(mx);


        return result;
    }


}
