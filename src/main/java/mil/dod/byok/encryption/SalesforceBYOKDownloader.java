package mil.dod.dha.byok.encryption;
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
import org.springframework.web.bind.annotation.GetMapping;
@RestController
public class SalesforceBYOKDownloader {
    @Value("${spring.security.oauth2.client.provider.salesforce.authorization-uri}")
    private   String SALESFORCE_AUTH_URL = "https://login.salesforce.com/services/oauth2/token";
    @Value("${spring.security.oauth2.client.provider.salesforce.token-uri}")
    private String SALESFORCE_API_URL = "https://your-instance.salesforce.com/services/data/v57.0/sobjects/KeyManagement/your-key-id";
    @Value("${spring.security.oauth2.client.registration.salesforce.client-id}")
    private String CLIENT_ID ="";

    @Value("${spring.security.oauth2.client.registration.salesforce.client-secret}")
    private String CLIENT_SECRET ="";
    private final String USERNAME = "sanjeev.mehrotra@salesforce.com";
        private final String PASSWORD = "P@ssme54321!";
            /*
                public static void main(String[] args) {
                    try {
                        String accessToken = getSalesforceAccessToken();
                        String byokCertificate = downloadBYOKCertificate(accessToken);
                        System.out.println("BYOK Certificate (Base64): " + byokCertificate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            */
                 @GetMapping("/getToken")
                private String getSalesforceAccessToken() throws Exception {
                    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                        HttpPost request = new HttpPost(this.SALESFORCE_AUTH_URL);
                        request.setHeader("Content-Type", "application/x-www-form-urlencoded");
            
                        String requestBody = "grant_type=password"
                                + "&client_id=" + this.CLIENT_ID
                                + "&client_secret=" + this.CLIENT_SECRET
                            + "&username=" + this.USERNAME
                            + "&password=" + this.PASSWORD;

            request.setEntity(new StringEntity(requestBody));

            try (@SuppressWarnings("deprecation")
            CloseableHttpResponse response = httpClient.execute(request);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()))) {

                StringBuilder responseString = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseString.append(line);
                }
                System.out.println("response -"+responseString.toString());
                JSONObject jsonResponse = new JSONObject(responseString.toString());
                return jsonResponse.getString("access_token");
            }
        }
    }
   @GetMapping("/downloadCertificate")
    private String downloadBYOKCertificate(String accessToken) throws Exception {
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

                return new String(Base64.getDecoder().decode(certificateBase64));
            }
        }
    }
}

