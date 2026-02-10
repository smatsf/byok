package mil.dod.dha.byok.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.net.ssl.SSLContext;
import java.time.Duration;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.*;
import java.io.*;
import java.net.URI;
import reactor.netty.http.client.HttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import reactor.netty.resources.*;
import reactor.netty.tcp.SslProvider;
@Service
public class SalesforceAuthService {
    private  WebClient webClient;

    @Value("${spring.security.oauth2.client.registration.salesforce.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.salesforce.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.provider.salesforce.token-uri}")
    private String tokenUri;

    public SalesforceAuthService(WebClient.Builder webClientBuilder) throws java.lang.Exception{
        this.webClient = this.createWebClientWithSSL();
    }
 // Method to create WebClient with SSL Context
    public WebClient createWebClientWithSSL() throws Exception {
 // Create an SSLContext
        SSLContext sslContext = SSLContext.getInstance("TLS");
        TrustManager[] trustAllCertificates = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };
        sslContext.init(null, trustAllCertificates, new java.security.SecureRandom());

        // Use HttpClient to configure WebClient with the SSLContext
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setDefaultMaxPerRoute(20);
        cm.setMaxTotal(100);
        
        SystemDefaultRoutePlanner routePlanner = new SystemDefaultRoutePlanner(null);
	ConnectionProvider connectionProvider = ConnectionProvider.builder("Sample Pool")
        .maxConnections(10)
        .pendingAcquireMaxCount(-1)
        .build();

        HttpClient httpClient = HttpClient.create()
		.wiretap(true)
                //.secure(sslContextSpec -> sslContextSpec.sslProvider(SslProvider.defaultClientProvider()))
                //.setConnectionManager(cm)
                //.setRoutePlanner(routePlanner)
		.responseTimeout(Duration.ofMinutes(3L));
               // .build();
        
       // ClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);

        // Build and return the WebClient
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl("https://login.salesforce.com") // Salesforce authentication base URL
                .build();
    }
    public String getAccessToken() {
        String str= webClient.post()
                .uri(tokenUri)
		 .header("Content-Type", "application/x-www-form-urlencoded")
                .bodyValue("grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret)
                .retrieve()
                .bodyToMono(SalesforceTokenResponse.class)
                .block()    //to get response synchronlously
                .getAccessToken();
	System.out.println("-AccessTkn-"+str);
return str;
    }

    private static class SalesforceTokenResponse {
        private String access_token;

        public String getAccessToken() {
            return access_token;
        }
    }
}




