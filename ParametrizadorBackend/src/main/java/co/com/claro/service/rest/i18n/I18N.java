package co.com.claro.service.rest.i18n;

import java.text.MessageFormat;
import java.util.Properties;

public class I18N {

    private static Properties props;

    static {
        try {
            props = new Properties();
            props.load(I18N.class.getClassLoader().getResourceAsStream("/META-INF/es.properties"));
        } catch (Exception e) {
            System.out.println("Error al cargar las propiedades " + e.getMessage());
        }
    }

    public static String getMessage(String key, String... args) {
        String message = props.getProperty(key);
        if (message == null) {
            throw new RuntimeException("No existe la propiedad");
        }
        return MessageFormat.format(message, args);
    }

    public static String getMessage(String key) {
        String message = props.getProperty(key);
        if (message == null) {
            throw new RuntimeException("No existe la propiedad");
        }
        return message;
    }

}
