package mil.dod.dha.byok;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.annotation.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;

@SpringBootApplication
public class ByokApplication {
/*	@Bean

    public TomcatServletWebServerFactory servletWebServerFactory() {

        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();

        factory.addConnectorCustomizers((Connector connector) -> {

            connector.setScheme("https");

            connector.setSecure(true);

            connector.setPort(8443); // Custom HTTPS port

            // Set keystore details here

        });

        return factory;

    }*/
	public static void main(String[] args) {
//		logger.debug("Starting BYOK");
        System.setProperty("spring.profiles.active", "errorhandling");
        SpringApplication.run(ByokApplication.class, args);
	}

}
