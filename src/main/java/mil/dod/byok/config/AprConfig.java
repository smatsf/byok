package mil.dod.dha.byok.config;
import org.apache.catalina.core.AprLifecycleListener;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.Ssl;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class AprConfig {
	@Value("${server.port}")
	private String port;
	@Value("${server.ssl.enabled}")
	private String ssl_enabled;
	@Value("${server.ssl.key-store}")
	private String keyStore;
	@Value("${server.ssl.key-store-password}")
	private String keyStorePassword;
    @Bean
    ServletWebServerFactory servletContainer() {
    TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory() {
      @Override
      public Ssl getSsl() {
        // avoid "IllegalStateException: To use SSL, the connector's protocol handler must be an AbstractHttp11JsseProtocol subclass":
        return null;
      }
    };

    // enable APR:
    factory.setProtocol("org.apache.coyote.http11.Http11NioProtocol");

    AprLifecycleListener aprLifecycleListener = new AprLifecycleListener();

    // will throw "FIPS was not available to tcnative at build time. You will need to re-build tcnative against an OpenSSL with FIPS." with default OpenSSL:
    aprLifecycleListener.setFIPSMode("on");
    factory.addConnectorCustomizers(connector -> {
            connector.setPort(Integer.parseInt(port));
            connector.setSecure(Boolean.parseBoolean(ssl_enabled));
            connector.setScheme("https");
 });
Ssl ssl = new Ssl();
    ssl.setKeyStore(keyStore);
    ssl.setKeyStorePassword(keyStorePassword);
    ssl.setKeyAlias("mycert");
    ssl.setEnabledProtocols(new String[] {"TLSv1.3"});
    factory.setSsl(ssl);
    return factory;
  }
}

