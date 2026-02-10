package mil.dod.dha.byok.service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Base64;
@Service
public class SalesforceService {
    private final WebClient webClient;
    private final SalesforceAuthService authService;

    @Value("${salesforce.base-url}")
    private String salesforceBaseUrl;
    private String SALESFORCE_API_URL = salesforceBaseUrl + "services/data/v57.0/sobjects/KeyManagement/55719f6a-5378-4485-a839-e83d16906437";

    public SalesforceService(WebClient.Builder webClientBuilder, SalesforceAuthService authService) {
        this.webClient = webClientBuilder.build();
        this.authService = authService;
    }

    @Cacheable("salesforceData")
    public String fetchSalesforceData(String endpoint) {
        String accessToken = authService.getAccessToken();

        return webClient.get()
                .uri(salesforceBaseUrl + endpoint)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
    public String downloadBYOKCertificate() throws Exception {
String accessToken = authService.getAccessToken();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(this.SALESFORCE_API_URL);
            request.setHeader("Authorization", "Bearer " + accessToken);
            request.setHeader("Content-Type", "application/json");

            try (@SuppressWarnings("deprecation")
            CloseableHttpResponse response = httpClient.execute(request);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {

                StringBuilder responseString = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseString.append(line);
                }

                JSONObject jsonResponse = new JSONObject(responseString.toString());
                String certificateBase64 = jsonResponse.getString("EncodedCertificate");	
		System.out.println("CERT--->"+Base64.getDecoder().decode(certificateBase64));	
                return new String(Base64.getDecoder().decode(certificateBase64));
            }
        }
    }
public Map getSalesforceData(String query) {
		//SalesforceAuthenticator salesforceAuthenticator = SalesforceAuthenticator.getSalesforceToken();
		try {
			RestTemplate restTemplate = new RestTemplate();
			String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
			//final String baseUrl = salesforceAuthenticator.instanceUrl + "/services/data/v52.0/query/?q="
			final String baseUrl = salesforceBaseUrl + "/services/data/v52.0/query/?q="
					+ encodedQuery;
			URI uri = new URI(baseUrl);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.add(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", authService.getAccessToken()));
			HttpEntity<?> request = new HttpEntity<Object>(headers);
			ResponseEntity<Map> response = null;
			try {
				response = restTemplate.exchange(uri, HttpMethod.GET, request, Map.class);
			} catch (HttpClientErrorException e) {
				System.out.println(e.getMessage());
			}
			return response.getBody();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return Collections.emptyMap();
	}
}




